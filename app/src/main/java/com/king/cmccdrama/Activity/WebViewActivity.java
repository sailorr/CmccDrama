package com.king.cmccdrama.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Const.MySharedPre;
import com.king.cmccdrama.R;
import com.king.cmccdrama.Utils.Utils;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.widget.EditText;
import android.webkit.ConsoleMessage;

public class WebViewActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);
        webView = this.findViewById(R.id.main_webView);
        //
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("webUrl");
        Log.i(Config.PROJECT_TAG,url);
        /*
        try {
            viewLoadUrl(url);
        } catch (Exception ex) {
            Log.e(Config.PROJECT_TAG, ex.getMessage());
        }
         */
        viewLoadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void viewLoadUrl(String url) {
        WebSettings webSettings = webView.getSettings();
        //支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 缓存设置
        //webSettings.setAppCacheEnabled(true);
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 优先使用缓存
        //设置  Application Caches 缓存目录
        //String cacheDirPath = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();// 缓存目录
        //webSettings.setAppCachePath(cacheDirPath);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        webView.requestFocus();
        webView.loadUrl(url);
        webView.setWebViewClient(mWebViewClient);//设置WebViewClient
        webView.setWebChromeClient(mWebChromeClient);// 创建WebViewChromeClient
    }

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //view.loadUrl(url);
            if (url.contains("app/jump.php")) {
                startActivity(VideoPlayerActivity.class,
                        setNameBundle("videoID", MySharedPre.getCurrVideoID(WebViewActivity.this)));
                finish();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //网页加载开始
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Utils.sendKey20();
        }
    };

    WebChromeClient mWebChromeClient = new WebChromeClient() {

        public void onProgressChanged(WebView view, int newProgress) {
            //mProgressBar.setVisibility(View.VISIBLE);
            //mProgressBar.setProgress(newProgress * 100);

            //if (newProgress == 100) mProgressBar.setVisibility(View.GONE);
        }

        // 处理javascript的alert
        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
            // 构建一个Builder来显示网页中的alert对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle("提示");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        // 处理javascript的confirm
        public boolean onJsConfirm(WebView view, String url, String message, final android.webkit.JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle("提示");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        /*
        // 输入框
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                  final android.webkit.JsPromptResult result) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(message);
            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et);
            builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm(et.getText().toString());
                }
            }).setNeutralButton(android.R.string.cancel, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }
         */

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.i("console", "["+consoleMessage.messageLevel()+"] "+ consoleMessage.message() + "(" +consoleMessage.sourceId()  + ":" + consoleMessage.lineNumber()+")");
            return super.onConsoleMessage(consoleMessage);
        }
    };

    /*
    protected Bundle setNameBundle(String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
     */
}
