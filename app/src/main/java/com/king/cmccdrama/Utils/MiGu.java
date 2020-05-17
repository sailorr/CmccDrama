package com.king.cmccdrama.Utils;

import android.accessibilityservice.GestureDescription;
import android.util.Log;

import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Const.MySharedPre;

import java.io.IOException;
import java.io.InputStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MiGu {

    private Context context;
    private String account;
    private String stbId;
    private String contentId;
    private String copyRightID;
    private String token;
    //
    private Handler handler;

    public MiGu(Context context, Handler handler,String account, String stbId, String contentId,
                String copyRightID,String token) {
        this.context = context;
        this.handler = handler;
        this.account = account;
        this.stbId = stbId;
        this.contentId = contentId;
        this.copyRightID = copyRightID;
        this.token = token;
    }

    public void loginMiGu() {
        authorize();
        /*
        final String message = String.format(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<message module=\"SCSP\" version=\"1.1\">" +
                "<header action=\"REQUEST\" command=\"LOGINAUTH\"/>" +
                "<body>" +
                "<loginAuth loginType='5' account='%s' password='%s' stbId='%s'/>" +
                "</body>" +
                "</message>\n",account,password,stbId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = httpPostXmlRaw(Config.MIGU_LOGIN_URL, message);
                Log.i(Config.PROJECT_TAG, "咪咕登录接口请求,结果:" + result);
                // 解析XML
                if (result != null) {
                    InputStream stream = new ByteArrayInputStream(result.getBytes());
                    XmlUtils xmlUtils = new XmlUtils();
                    Document root = xmlUtils.parse(stream);
                    xmlUtils.getAllProperty(root.getRootElement());
                    HashMap<String, String> map = xmlUtils.GetPropertyMapMap(); // 取所有属性
                    token = map.get("token");
                    copyrighteId = map.get("copyrighteId");
                    //MySharedPre.setCurrMiguToken(context, token); // 写token
                    Log.i(Config.PROJECT_TAG, "咪咕登录接口请求token:" + token);
                    Log.i(Config.PROJECT_TAG, "咪咕登录接口请求copyrighteId:" + copyrighteId);
                    // 调支付
                    //
                    authorize();
                } else {
                    //respFailed();
                }
            }
        }).start();
        */
    }

    /* 0：鉴权成功,,1：需要订购,,10: 用户未登录,,11：请求参数异常,,12：黑名单用户，鉴权失败,,
     * 20：需要订购（账户的计费标识需要验证）
     * 30：您的帐户不能消费,,31：对应牌照方没可计费的产品,,50: 试看鉴权通过,,5000：系统无响应
     */
    private void authorize() {
        final String message = String.format(
                " {\"userId\":\"%s\",\"terminalId\":\"%s\",\"copyRightId\":\"%s\",\"systemId\":\"0\"," +
                "\"contentId\":\"%s\",\"spId\":\"%s\",\"consumeLocal\":\"%s\",\"consumeScene\":\"%s\"," +
                "\"consumeBehaviour\":\"%s\",\"token\":\"%s\",\"clientCallBack\":\"%s\"}",
                account,stbId,copyRightID,contentId,Config.MIGU_SPID,Config.MIGU_CONSUME_LOCAL,
                Config.MIGU_CONSUME_SCENE,Config.MIGU_CONSUME_BEHAVIOUR,token, Config.MIGU_URL_CALLBACK);
        Log.i(Config.PROJECT_TAG, "咪咕鉴权接口请求数据:" + message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseString = httpPostJsonRaw(Config.MIGU_AUTHORIZE_URL, message);
                Log.i(Config.PROJECT_TAG, "咪咕鉴权接口请求,结果:" + responseString);
                //
                try {
                    JSONObject response = new JSONObject(responseString);

                    //JSONObject json = new JSONObject(response.getString("Result"));
                    String result = response.getString("result");
                    String resultDesc = response.getString("resultDesc");
                    String webUrl = response.getString("webUrl");

                    Log.i(Config.PROJECT_TAG, resultDesc);
                    Log.i(Config.PROJECT_TAG, webUrl);
                    //
                    Bundle data = new Bundle();
                    Message msg = new Message();
                    data.putString("value", result);
                    msg.obj = webUrl;
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch (Exception ex) {
                    Log.e(Config.PROJECT_TAG, ex.getMessage());
                }
            }
        }).start();
    }

    /*
    private void respFailed() {
        Bundle data = new Bundle();
        Message msg = new Message();
        data.putString("value", "0"); // 0: 失败
        msg.setData(data);
        handler.sendMessage(msg);
    }
     */

    private String httpPostXmlRaw(String url, String message) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        StringEntity postingString;
        try {
            postingString = new StringEntity(message);
            post.setEntity(postingString);
            post.setHeader("Content-type", "text/xml");
            HttpResponse response = httpClient.execute(post);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            Log.e(Config.PROJECT_TAG, e.getMessage());
            return null;
        }
    }

    private String httpPostJsonRaw(String url, String message) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        StringEntity postingString;
        try {
            postingString = new StringEntity(message);
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            Log.e(Config.PROJECT_TAG, e.getMessage());
            return null;
        }
    }
}
