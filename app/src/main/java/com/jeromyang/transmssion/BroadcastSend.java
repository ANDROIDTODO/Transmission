package com.jeromyang.transmssion;

import com.jeromyang.transmssion.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Jeromeyang on 2017/1/14.
 * 发送在线通知
 */

public class BroadcastSend extends Thread {

    private ByteArrayOutputStream outputStream;

    private JSONObject onLine = new JSONObject();

    private boolean send = true;

    public void setSend(boolean send) {
        this.send = send;
    }

    BroadcastSend() {
        try {
            onLine.put("dev_name", SharedPreferencesUtils.getName());
            TLog.e("BroadcastSend");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        super.run();

        while (send) {

            sendBroadcast(TransmissionHelper.getDatagramSocket(T.BROADCAST_UDP_PORT));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private ByteArrayOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = Packet.getInstance().getSendOnlineBroadCast();
//            outputStream = Packet.getInstance().getSendOnlineBroadCast();
            byte[] sendJson = onLine.toString().getBytes();
            outputStream.write((sendJson.length >> 8) & 0xff);
            outputStream.write(sendJson.length & 0xff);
            outputStream.write(sendJson);
        }

        return outputStream;
    }

    private void sendBroadcast(DatagramSocket datagramSocket) {


        try {
            InetAddress inetAddress = InetAddress.getByName(Packet.getInstance().getCurrentBroadcastIpName());
            ByteArrayOutputStream byteArrayOutputStream = getOutputStream();
//            TLog.e("send port :"+ datagramSocket.getPort());
            DatagramPacket datagramPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length, inetAddress, T.BROADCAST_UDP_PORT);
            datagramSocket.send(datagramPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
