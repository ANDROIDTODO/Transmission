package com.jeromyang.transmssion;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jeromeyang on 2017/1/13.
 */

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        TransmissionHelper.init();
    }
}
