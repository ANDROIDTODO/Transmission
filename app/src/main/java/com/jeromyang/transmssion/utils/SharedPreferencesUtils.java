package com.jeromyang.transmssion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jeromyang.transmssion.App;

/**
 * Created by Jeromeyang on 2017/1/13.
 */

public class SharedPreferencesUtils {

    public static void setName(String name){
        SharedPreferences sharedPreferences = App.context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",name);
        editor.apply();
    }

    public static String getName(){
        SharedPreferences sharedPreferences = App.context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getString("name",android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL);
    }
}
