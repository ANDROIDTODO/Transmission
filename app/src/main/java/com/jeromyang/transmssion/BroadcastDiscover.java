package com.jeromyang.transmssion;

import com.jeromyang.transmssion.model.DataResult;
import com.jeromyang.transmssion.model.Model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Jeromeyang on 2017/1/13.
 */

public class BroadcastDiscover extends Thread {


    private Listener listener;

    private boolean receive = true;

    public void setReceive(boolean receive){
        this.receive = receive;
    }


    @Override
    public void run() {
        super.run();

        try {
            receiveBroadcast(TransmissionHelper.getDatagramSocket());
        } catch (IOException e) {
            TLog.e("receive 1 error : " + e.getMessage());

            e.printStackTrace();
        }


    }



    BroadcastDiscover(Listener listener){

        this.listener = listener;

    }



    private void receiveBroadcast(DatagramSocket datagramSocket) throws IOException {
        byte[] data = new byte[2048];
        while (receive){
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length);


        datagramSocket.receive(datagramPacket);

            DataResult data1 = UnPacket.getInstance().getData(datagramPacket.getData());
            if (data1!=null && data1.isResult()){
//                data1.getType()
//                TLog.e(data1.toString());
                if (listener!=null){
                    listener.receiver(data1);
                }
            }

//        String s =  new String(datagramPacket.getData());
////        TLog.e("address : " + datagramPacket.getAddress() + ", port : " + datagramPacket.getPort() + ", content : " + s);
//            if (listener!=null){
//                listener.receiver(s);
//            }
    }
    }

     interface Listener{
        void receiver(DataResult dataResult);
    }
}
