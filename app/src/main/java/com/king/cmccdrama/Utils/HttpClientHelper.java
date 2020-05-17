package com.king.cmccdrama.Utils;

import com.king.cmccdrama.Const.Config;
import com.loopj.android.http.*;

public class HttpClientHelper {

    private static final String BASE_URL = Config.API_BASE_URL; // 接口服务器地址
    private static final int timeOut = 10;

    private static AsyncHttpClient client =
            new AsyncHttpClient(true, 80, 443);
    //client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());

    /*
    // 添加HTTP头
    public static void addHeader(String header, String value) {
        client.addHeader(header, value);
    }
    */

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(timeOut * 1000);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(timeOut * 1000);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}


/*

AsyncHttpClient client = new AsyncHttpClient();

client.get("www.baidu.com", new AsyncHttpResponseHandler() {

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    public void onRetry(int retryNo) {
        super.onRetry(retryNo);
    }

    @Override
    public void onCancel() {
        super.onCancel();
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);
    }
});
 */