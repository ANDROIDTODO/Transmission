package com.jeromyang.transmssion;

import android.util.Log;

import com.jeromyang.transmssion.model.DataResult;
import com.jeromyang.transmssion.model.Model;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class UnPacket {


    private UnPacket() {
    }

    public static UnPacket getInstance() {

        return INSTANCE.instance;
    }


    private static class INSTANCE {
        static UnPacket instance = new UnPacket();
    }


    public int isValidData(byte[] packet) {

        int offset = 0;
        if (packet.length >= offset + 4 && bytesToIntReverse(packet, offset) == 0xDEADBEDF) {
            offset += 4;
            if (packet.length >= offset + 1 && packet[offset] == 0x01) {
                offset += 1;
                return offset;
            }
        }

        return 0;
    }

    /**
     * 数据转换为model
     **/
    private Model dataToModel(byte[] packet, int offset) {
        if (offset != 0 && packet.length >= offset + 1) {
            int dataType = (packet[offset] & 0xff);
            Model model = ModelFactory.getInstance().createModel(dataType);
            offset += 1;
            if (packet.length >= offset + 4) { //ip处理
                model.setSourceIp(bytesToInt(packet, offset));
                offset += 4;
                if (packet.length >= offset + 4) {
                    model.setDestinationIp(bytesToInt(packet, offset));
                    offset += 4;
                    model.handleData(packet, offset);
                }
            }
            if (dataType != Packet.DATA_TYPE_ONLINE) {
                Log.e("UnPacket", "tag2--dataType=" + dataType + " content=" + model.getData());
            }
            return model;
        }
        return null;
    }

    /**
     * 返回dataResult
     **/
    public DataResult getData(byte[] packet) {
        DataResult dataResult = new DataResult();
        int offset = isValidData(packet);
        if (offset != 0) {
            Model model = dataToModel(packet, offset);
            if (model != null) {
                dataResult.setResult(model.dataResult());
                dataResult.setT(model);
                dataResult.setType(model.getDataType());
            }
        }
        return dataResult;
    }


    private static int bytesToInt(byte[] bytes, int offset) {
        return (bytes[offset + 3] & 0xFF) << 24
                | (bytes[offset + 2] & 0xFF) << 16
                | (bytes[offset + 1] & 0xFF) << 8
                | (bytes[offset] & 0xFF);
    }


    private static int bytesToIntReverse(byte[] bytes, int offset) {
        return (bytes[offset] & 0xFF) << 24
                | (bytes[offset + 1] & 0xFF) << 16
                | (bytes[offset + 2] & 0xFF) << 8
                | (bytes[offset + 3] & 0xFF);
    }


}
