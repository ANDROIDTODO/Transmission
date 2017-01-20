package com.jeromyang.transmssion;

import com.jeromyang.transmssion.model.MessageModel;
import com.jeromyang.transmssion.model.Model;
import com.jeromyang.transmssion.model.OnlineModel;

/**
 * Created by wangxi on 2017/1/19.
 * <p>
 * description :
 * version code: 1
 * create time : 2017/1/19
 * update time : 2017/1/19
 * last modify : wangxi
 */

public class ModelFactory {
    public static ModelFactory getInstance() {
        return ModelFactory.MessageFactoryHolder.sInstance;
    }

    private final static class MessageFactoryHolder {
        public static final ModelFactory sInstance = new ModelFactory();
    }

    private ModelFactory() {

    }

    public Model createModel(int dataType) {
        Model model;
        if(dataType==Packet.DATA_TYPE_ONLINE){
            model=new OnlineModel();
        }else if(dataType==Packet.DATA_TYPE_MESSAGE){
            model=new MessageModel();
        }else {
            model=new Model();
        }
        model.setDataType(dataType);
        return model;
    }

}
