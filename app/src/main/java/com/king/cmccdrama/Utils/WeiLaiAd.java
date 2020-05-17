package com.king.cmccdrama.Utils;

import android.content.Context;
import android.util.Log;

import com.king.cmccdrama.Const.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tv.icntv.adsdk.*;
import tv.icntv.ottlogin.loginSDK;

public class WeiLaiAd {

    /*
    public static AdSDK initAdSDK() {
        AdSDK sdk = AdSDK.getInstance();
    }
     */
    private static AdSDK adInit(Context context) {
        AdSDK ad = tv.icntv.adsdk.AdSDK.getInstance();
        //
        String adAddress = WeiLaiLogin.getServerAddress("AD");
        Log.i(Config.PROJECT_TAG,"广告服务器地址：" + adAddress);
        //
        boolean result;
        if (!Utils.isBlank(adAddress)) { // 能获取到广告服务器地址
            result = ad.init(
                    adAddress, WeiLaiLogin.getDeviceID(), Config.WEILAI_APPKEY, Config.WEILAI_CHANNELCODE, null,context);
            Log.i(Config.PROJECT_TAG, "初始化log sdk结果：" + result);
        } else {
            result = ad.init(
                    WeiLaiLogin.getDeviceID(), Config.WEILAI_APPKEY, Config.WEILAI_CHANNELCODE, null,context);
            Log.i(Config.PROJECT_TAG, "初始化log sdk结果：" + result);
        }
        return ad;
    }

    public static String getAD(Context context, String adType) {
        StringBuffer buffer = new StringBuffer();
        int result = adInit(context).getAD(adType,null,null, null,null,null,buffer);
        Log.i(Config.PROJECT_TAG,"获取广告列表结果:" + result);
        return buffer.toString();
    }

    public static boolean report(Context context, String mid, String aid, String mtid, String seriesID, String programID, String location, String extend) {
        return adInit(context).report(mid, aid, mtid, seriesID, programID, location, extend);
    }

    /**
     * 播开屏广告
     * @param context
     * @return
     */
    public static Map<String,String> playStartAd(Context context) {
        Map<String,String> result = new HashMap<>();
        String adList = WeiLaiAd.getAD(context, "open");
        Log.i(Config.PROJECT_TAG,"取开屏广告结果：" + adList);
        if (!Utils.isBlank(adList)) {
            try {
                JSONObject response = new JSONObject(adList);
                JSONObject adspaces = response.getJSONObject("adspaces");
                JSONArray open = adspaces.getJSONArray("open");
                JSONArray materials = null;
                String mid, aid;
                for (int i = 0; i < open.length(); i++) {
                    JSONObject openValue = open.getJSONObject(i);
                    materials = openValue.getJSONArray("materials");
                    mid = openValue.getString("mid");
                    aid = openValue.getString("aid");
                    result.put("mid",mid);
                    result.put("aid",aid);
                    Log.i(Config.PROJECT_TAG, "mid:::::" + mid);
                    Log.i(Config.PROJECT_TAG, "aid:::::" + aid);

                    //Log.i(Config.PROJECT_TAG, materials.toString());
                }
                for(int i =0; i < materials.length(); i++) {
                    JSONObject value = materials.getJSONObject(i);
                    String filePath = value.getString("file_path");
                    String playTime = value.getString("play_time");
                    String mtid = value.getString("id");
                    result.put("filePath",filePath);
                    result.put("playTime",playTime);
                    result.put("mtid",mtid);
                    Log.i(Config.PROJECT_TAG, "filePath:" + filePath);
                    Log.i(Config.PROJECT_TAG, "playTime:" + playTime);
                    Log.i(Config.PROJECT_TAG, "mtid:" + mtid);
                }
                return result;
            } catch (Exception ex) {
                Log.e(Config.PROJECT_TAG,"出错了:" + ex.getMessage());
            }
            return null;
        }
        return null;
    }
}
