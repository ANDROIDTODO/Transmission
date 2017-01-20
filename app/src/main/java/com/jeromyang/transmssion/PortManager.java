package com.jeromyang.transmssion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxi on 2017/1/18.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/1/18
 * update time : 2017/1/18
 * last modify : wangxi
 */

public class PortManager {

    public static final int UDP_START_PORT = 18000;
    private final List<Integer> inUsePortList = new ArrayList();

    public int getMaxPort() {
        if (inUsePortList.isEmpty()) {
            return UDP_START_PORT;
        } else {
            return inUsePortList.get(inUsePortList.size() - 1);
        }
    }

    public int getSparePort(int maxUsePort) {
        int port = maxUsePort + 10;
        inUsePortList.add(port);
        return port;
    }
}
