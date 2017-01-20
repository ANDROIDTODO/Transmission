package com.jeromyang.transmssion.model;

import com.jeromyang.transmssion.Packet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class Model {

    protected int sourceIp;
    protected int destinationIp;
    protected String dev_name;
    protected String name;
    protected String data;
    private int dataType;

    public int getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(int sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(int destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void handleData(byte[] packet, int offset) {
        if (packet.length >= offset + 2) {
            int dataLength = (packet[offset] << 8) | (packet[offset + 1]);
            offset += 2;
            try {
                String data = new String(packet, offset, dataLength, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                setData(data);
                setName(jsonObject.getString("dev_name"));
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 默认过滤当前ip
     **/
    public boolean dataResult() {
        return sourceIp != 0 && sourceIp != Packet.getInstance().getCurrentHostIp();
    }

    @Override
    public String toString() {
        return "Model{" +
                "sourceIp=" + sourceIp +
                ", destinationIp=" + destinationIp +
                ", dev_name='" + dev_name + '\'' +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
