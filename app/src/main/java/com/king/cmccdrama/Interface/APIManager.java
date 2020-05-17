package com.king.cmccdrama.Interface;

import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Utils.CryptAES;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.king.cmccdrama.Utils.HttpClientHelper;

/**
 * 服务器接口
 */
public class APIManager {

    // adb connect 192.168.3.201:5555

    private static volatile APIManager singleton = null;

    private APIManager(){}

    public static APIManager getSingleton() {
        if(singleton == null) {
            synchronized (APIManager.class) {
                if(singleton == null) {
                    singleton = new APIManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 设置Http头部，后继可用作验证加密
     */
    /*
    private void setHttpHeader() {
        String authorization = "ddddd";
        HttpClientHelper.addHeader("Authorization",authorization);
        HttpClientHelper.addHeader("user-agent",useragent);
    }
    */

    /**
     * 取首页VOD列表
     * http://192.168.3.230/app/interface.php?Action=getPortalVodList
     */
    public void getPortalVodList(AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        // 请求方法名加密
        String actionName = CryptAES.encrypt("getPortalVodList", Config.SECRET_KEY, Config.SECRET_IV);
        params.put("Action", actionName);
        HttpClientHelper.post("",params,responseHandler);
    }

    /**
     * 取VOD列表
     * @param responseHandler
     * @param currPage 第几页，0为第一页
     * @param pageCount 每页多少条数据
     */
    public void getVodList(AsyncHttpResponseHandler responseHandler, int currPage, int pageCount) {
        RequestParams params = new RequestParams();
        params.add("currPage",String.valueOf(currPage));
        params.add("pageCount",String.valueOf(pageCount));
        params.put("Action", "getVodList");
        HttpClientHelper.post("",params,responseHandler);
    }

    public void getCategoryVodList(AsyncHttpResponseHandler responseHandler,int categoryID, int currPage, int pageCount) {
        RequestParams params = new RequestParams();
        params.add("category_id",String.valueOf(categoryID));
        params.add("currPage",String.valueOf(currPage));
        params.add("pageCount",String.valueOf(pageCount));
        params.put("Action", "getCategoryVodList");
        HttpClientHelper.post("",params,responseHandler);
    }

    // http://39.108.124.116/app/interface.php?Action=getVodDetail&videoID=149
    public void getVodDetail(AsyncHttpResponseHandler responseHandler, int videoID) {
        RequestParams params = new RequestParams();
        params.add("videoID",String.valueOf(videoID));
        params.put("Action", "getVodDetail");
        HttpClientHelper.post("",params,responseHandler);
    }

    public void setPlayHistory(AsyncHttpResponseHandler responseHandler,int userID, int videoID) {
        RequestParams params = new RequestParams();
        params.add("userID",String.valueOf(userID));
        params.add("videoID",String.valueOf(videoID));
        params.put("Action", "setPlayHistory");
        HttpClientHelper.post("",params,responseHandler);
    }

    public void getPlayHistory(AsyncHttpResponseHandler responseHandler, int userID, int currPage, int pageCount) {
        RequestParams params = new RequestParams();
        params.add("currPage",String.valueOf(currPage));
        params.add("pageCount",String.valueOf(pageCount));
        params.add("userID",String.valueOf(userID));
        params.put("Action", "getPlayHistory");
        HttpClientHelper.post("",params,responseHandler);
    }

    // http://47.111.6.79/app/interface.php?Action=userSign&userID=1&signData=2019-10-01
    public void userSign(AsyncHttpResponseHandler responseHandler, String name, String currData) {
        RequestParams params = new RequestParams();
        params.add("name",name);
        params.add("signData",currData);
        params.put("Action", "userSign");
        HttpClientHelper.post("",params,responseHandler);
    }

    // http://47.111.6.79/app/interface.php?Action=userInfo&userID=1
    public void userInfo(AsyncHttpResponseHandler responseHandler, String name) {
        RequestParams params = new RequestParams();
        params.add("name",name);
        params.put("Action", "userInfo");
        HttpClientHelper.post("",params,responseHandler);
    }

    // http://39.108.124.116/app/interface.php?Action=dicInfo&key=contentID
    public void dicInfo(AsyncHttpResponseHandler responseHandler, String keyName) {
        RequestParams params = new RequestParams();
        params.add("dicInfo", keyName);
        params.put("Action", "userInfo");
        HttpClientHelper.post("",params,responseHandler);
    }

    // http://39.108.124.116/app/interface.php?Action=user&name=name
    public void user(AsyncHttpResponseHandler responseHandler, String name) {
        RequestParams params = new RequestParams();
        params.add("name", name);
        params.put("Action", "user");
        HttpClientHelper.post("",params,responseHandler);
    }

    public void notPay(AsyncHttpResponseHandler responseHandler, String macAddress) {
        RequestParams params = new RequestParams();
        params.add("macAddress", macAddress);
        params.put("Action", "notPay");
        HttpClientHelper.post("",params,responseHandler);
    }
}
