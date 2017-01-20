package com.jeromyang.transmssion.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by wangxi on 2017/1/17.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/1/17
 * update time : 2017/1/17
 * last modify : wangxi
 */

public class MessageModel extends Model {

    public static final int SEND_INFO = 1;

    public static final int REPLY_TYPE_NORMAL = 1;
    public static final int REPLY_TYPE_REQUEST = 2;
    public static final int REPLY_TYPE_RESPONSE = 3;


    //消息类型
    private int messageType;
    private String messageId;
    private String replyMessageId;
    private long createTime;
    private int replyType = REPLY_TYPE_NORMAL;   //1，不需要回复，2，request,3,response

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public JSONObject getMessageJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("messageType", messageType);
            json.put("replyType", replyType);
            json.put("replyMessageId", replyMessageId);
            json.put("data", data);
            json.put("messageId", messageId);
            json.put("createTime", createTime);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return json;
        }
    }

    public static MessageModel createMessage(int messageType, int desIp, String content) {
        MessageModel message = new MessageModel();
        message.createTime = System.currentTimeMillis();
        message.messageId = productUUID();
        message.setDestinationIp(desIp);
        message.messageType = messageType;
        message.data = content;
        return message;
    }

    public int getReplyType() {
        return replyType;
    }

    public MessageModel setReplyTypeRequest() {
        this.replyType = REPLY_TYPE_REQUEST;
        return this;
    }

    public MessageModel setReplyTypeResponse(String replyMessageId) {
        this.replyMessageId = replyMessageId;
        this.replyType = REPLY_TYPE_RESPONSE;
        return this;
    }

    public String getReplyMessageId() {
        return replyMessageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void handleData(byte[] packet, int offset) {
        if (packet.length >= offset + 2) {
            int dataLength = (packet[offset] << 8) | (packet[offset + 1]);
            offset += 2;
            try {
                String data = new String(packet, offset, dataLength, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                messageType = jsonObject.getInt("messageType");
                replyType = jsonObject.getInt("replyType");
                this.data = jsonObject.getString("data");
                messageId = jsonObject.getString("messageId");
                createTime = jsonObject.getLong("createTime");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String productUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }
}
