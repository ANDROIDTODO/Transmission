package com.jeromyang.transmssion;

import android.util.Log;
import android.widget.Toast;

import com.jeromyang.transmssion.model.MessageModel;
import com.jeromyang.transmssion.model.SendInfo;
import com.jeromyang.transmssion.utils.RxObservableUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.functions.Action1;

/**
 * Created by wangxi on 2017/1/18.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/1/18
 * update time : 2017/1/18
 * last modify : wangxi
 */

public class MessageHandler {

    private final Map<String, MessageModel> toReplyMessageMap = new HashMap<>();
    private final List<MessageModel> messageModels = new CopyOnWriteArrayList<>();
    private boolean isHandling = false;

    public static MessageHandler getInstance() {
        return MessageHandlerHolder.sInstance;
    }

    private final static class MessageHandlerHolder {
        public static final MessageHandler sInstance = new MessageHandler();
    }

    private MessageHandler() {

    }

    public void handleMessage(final MessageModel message) {
        if (!ifContainMessage(message)) {
            Log.e("MessageHandler", "tag2--handleMessage");
            messageModels.add(message);
            if (!isHandling) {
                isHandling = true;
                try {
                    while (!messageModels.isEmpty()) {
                        handleSingleMessage(messageModels.remove(0));
                    }
                } catch (Exception e) {

                } finally {
                    isHandling = false;
                }
            }
        }
    }

    private boolean ifContainMessage(MessageModel message) {
        synchronized (this) {
            for (int i = 0; i < messageModels.size(); i++) {
                if (messageModels.get(i).getMessageId().equals(message.getMessageId())) {
                    return true;
                }
            }
            return false;
        }
    }

    private void handleSingleMessage(final MessageModel message) {
        if (message.getMessageType() == MessageModel.SEND_INFO) {
            if (message.getReplyType() == MessageModel.REPLY_TYPE_REQUEST) { //请求类型时，回复消息
                handleRequestMessage(message, new SendInfo().getJsonString());
            } else if (message.getReplyType() == MessageModel.REPLY_TYPE_RESPONSE) {
                handleResponseMessage(message);
            } else {
                RxObservableUtil.getMainThreadObservable().subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        Toast.makeText(App.context, "普通消息 content=" + message.getData(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void handleResponseMessage(final MessageModel message) {
        if (toReplyMessageMap.containsKey(message.getReplyMessageId())) {
            Log.e("MessageHandler", "tag2--收到回复消息");
            SendOperation.getInstance().stopMessageSend();
            toReplyMessageMap.remove(message.getReplyMessageId());
            RxObservableUtil.getMainThreadObservable().subscribe(new Action1() {
                @Override
                public void call(Object o) {
                    Toast.makeText(App.context, "收到回复，停止发送消息 content=" + message.getData(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            RxObservableUtil.getMainThreadObservable().subscribe(new Action1() {
                @Override
                public void call(Object o) {
                    Toast.makeText(App.context, "无对应请求，或请求已处理，不做处理" + message.getData(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleRequestMessage(MessageModel message, String responseContent) {
        MessageModel messageModel = MessageModel.createMessage(message.getMessageType(),
                message.getSourceIp(), responseContent).setReplyTypeResponse(message.getMessageId());
        SendOperation.getInstance().sendMessage(messageModel, 10);
        Log.e("MessageHandler", "tag2--回复消息");
        RxObservableUtil.getMainThreadObservable().subscribe(new Action1() {
            @Override
            public void call(Object o) {
                Toast.makeText(App.context, "回复消息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 添加待回复的Message
     **/
    public void addToReplyMessage(MessageModel messageModel) {
        if (!toReplyMessageMap.containsKey(messageModel.getMessageId())) {
            toReplyMessageMap.put(messageModel.getMessageId(), messageModel);
        }
    }
}
