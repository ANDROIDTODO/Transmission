package com.jeromyang.transmssion.model;

import com.jeromyang.transmssion.UnPacket;

/**
 * Created by Jeromeyang on 2017/1/16.
 */

public class UdpPort {
    public boolean isUesd;
    public int PORT;

    public UdpPort(int port){
        this.PORT = port;
    }

    public boolean isUesd() {
        return isUesd;
    }

    public void setUesd(boolean uesd) {
        isUesd = uesd;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }
}
