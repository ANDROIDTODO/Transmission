package com.jeromyang.transmssion;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeromyang.transmssion.event.OnlineEvent;
import com.jeromyang.transmssion.model.OnlineModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Jeromeyang on 2017/1/15.
 */

public class OnlineRecyclerAdapter extends RecyclerView.Adapter<OnlineRecyclerAdapter.ViewHolder>{

    private final List<OnlineModel> models = new ArrayList<>();


    public void setDataList(final List<OnlineModel> modelss, final int position){


        Observable.just(models)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OnlineModel>>() {
                    @Override
                    public void call(List<OnlineModel> onlineModels) {
                        synchronized (models){
                            models.clear();
                        if (models.size() == modelss.size()){

                            models.addAll(modelss);
                            notifyItemChanged(position);
                        }else if (models.size() > modelss.size()){ //删除
                            notifyItemRemoved(position);
                            models.addAll(modelss);

                        }else if (models.size() < modelss.size()){
                            models.addAll(modelss);

                            notifyItemInserted(0);
                        }
                        notifyDataSetChanged();
                            EventBus.getDefault().post(new OnlineEvent(1,models.size(),0));
                    }}
                });

    }



    @Override
    public OnlineRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(models.get(position).getName());
        holder.ip_name.setText(Packet.intToIp(models.get(position).getSourceIp()));
    }



    @Override
    public int getItemCount() {
        return models.size();
    }

    public void addItem(OnlineModel onlineModel){
        models.add(0,onlineModel);
        Observable.just(onlineModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OnlineModel>() {
                    @Override
                    public void call(OnlineModel onlineModel) {
                        notifyItemChanged(0,onlineModel);
                    }
                });

    }

    public void remove(int position){
        models.remove(position);

        Observable.just(position)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        notifyItemRemoved(integer);
                    }
                });

    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView ip_name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            ip_name = (TextView) itemView.findViewById(R.id.ip_name);

        }
    }
}
