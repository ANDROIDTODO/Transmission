package com.jeromyang.transmssion;

import com.jeromyang.transmssion.model.MessageModel;

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

    private DatagramSocket sendDatagramSocket;

    private InetAddress desAddress;

    public MessageSend() {
        setDaemon(true);
        sendDatagramSocket = TransmissionHelper.getDatagramSocket(T.MESSAGE_UDP_PORT);

    }

    public void setWaited(boolean waited) {
        this.isWaited = waited;
        if (!isWaited) {
            waitMessage(false);
        } else {
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


    public MessageSend setSendMessage(MessageModel message) throws IOException {

        if (message.getDestinationIp() == -1) {
            desAddress = InetAddress.getByName(Packet.getInstance().getCurrentBroadcastIpName());
        } else {
            desAddress = InetAddress.getByName(Packet.intToIp(message.getDestinationIp()));
        }

        outputStream = Packet.getInstance().getSendOutputStream(Packet.DATA_TYPE_MESSAGE, message.getDestinationIp());
        byte[] sendJson = message.getMessageJson().toString().getBytes();
        outputStream.write((sendJson.length >> 8) & 0xff);
        outputStream.write(sendJson.length & 0xff);
        outputStream.write(sendJson);

        return this;
    }


    private boolean sendMessage() throws IOException {

        if (outputStream == null) {
            return false;
        }

        DatagramPacket datagramPacket = new DatagramPacket(outputStream.toByteArray(), outputStream.toByteArray().length, desAddress, T.MESSAGE_UDP_PORT);
        sendDatagramSocket.send(datagramPacket);

        return true;
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
