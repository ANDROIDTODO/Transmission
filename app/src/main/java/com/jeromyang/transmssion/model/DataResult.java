package com.jeromyang.transmssion.model;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class DataResult {

    private boolean result;
    private int type;
    private Model t;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Model getT() {
        return t;
    }

    public void setT(Model t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "DataResult{" +
                "result=" + result +
                ", type=" + type +
                ", t=" + t +
                '}';
    }
}
