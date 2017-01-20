package com.jeromyang.transmssion;

import com.jeromyang.transmssion.model.DataResult;
import com.jeromyang.transmssion.model.MessageModel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


/**
 * Created by Jeromeyang on 2017/1/16.
 * 接受消息信息
 */

public class MessageReceiver extends Thread {


    private boolean receive = true;

    private DatagramSocket receiveDatagramSocket;

    public MessageReceiver() {
        receiveDatagramSocket = TransmissionHelper.getDatagramSocket(T.MESSAGE_UDP_PORT);
    }

    @Override
    public void run() {
        super.run();
        while (receive) {
            try {
                receiveMessage();
            } catch (Exception e) {
                TLog.e("receive 1 error : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void receiveMessage() throws IOException {
        byte[] data = new byte[2048];
        while (receive) {
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length);
            receiveDatagramSocket.receive(datagramPacket);

            DataResult data1 = UnPacket.getInstance().getData(datagramPacket.getData());
            if (data1 != null && data1.isResult() && data1.getT().getDataType()==Packet.DATA_TYPE_MESSAGE) {
                MessageHandler.getInstance().handleMessage((MessageModel) data1.getT());
            }
        }
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }
}
