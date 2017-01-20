package com.jeromyang.transmssion;

import com.jeromyang.transmssion.model.MessageModel;
import com.jeromyang.transmssion.utils.RxObservableUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Jeromeyang on 2017/1/16.
 * 发送消息API
 */

public class SendOperation {
    private static MessageSend mMessageSend;
    public static Subscription timer;

    public static void init(MessageSend messageSend) {
        mMessageSend = messageSend;
        stopMessageSend();
    }

    public static SendOperation getInstance() {
        return SendOperationHolder.sendOperation;
    }

    private static class SendOperationHolder {
        private static final SendOperation sendOperation = new SendOperation();
    }

    /**
     * 发送文件请求
     */
    public void sendFileRequest() {


    }

    public void sendMessage(final MessageModel messageModel, final int timeOut) {
        RxObservableUtil.getObservable().subscribe(new Action1() {
            @Override
            public void call(Object o) {
                try {
                    mMessageSend.setSendMessage(messageModel);
                    if (messageModel.getReplyType() == MessageModel.REPLY_TYPE_REQUEST) {  //如果是要求回复的消息，加入到待回复的列表
                        MessageHandler.getInstance().addToReplyMessage(messageModel);
                    }
                    mMessageSend.setWaited(false);
                    setMessageSendTimeOut(timeOut);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setMessageSendTimeOut(final int timeOut) {
        if (timer != null) {
            timer.unsubscribe();
        }
        timer = Observable.interval(0, 1, TimeUnit.SECONDS)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (aLong >= timeOut) {
                            mMessageSend.setWaited(true);
                        }
                    }
                });
    }

    public static void stopMessageSend() {
        mMessageSend.setWaited(true);
    }
}
