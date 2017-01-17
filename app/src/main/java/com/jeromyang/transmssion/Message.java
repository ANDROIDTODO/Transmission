package com.jeromyang.transmssion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangxi on 2017/1/17.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/1/17
 * update time : 2017/1/17
 * last modify : wangxi
 */

public class Message {
    private int type;
    private String content;
    private long createTime;
    private int desIp;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDesIp() {
        return desIp;
    }

    public void setDesIp(int desIp) {
        this.desIp = desIp;
    }

    public JSONObject getMessageJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("content", content);
            json.put("createTime", createTime);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return json;
        }
    }

    public Message createMessage(int type, int desIp, String content) {
        Message message = new Message();
        message.createTime = System.currentTimeMillis();
        message.desIp = desIp;
        message.type = type;
        message.content = content;
        return message;
    }
}
