package com.jeromyang.transmssion.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangxi on 2017/1/4.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/1/4
 * update time : 2017/1/4
 * last modify : wangxi
 */

public class RxObservableUtil {
    public static Observable getObservable() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable getMainThreadObservable() {
        return getObservable().observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable getWaitObservable(long milliSeconds) {
        return getMainThreadObservable().delay(milliSeconds, TimeUnit.MILLISECONDS);
    }
}
