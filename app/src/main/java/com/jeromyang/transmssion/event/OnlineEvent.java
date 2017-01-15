package com.jeromyang.transmssion.event;

/**
 * Created by Jeromeyang on 2017/1/15.
 */

public class OnlineEvent {

    // 0 timeout 1 update

    private int sourceIp;

    private int type ;

    private int size;

   public OnlineEvent(int type,int sourceIp){
       this.type = type;
        this.sourceIp = sourceIp;
    }
    public OnlineEvent(int type,int size,int sourceIp){
        this.type = type;
        this.size = size;
    }
    public int getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(int sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
