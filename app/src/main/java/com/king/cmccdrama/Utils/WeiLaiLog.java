package com.king.cmccdrama.Utils;

import android.util.Log;

import com.king.cmccdrama.Const.Config;

import tv.icntv.logsdk.logSDK;
import tv.icntv.ottlogin.loginSDK;

public class WeiLaiLog {

    private static logSDK logInit() {
        logSDK log = logSDK.getInstance();
        String logAddress = WeiLaiLogin.getServerAddress("USER_LOG");
        boolean result = log.sdkInit(
                logAddress, "", WeiLaiLogin.getDeviceID(), Config.WEILAI_CHANNELCODE, Config.WEILAI_APPKEY);
        Log.i(Config.PROJECT_TAG, "初始化log sdk结果：" + result);
        return log;
    }

    public static int logUpload(int type, String content) {
        int result = WeiLaiLog.logInit().logUpload(type,content);
        Log.i(Config.PROJECT_TAG,"上传日志结果：" + result);
        return result;
    }
}
