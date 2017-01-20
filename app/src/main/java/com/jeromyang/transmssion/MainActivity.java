package com.jeromyang.transmssion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jeromyang.transmssion.base.BaseActivity;
import com.jeromyang.transmssion.event.OnlineEvent;
import com.jeromyang.transmssion.model.DataResult;
import com.jeromyang.transmssion.model.MessageModel;
import com.jeromyang.transmssion.model.OnlineModel;
import com.jeromyang.transmssion.model.SendInfo;
import com.jeromyang.transmssion.utils.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends BaseActivity {


    final CopyOnWriteArrayList<OnlineModel> onlineModels = new CopyOnWriteArrayList<>();

    TextView setTe;
    RecyclerView onLineListView;
    Toolbar toolbar;
    OnlineRecyclerAdapter onlineRecyclerAdapter;

    private BroadcastDiscover broadcastDiscover;
    private BroadcastSend broadcastSend;
    private MessageSend messageSend;
    private MessageReceiver messageReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        broadcastSend.setSend(false);
        broadcastDiscover.setReceive(false);
        messageReceiver.setReceive(false);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        initView();

        broadcastDiscover = new BroadcastDiscover(new BroadcastDiscover.Listener() {
            @Override
            public void receiver(DataResult dataResult) {
                if (dataResult.getType() == Packet.DATA_TYPE_ONLINE) { //收到在线通知
                    OnlineModel onlineModel = (OnlineModel) dataResult.getT();
                    int result = isExist(onlineModel);
                    if (result != -1) {
                        onlineModels.remove(result);
                        onlineModels.add(result, onlineModel);
                        onlineRecyclerAdapter.setDataList(onlineModels, result);
                    } else {
                        onlineModels.add(0, onlineModel);
                        onlineRecyclerAdapter.setDataList(onlineModels, 0);
                    }

                    onlineModel.start();
//                    TLog.e("当前局域网人数为： " + onlineModels.size());


                }

            }
        });
        broadcastDiscover.start();

        broadcastSend = new BroadcastSend();
        broadcastSend.start();

        messageReceiver = new MessageReceiver();
        messageReceiver.start();
        messageSend = new MessageSend();
        messageSend.start();
        SendOperation.getInstance().init(messageSend);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTe = (TextView) findViewById(R.id.receive);
        onLineListView = (RecyclerView) findViewById(R.id.online_list);
        onlineRecyclerAdapter = new OnlineRecyclerAdapter();
        onlineRecyclerAdapter.setOnItemClickListener(new OnlineRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, final OnlineModel onlineModel) {
                Toast.makeText(App.context, "发送消息 desIp=" + onlineModel.getSourceIp(), Toast.LENGTH_SHORT).show();
                SendOperation.getInstance().sendMessage(MessageModel.createMessage(MessageModel.SEND_INFO, onlineModel.getSourceIp(),
                        new SendInfo().getJsonString()), 10);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        onLineListView.setLayoutManager(layoutManager);
        onLineListView.setItemAnimator(new DefaultItemAnimator());
        onLineListView.setAdapter(onlineRecyclerAdapter);

        toolbar.inflateMenu(R.menu.base_toolbar_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int menuId = item.getItemId();
                if (menuId == R.id.action_setting) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 0xaa);
                    } catch (android.content.ActivityNotFoundException ex) {
                    }
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 0xaa:
                Uri selectedMediaUri = data.getData();
                TLog.e("choice path : " + FileUtil.getPath(this, selectedMediaUri));
                break;
            default:
                break;
        }
    }

    private void updateOnline(final int size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTe.setText("当前局域网在线人数：" + size);
            }
        });

    }


    private int isExist(OnlineModel onlineModel) {
        for (int i = 0; i < onlineModels.size(); i++) {
            if (onlineModels.get(i).getSourceIp() == onlineModel.getSourceIp()) {
                onlineModels.get(i).release();
                onlineModels.get(i).timer = null;
                return i;
            }
        }

        return -1;
    }

    private int isExist(int sourceIp) {
        for (int i = 0; i < onlineModels.size(); i++) {
            if (onlineModels.get(i).getSourceIp() == sourceIp) {
                onlineModels.get(i).release();
                onlineModels.get(i).timer = null;
                return i;
            }
        }

        return -1;
    }


    @Subscribe
    public void timeout(OnlineEvent onlineEvent) {
        synchronized (onlineModels) {
            if (onlineEvent.getType() == 0) {
                int result = isExist(onlineEvent.getSourceIp());
                if (result != -1) {
                    onlineModels.remove(result);
                    onlineRecyclerAdapter.setDataList(onlineModels, result);
                    TLog.e("time out 当前人数:" + onlineModels.size());

                }
            } else if (onlineEvent.getType() == 1) {
                updateOnline(onlineEvent.getSize());
            }
        }
    }
}
