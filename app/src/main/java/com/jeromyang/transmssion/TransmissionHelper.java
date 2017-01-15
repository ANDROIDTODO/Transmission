package com.jeromyang.transmssion;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class TransmissionHelper {


    static DatagramSocket datagramSocket ;


    public static DatagramSocket getDatagramSocket(){

        if (datagramSocket==null){
            try {
                datagramSocket  = new DatagramSocket(T.UDP_PORT);
                datagramSocket.setBroadcast(true);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        return datagramSocket;

    }

}
