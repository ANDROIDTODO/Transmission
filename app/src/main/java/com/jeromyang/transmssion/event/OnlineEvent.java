package com.jeromyang.transmssion.event;

/**
 * Created by Jeromeyang on 2017/1/15.
 */

public class OnlineEvent {

    private int sourceIp;

   public OnlineEvent(int sourceIp){
        this.sourceIp = sourceIp;
    }

    public int getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(int sourceIp) {
        this.sourceIp = sourceIp;
    }
}
