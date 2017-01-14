package com.jeromyang.transmssion;

import com.jeromyang.transmssion.model.DataResult;
import com.jeromyang.transmssion.model.Model;
import com.jeromyang.transmssion.model.OnlineModel;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jeromeyang on 2017/1/14.
 */

public class UnPacket {


    private UnPacket(){}

    public static UnPacket getInstance(){

        return INSTANCE.instance;
    }


    private static class INSTANCE{
        static UnPacket instance = new UnPacket();
    }



    public int isValidData(byte[] packet){

        int offset = 0;
        if (packet.length >= offset + 4 && bytesToIntReverse(packet,offset) == 0xDEADBEDF){
            offset += 4;
            if (packet.length >= offset + 1 && packet[offset] == 0x01){
                offset += 1;
                return offset;
            }
        }

        return 0;
    }


    public DataResult getData(byte[] packet){
        DataResult dataResult = new DataResult();
        int offset = isValidData(packet);
        if (offset!=0){
            if (packet.length >= offset + 1 ){

                if ((packet[offset]&0xff)  == Packet.DATA_TYPE_ONLINE){ //在线通知
                    OnlineModel model = new OnlineModel();
                    offset += 1;

                    if (packet.length >= offset + 4){

                        model.setSourceIp(bytesToInt(packet,offset));
                        offset += 4;
                        if (packet.length >= offset +4){

                            model.setDestinationIp(bytesToInt(packet,offset));
                            offset += 4;
                            if (packet.length >= offset + 2){

                                int dataLength = (packet[offset]<<8)|(packet[offset+1]);
                                offset += 2;
                                try {
                                    String data = new String(packet,offset,dataLength,"UTF-8");
                                    model.setData(data);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    if (model.getSourceIp() != 0 && model.getSourceIp() != Packet.getInstance().getCurrentHostIp()){
                        dataResult.setResult(true);
                    }
                    dataResult.setT(model);
                    dataResult.setType(Packet.DATA_TYPE_ONLINE);
                }
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
        return (bytes[offset ] & 0xFF) << 24
                | (bytes[offset + 1] & 0xFF) << 16
                | (bytes[offset + 2] & 0xFF) << 8
                | (bytes[offset +3] & 0xFF);
    }


}
