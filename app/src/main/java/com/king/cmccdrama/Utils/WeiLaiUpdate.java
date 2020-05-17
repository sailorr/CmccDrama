package com.king.cmccdrama.Utils;

import android.content.Context;
import android.util.Log;

import com.king.cmccdrama.BuildConfig;
import com.king.cmccdrama.Const.Config;

import tv.icntv.ottlogin.loginSDK;
import tv.newtv.upgradesdk.upgradeSDK;

// 未来升级SDK
public class WeiLaiUpdate {

    public static boolean init () {
        boolean result = upgradeSDK.getInstance().init(0); // 0：有线；1:无线
        return result;
    }

    public static String getAppUpgradeInfo() {
        String address = WeiLaiLogin.getServerAddress("APP_UPDATE"); // APP 升级服务器地址
        //
        StringBuffer buffer = new StringBuffer();
        int result = upgradeSDK.getInstance().J_getAppUpgradeInfo(
                address,Config.WEILAI_APPKEY,Config.WEILAI_CHANNELCODE, String.valueOf(BuildConfig.VERSION_CODE),buffer);
        Log.i(Config.PROJECT_TAG,"取app升级信息结果:" + result);
        Log.i(Config.PROJECT_TAG,"app升级信息:" + buffer.toString());
        return buffer.toString();
    }
}
