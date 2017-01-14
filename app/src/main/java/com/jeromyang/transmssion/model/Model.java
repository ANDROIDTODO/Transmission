package com.jeromyang.transmssion.model;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class Model {

    private int sourceIp ;
    private int destinationIp ;
    private String dev_name ;
    private String name;
    private String data;


    public int getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(int sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(int destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Model{" +
                "sourceIp=" + sourceIp +
                ", destinationIp=" + destinationIp +
                ", dev_name='" + dev_name + '\'' +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
