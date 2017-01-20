package com.jeromyang.transmssion.model;

/**
 * Created by wangxi on 2017/1/19.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/1/19
 * update time : 2017/1/19
 * last modify : wangxi
 */

public class SendInfo {
    private String fileName;
    private String suffix;
    private int maxUsePort;
    private int fileSize;
    private int port;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getMaxUsePort() {
        return maxUsePort;
    }

    public void setMaxUsePort(int maxUsePort) {
        this.maxUsePort = maxUsePort;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getJsonString() {
        return "SendInfo =";
    }
}
