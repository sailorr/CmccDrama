package com.king.cmccdrama.Const;

import android.content.Context;
import android.content.SharedPreferences;

import com.king.cmccdrama.Utils.Utils;


public class MySharedPre {


    public static Boolean setCurrVideoID(Context context,String value) {
        return Utils.setSharedString(context,Config.PRO_SHARED_NAME,"videoID",value);
    }

    public static String getCurrVideoID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.PRO_SHARED_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString("videoID", "");
    }

    // 取当前签到日期
    public static Boolean setSignData(Context context,String value) {
        return Utils.setSharedString(context,Config.PRO_SHARED_NAME,"signData",value);
    }

    // 存当前签到日期
    public static String getSignData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.PRO_SHARED_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString("signData", "");
    }

    // 返回历史还是返回播放列表
    public static Boolean setBackUrl(Context context,String value) {
        return Utils.setSharedString(context,Config.PRO_SHARED_NAME,"backUrl",value);
    }

    // 存当前签到日期
    public static String getBackUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.PRO_SHARED_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString("backUrl", "");
    }
}
