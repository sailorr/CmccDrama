package com.king.cmccdrama.Utils;

import android.content.Context;
import android.util.Log;

import com.king.cmccdrama.Const.Config;

import tv.icntv.ottlogin.loginSDK;

public class WeiLaiLogin {

    /*
    public static loginSDK loginSDKGetInstance() {
        return loginSDK.getInstance(); // 初始化登录SDK
    }
     */

    // 初始化login SDK并登录
    public static String sdkInit(Context context) {
        boolean result = loginSDK.getInstance().sdkInit(
                loginSDK.TYPE_COMMON, Config.WEILAI_CHANNELCODE,Config.WEILAI_APPKEY,Config.WEILAI_APPSECRET, context);
        Log.i(Config.PROJECT_TAG,"login:" + result);
        String loginRet = loginSDK.getInstance().deviceLogin();
        Log.i(Config.PROJECT_TAG,"认证返回:" + loginRet);
        Log.i(Config.PROJECT_TAG,loginSDK.getInstance().loginStatusToMsg(loginSDK.getInstance().getLoginStatus()));
        // 写认证LOG
        String logContent;
        if (loginRet.equals("1")) { // 成功
            logContent = "0," + WeiLaiLogin.getValueByKey("EXT_VERSION_TYPE") + "," +
                    WeiLaiLogin.getValueByKey("EXT_VERSION_CODE");
            WeiLaiLog.logUpload(10,logContent);
            return "1";
        } else {
            logContent = "0," + WeiLaiLogin.getValueByKey("EXT_VERSION_TYPE") + "," +
                    WeiLaiLogin.getValueByKey("EXT_VERSION_CODE");
            WeiLaiLog.logUpload(10,logContent);
            return "认证失败，错误码:" + loginRet + "\nmac地址:" + WeiLaiLogin.getMacAddress();
        }
    }

    // 获取设备ID
    public static String getDeviceID() {
        StringBuffer result = new StringBuffer();
        loginSDK.getInstance().getDeviceID(result);
        Log.i(Config.PROJECT_TAG, "device ID:" + result.toString());
        return result.toString();
    }

    // 取MAC地址
    public static String getMacAddress() {
        StringBuffer result = new StringBuffer();
        loginSDK.getInstance().getValueByKey("EXT_GET_LOGIN_MAC", result);
        Log.i(Config.PROJECT_TAG,"mac address:" + result.toString());
        return result.toString();
    }

    // 取服务器地址
    public static String getServerAddress(String type) {
        // int getServerAddress(String type, StringBuffer serverAddress); // 获取服务器地址
        // type:“AD”:广告服务器
        // “USER_LOG”:日志服务器
        // “CDN_DISPATCH”:CDN 调度
        // “DEVICE_UPDATE”:升级服务器
        // “APP_UPDA TE”:应用升级服务器
        // “ROM_UPDATE”:ROM 升级服务器
        // “PLAY_CHECK”:播放鉴权服务器
        // "UC_CONTENT_VERIFY" : 播放器审核鉴权

        StringBuffer serverAddress = new StringBuffer();
        int temp = loginSDK.getInstance().getServerAddress(type, serverAddress);
        Log.i(Config.PROJECT_TAG,"取播放器审核鉴权服务器地址结果:" + temp);
        Log.i(Config.PROJECT_TAG,"address:" + serverAddress.toString());
        return serverAddress.toString();
    }

    public static String getValueByKey(String key) {
        StringBuffer buffer = new StringBuffer();
        int temp = loginSDK.getInstance().getValueByKey(key, buffer);
        Log.i(Config.PROJECT_TAG,"login sdk getValueByKey result:" + temp);
        return buffer.toString();
    }
}
