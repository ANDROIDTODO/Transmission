package com.jeromyang.transmssion;

import android.util.SparseArray;

import com.jeromyang.transmssion.model.UdpPort;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class TransmissionHelper {
    private final static SparseArray<DatagramSocket> datagramSocketArray=new SparseArray();

    private static DatagramSocket datagramSocket ;
    private static List<UdpPort> udpPorts = new ArrayList<>();



     static void init(){
//        int port = T.UDP_PORT;
//        for (int i = 0 ; i < 10 ; i ++){
//            UdpPort udpPort = new UdpPort(port);
//            port += 10;
//            udpPorts.add(udpPort);
//        }
    }

    public static DatagramSocket getDatagramSocket(int udpPort){
        DatagramSocket datagramSocket=datagramSocketArray.get(udpPort);
        if(datagramSocket==null){
            try {
                datagramSocket  = new DatagramSocket(udpPort);
                datagramSocket.setBroadcast(true);
                datagramSocketArray.put(udpPort,datagramSocket);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return datagramSocket;
    }

}
