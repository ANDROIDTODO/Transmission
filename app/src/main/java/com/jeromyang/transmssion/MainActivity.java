package com.jeromyang.transmssion;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jeromyang.transmssion.base.BaseActivity;
import com.jeromyang.transmssion.event.OnlineEvent;
import com.jeromyang.transmssion.model.DataResult;
import com.jeromyang.transmssion.model.OnlineModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends BaseActivity {


    final CopyOnWriteArrayList<OnlineModel> onlineModels = new CopyOnWriteArrayList<>();

    TextView setTe;
    private BroadcastDiscover broadcastDiscover;
    private BroadcastSend broadcastSend;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        broadcastSend.setSend(false);
        broadcastDiscover.setReceive(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTe = (TextView) findViewById(R.id.receive);


         broadcastDiscover = new BroadcastDiscover(new BroadcastDiscover.Listener() {
            @Override
            public void receiver(DataResult dataResult) {
                if (dataResult.getType() == Packet.DATA_TYPE_ONLINE){ //收到在线通知
                    OnlineModel onlineModel = (OnlineModel) dataResult.getT();
                    int result = isExist(onlineModel);
                    if (result != -1){
                        onlineModels.remove(result);
                        onlineModels.add(onlineModel);

                    }else {
                        onlineModels.add(onlineModel);
                    }
                    onlineModel.start();
                    TLog.e("当前局域网人数为： " + onlineModels.size());


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateOnline();
                        }
                    });

                }

            }
        });
        broadcastDiscover.start();

         broadcastSend = new BroadcastSend();
        broadcastSend.start();
    }


    private void updateOnline(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (onlineModels){
                    setTe.setText("当前局域网在线人数：" +onlineModels.size());

                }
            }
        });

    }


    private int isExist(OnlineModel onlineModel){
        for (int i = 0 ; i < onlineModels.size() ; i ++){
            if (onlineModels.get(i).getSourceIp() == onlineModel.getSourceIp()){
                onlineModels.get(i).release();
                onlineModels.get(i).timer = null;
                return i;
            }
        }

        return -1;
    }

    private int isExist(int sourceIp){
        for (int i = 0 ; i < onlineModels.size() ; i ++){
            if (onlineModels.get(i).getSourceIp() == sourceIp){
                onlineModels.get(i).release();
                onlineModels.get(i).timer = null;
                return i;
            }
        }

        return -1;
    }


    @Subscribe
    public void timeout(OnlineEvent onlineEvent){
        int result = isExist(onlineEvent.getSourceIp());
        if (result!=-1){
            onlineModels.remove(result);
            TLog.e("time out 当前人数:" + onlineModels.size());
            updateOnline();
        }
    }
}
