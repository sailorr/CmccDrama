package com.king.cmccdrama.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Const.MySharedPre;
import com.king.cmccdrama.Interface.APIManager;
import com.king.cmccdrama.R;
import com.king.cmccdrama.Utils.ImageLoadOptions;
import com.king.cmccdrama.Utils.MiGu;
import com.king.cmccdrama.Utils.Utils;
import com.king.cmccdrama.Utils.WeiLaiLogin;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class VodDetailActivity extends BaseActivity {

    private ImageView btnDtailPlay, btnDetailChoose, btnDetailAbout, btnDetailMore;

    private TextView vodTitle, vodActor, vodDesc, vodType;
    private ImageView vodDetailPoster; // 海报
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //
    private int videoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate:VodDetailActivity---> ");
        setContentView(R.layout.activity_vod_detail);
        //
        initView();
        initData();
        setClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //
        //migu.loginMiGu();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        btnDtailPlay = findViewById(R.id.btn_detail_play);
        //btnDtailPlay.setVisibility(View.GONE);
        //btnDetailChoose = findViewById(R.id.btn_detail_choose);
        //btnDetailAbout = findViewById(R.id.btn_detail_about);
        //btnDetailMore = findViewById(R.id.btn_detail_more);
        //
        vodTitle = findViewById(R.id.vod_title);
        vodActor = findViewById(R.id.vod_actor);
        vodDesc = findViewById(R.id.vod_desc);
        vodType = findViewById(R.id.vod_type);
        vodDetailPoster = findViewById(R.id.vod_detail_poster);
    }

    // 是否从外部跳转过来
    private String param="";

    private void initData() {
        Intent intent = this.getIntent();
        videoID = Integer.valueOf(intent.getStringExtra("videoID"));
        param = intent.getStringExtra("param");
        APIManager.getSingleton().getVodDetail(respHandler, videoID);
    }


    private void setClick() {
        btnDtailPlay.setOnClickListener(this); // 播放按钮
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_detail_play:
                //startActivity(VideoPlayerActivity.class,
                //        setNameBundle("videoID",String.valueOf(videoID)));
                //finish();
                String macAddress = WeiLaiLogin.getMacAddress();
                APIManager.getSingleton().notPay(notPayHandler, macAddress);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(Config.PROJECT_TAG, "keycode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String backUrl = MySharedPre.getBackUrl(this);
            Log.d("Test", "onKeyDown: backUrl-->"+backUrl);
            Log.d("Test", "onBackPressed: "+"isFromOut:-----> "+ param);
            if (param.equals("1")) {
                startActivity(new Intent(VodDetailActivity.this, VodCategoryActivity.class));
            }
            if (backUrl.equals("history")) {
                startActivity(new Intent(VodDetailActivity.this, PlayHistoryActivity.class));
            } else if (backUrl.equals("category")) {
                startActivity(new Intent(VodDetailActivity.this, VodCategoryActivity.class));
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    AsyncHttpResponseHandler respHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Log.i(Config.PROJECT_TAG, responseString);
                JSONObject response = new JSONObject(responseString);
                int result = response.getInt("Result");
                if (result == Config.API_RESULT_OK) {
                    JSONArray data = response.getJSONArray("Data");
                    int count = data.length();
                    String video_poster_img;
                    String vodTypeName = null;
                    for (int i = 0; i < count; i++) {
                        JSONObject value = data.getJSONObject(i);
                        vodTitle.setText(value.getString("video_name"));
                        vodActor.setText("演员：" + value.getString("video_actor"));
                        vodDesc.setText("简介：" + value.getString("video_desc"));
                        video_poster_img = value.getString("video_poster_img");
                        imageLoader.displayImage(
                                video_poster_img, vodDetailPoster, ImageLoadOptions.getDisplayImageOptions()); // 加载网络图片
                        int categoryID = value.getInt("category_id");
                        switch (categoryID) {
                            case 6:
                                vodTypeName = "京剧";
                                break;
                            case 7:
                                vodTypeName = "晋剧";
                                break;
                            case 8:
                                vodTypeName = "越调";
                                break;
                            case 9:
                                vodTypeName = "粤剧";
                                break;
                            case 10:
                                vodTypeName = "湘剧";
                                break;
                            case 11:
                                vodTypeName = "相声";
                                break;
                        }
                        vodType.setText("简介：" + vodTypeName);
                    }
                }
            } catch (org.json.JSONException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        }
    };

    boolean notPay = false;
    AsyncHttpResponseHandler notPayHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                JSONObject response = new JSONObject(responseString);
                Log.d("Test", "onSuccess: 是否需要支付--"+response);
                int result = response.getInt("Result");
                if (result == Config.API_RESULT_OK) {
                    notPay = true;
                }
                if (notPay) { // 不需要支付,白名单 // TODO,最后这里要去掉
                    startActivity(VodPlayer2Activity.class, setNameBundle("videoID", String.valueOf(videoID)));
                    finish();
                } else {
                    migu.loginMiGu();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        }
    };

    // 以下为咪咕
    com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
            "10086", "", this);
    private String account = sysinfo.getEpgUserId();
    private String stbId = sysinfo.getSnNum();
    private String token = sysinfo.getEpgToken();
    private String copyRightID = sysinfo.getEpgCopyrightId();

    private String contentId = "8802000528"; // TODO !!!!!!   9901001204

    private MessageHandler messageHandler = new MessageHandler(this);
    MiGu migu = new MiGu(this, messageHandler, account, stbId, contentId, copyRightID, token);

    private class MessageHandler extends Handler {

        private WeakReference<Context> reference;

        public MessageHandler(Context context) {
            reference = new WeakReference<>(context); // 传入activity
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int result = Integer.valueOf(bundle.getString("value"));
            switch (result) {
                case 20: // 20：需要订购（账户的计费标识需要验证）
                    buy(msg);
                    break;
                case 1: // 1：需要订购
                    buy(msg);
                    break;
                case 0: // 0：鉴权成功
                    startActivity(VodPlayer2Activity.class, setNameBundle("videoID", String.valueOf(videoID)));
                    finish();
                    break;
                case 5000: // 系统无响应
                    //btnDtailPlay.setVisibility(View.GONE);
                    showToast("系统无响应，请稍后再试.错误码:5000");
                    break;
                default:
                    showToast("系统开小差了,请稍后再试.错误码:" + result);
                    break;
            }
        }
    }

    private void buy(Message msg) {
        String webUrl = msg.obj.toString();
        if (!Utils.isBlank(webUrl)) {
            MySharedPre.setCurrVideoID(VodDetailActivity.this, String.valueOf(videoID));
            startActivity(WebViewActivity.class, setNameBundle("webUrl", webUrl));
            finish();
        }
    }
    /* 0：鉴权成功,,1：需要订购,,10: 用户未登录,,11：请求参数异常,,12：黑名单用户，鉴权失败,,
     * 20：需要订购（账户的计费标识需要验证）
     * 30：您的帐户不能消费,,31：对应牌照方没可计费的产品,,50: 试看鉴权通过,,5000：系统无响应
     */
}
