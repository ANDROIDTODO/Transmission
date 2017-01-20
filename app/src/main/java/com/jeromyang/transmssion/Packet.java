package com.jeromyang.transmssion;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.io.ByteArrayOutputStream;

/**
 * Created by Jeromeyang on 2017/1/13.
 * 获取相应的数据包格式
 */

public class Packet {

    public static final int DATA_TYPE_ONLINE = 0xA1;
    public static final int DATA_TYPE_REQUEST = 0xA2;
    public static final int DATA_TYPE_RESPONSE = 0xA3;
    public static final int DATA_TYPE_SUCCESS = 0xA4;
    public static final int DATA_TYPE_MESSAGE = 0xA5;


    private WifiManager wifiManager;


    private ByteArrayOutputStream onlineBroadcast;


    private Packet() {
    }

    public static Packet getInstance() {

        return INSTANCE.instance;
    }


    private static class INSTANCE {
        static Packet instance = new Packet();
    }

    /**
     * 封装头部唯一标识 4 byte
     *
     * @param byteArrayOutputStream
     */
    private void headerWapper(ByteArrayOutputStream byteArrayOutputStream) {
        byteArrayOutputStream.write(0xDE);
        byteArrayOutputStream.write(0xAD);
        byteArrayOutputStream.write(0xBE);
        byteArrayOutputStream.write(0xDF);
    }

    /**
     * 封装协议版本号 1byte
     *
     * @param byteArrayOutputStream
     */
    private void protocolWapper(ByteArrayOutputStream byteArrayOutputStream) {
        byteArrayOutputStream.write(0x01);
    }

    /**
     * 封装四个字节的源地址 4 byte
     *
     * @param byteArrayOutputStream
     */
    private boolean sourceIpWapper(ByteArrayOutputStream byteArrayOutputStream) {

        int sourceIp = getWifiManager().getConnectionInfo().getIpAddress();

        if (sourceIp == 0) {
            return false;
        }


        //正序
        byteArrayOutputStream.write(sourceIp & 0xff);
        byteArrayOutputStream.write((sourceIp >> 8) & 0xff);
        byteArrayOutputStream.write((sourceIp >> 16) & 0xff);
        byteArrayOutputStream.write((sourceIp >> 24) & 0xff);
        // [192][168][1][1]

        return true;
    }

    /**
     * 封装目标地址 4 byte
     *
     * @param byteArrayOutputStream
     * @param destinationIp
     * @return
     */
    private boolean destinationIpWapper(ByteArrayOutputStream byteArrayOutputStream, int destinationIp) {

        if (destinationIp == -1) {// 封装的地址为当前网段的广播地址

            int broadcastIp = getCurrentBroadcastIp();

            byteArrayOutputStream.write(broadcastIp & 0xff);
            byteArrayOutputStream.write((broadcastIp >> 8) & 0xff);
            byteArrayOutputStream.write((broadcastIp >> 16) & 0xff);
            byteArrayOutputStream.write((broadcastIp >> 24) & 0xff);

        } else {


//            if (bytes.length != 4) {
//                return false;
//            } else {
            byteArrayOutputStream.write(destinationIp & 0xff);
            byteArrayOutputStream.write(destinationIp >> 8 & 0xff);
            byteArrayOutputStream.write(destinationIp >> 16 & 0xff);
            byteArrayOutputStream.write(destinationIp >> 24 & 0xff);

//        }
        }


        return true;

    }

    /**
     * 封装数据类型 1 byte
     *
     * @param byteArrayOutputStream
     * @param type
     */
    private void dataTypeWapper(ByteArrayOutputStream byteArrayOutputStream, int type) {
        byteArrayOutputStream.write(type);
    }

    /**
     * 获取发送的的在线通知
     *
     * @return
     */
    public ByteArrayOutputStream getSendOnlineBroadCast() {

        if (onlineBroadcast == null) {
            onlineBroadcast = new ByteArrayOutputStream();
            headerWapper(onlineBroadcast);
            protocolWapper(onlineBroadcast);
            dataTypeWapper(onlineBroadcast, DATA_TYPE_ONLINE);
            sourceIpWapper(onlineBroadcast);
            destinationIpWapper(onlineBroadcast, -1);
        }

        return onlineBroadcast;
    }

    /**
     * 获取对应数据类型的OutputStream
     *
     * @return
     */
    public ByteArrayOutputStream getSendOutputStream(int dataType, int destinationIp) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        headerWapper(outputStream);
        protocolWapper(outputStream);
        dataTypeWapper(outputStream, dataType);
        sourceIpWapper(outputStream);
        destinationIpWapper(outputStream, destinationIp);
        return outputStream;
    }


    private WifiManager getWifiManager() {
        if (wifiManager == null) {
            wifiManager = (WifiManager) App.context.getSystemService(Context.WIFI_SERVICE);
        }

        return wifiManager;
    }

    public String getCurrentBroadcastIpName() {
        return intToIp(getCurrentBroadcastIp());
    }


    private int getCurrentBroadcastIp() {
        int sourceIp = getWifiManager().getConnectionInfo().getIpAddress();

        return sourceIp | 0xFF000000;
    }

    public int getCurrentHostIp() {
        return getWifiManager().getConnectionInfo().getIpAddress();
    }


    public static String intToIp(int ipInt) {
        return String.valueOf((ipInt & 0xff)) + '.' +
                ((ipInt >> 8) & 0xff) + '.' +
                ((ipInt >> 16) & 0xff) + '.' + ((ipInt >> 24) & 0xff);
    }

}
