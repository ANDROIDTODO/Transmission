package com.jeromyang.transmssion;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Jeromeyang on 2017/1/16.
 * 用来发送操作消息
 * 时间由外部控制
 */

public class MessageSend extends Thread {

    private boolean isWaited;

    private ByteArrayOutputStream outputStream;

    private DatagramSocket datagramSocket;

    private InetAddress desAddress;




    public MessageSend() {
        setDaemon(true);
        datagramSocket = TransmissionHelper.getDatagramSocket();

    }

    public void setWaited(boolean waited){
        this.isWaited = waited;
        if (!isWaited){
            waitMessage(false);
        }else {
            outputStream = null;
            desAddress = null;
        }
    }

    public void waitMessage(boolean waited) {
        synchronized (this) {
            try {
                if (waited) {
                    wait();
                } else {
                    notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public MessageSend setSendMessage(int type, JSONObject message , int desIp) throws IOException {

            if (desIp == -1){
                desAddress = InetAddress.getByName(Packet.getInstance().getCurrentBroadcastIpName());
            }else {
                desAddress = InetAddress.getByName(Packet.intToIp(desIp));
            }

            outputStream = Packet.getInstance().getSendOnlineBroadCast();
            byte[] sendJson = message.toString().getBytes();
            outputStream.write((sendJson.length >> 8) & 0xff);
            outputStream.write(sendJson.length & 0xff);
            outputStream.write(sendJson);

        return this;
    }



    public MessageSend sendMessage() throws IOException {

        if (outputStream == null){
            return this;
        }

        DatagramPacket datagramPacket = new DatagramPacket(outputStream.toByteArray(),outputStream.toByteArray().length,desAddress,datagramSocket.getPort());
        datagramSocket.send(datagramPacket);


        return this;
    }

    @Override
    public void run() {

        super.run();

        while (true) {


            try {
                sendMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isWaited) {
                waitMessage(true);
            }
        }

    }






}
