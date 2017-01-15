package com.jeromyang.transmssion.model;


import com.jeromyang.transmssion.event.OnlineEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Jeromeyang on 2017/1/15.
 */

public class OnlineModel extends Model {

    public Subscription timer;




    public void start(){
         timer = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (aLong == 5) {
                            EventBus.getDefault().post(new OnlineEvent(0,getSourceIp()));
                        }
                    }
                });
    }

    public void release(){
        if (timer!=null){
        timer.unsubscribe();}
    }


}
