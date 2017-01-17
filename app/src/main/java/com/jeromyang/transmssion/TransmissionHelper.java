package com.jeromyang.transmssion;

import com.jeromyang.transmssion.model.UdpPort;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class TransmissionHelper {


    private static DatagramSocket datagramSocket ;
    private static List<UdpPort> udpPorts = new ArrayList<>();



     static void init(){
        int port = T.UDP_PORT;
        for (int i = 0 ; i < 10 ; i ++){
            UdpPort udpPort = new UdpPort(port);
            port += 10;
            udpPorts.add(udpPort);
        }
    }


     static DatagramSocket getDatagramSocket(){

        if (datagramSocket==null){
            try {
                UdpPort udpPort = getUsefulUdpPort();
                datagramSocket  = new DatagramSocket(udpPort.getPORT());
                udpPort.setUesd(true);
                datagramSocket.setBroadcast(true);
            } catch (SocketException e) {
                e.printStackTrace();
                getDatagramSocket();
            }
        }

        return datagramSocket;

    }


    private static UdpPort getUsefulUdpPort(){
        for (UdpPort udpPort :udpPorts){
            if (!udpPort.isUesd){
                return udpPort;
            }
        }
        return new UdpPort(31323);
    }

}
