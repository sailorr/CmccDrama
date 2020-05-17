package com.king.cmccdrama.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.king.cmccdrama.Activity.WelComeActivity;
import com.king.cmccdrama.BuildConfig;
import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Interface.APIManager;
import com.king.cmccdrama.Utils.WeiLaiLog;
import com.king.cmccdrama.Utils.WeiLaiLogin;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public enum SDKManager {
    INSTANCE;

    public boolean isLoginWeilai=false;

    public void initSDK(Context context){
        String result = WeiLaiLogin.sdkInit(context);
        WeiLaiLog.logUpload(88, "0," + BuildConfig.VERSION_CODE);
        Log.i(Config.PROJECT_TAG, "login SDK version:" + WeiLaiLogin.getValueByKey("EXT_VERSION_CODE"));
        if (!result.equals("1")) {
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
            isLoginWeilai = false;
        }
        isLoginWeilai= true;
    }

    public void insertUser(Context context){
        com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
                "10086", "", context);
        String mobile = sysinfo.getEpgAccountIdentity();

        APIManager.getSingleton().user(userHandler, mobile);

    }


    AsyncHttpResponseHandler userHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Log.i(Config.PROJECT_TAG, "添加新用户返回:" + responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        }
    };
}
