package com.jeromyang.transmssion.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.jeromyang.transmssion.App;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Jeromeyang on 2016/10/31.
 */

public class ToastUtil {

    private static Toast mToast;

    public static void toast(int resId) {
        Observable.just(resId)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return App.context.getResources().getString(integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            if (mToast != null) {
                                mToast.cancel();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mToast = Toast.makeText(App.context, s, Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                        mToast.show();
                    }
                });
    }

    public static void toast(String msg) {
        Observable.just(msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            mToast.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mToast = Toast.makeText(App.context, s, Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                        mToast.show();
                    }
                });
    }

    public static void toastBottom(int resId) {
        Observable.just(resId)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return App.context.getResources().getString(integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            if (mToast != null) {
                                mToast.cancel();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mToast = Toast.makeText(App.context, s, Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                        mToast.show();
                    }
                });
    }

}
