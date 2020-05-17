package com.king.cmccdrama.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.king.cmccdrama.BuildConfig;
import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Const.MySharedPre;
import com.king.cmccdrama.Interface.APIManager;
import com.king.cmccdrama.R;
import com.king.cmccdrama.Utils.DateTime;
import com.king.cmccdrama.Utils.WeiLaiLog;
import com.king.cmccdrama.Utils.WeiLaiLogin;
import com.king.cmccdrama.Utils.WeiLaiUpdate;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    private ImageView btnAllVideo, btnPerson, btnPromotion, btn_main_history, btn_main_sign;
    private ImageView imageVideoBtn1, imageVideoBtn2, imageVideoBtn3, imageVideoBtn4;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test", "onCreate: MainActivity--->");
        //
        initView();
        initData();
        setClick();
        // 插入用户
        com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
                "10086", "", this);
        String mobile = sysinfo.getEpgAccountIdentity();
        APIManager.getSingleton().user(userHandler, mobile);
        // 升级检查
        if (WeiLaiUpdate.init()) {
            String result = WeiLaiUpdate.getAppUpgradeInfo();
            JSONObject response;
            try {
                response = new JSONObject(result);
                Log.i(Config.PROJECT_TAG, "response:" + response.toString());
                String packageAddr = response.getString("packageAddr");
                int versionCode = response.getInt("versionCode");
                Log.i(Config.PROJECT_TAG, "packageAddr:" + packageAddr);
                if ((versionCode > BuildConfig.VERSION_CODE) && (null != packageAddr)) {
                    //showToast("有新版本了");
                    //UIData.create().setTitle("有新版本了").setContent("点击确定更新");
                    String upContent = "点击确定更新版本" + versionCode;
                    AllenVersionChecker.getInstance().downloadOnly(
                            UIData.create().
                                    setTitle("有更新了").
                                    setContent(upContent).
                                    setDownloadUrl(packageAddr)
                    ).executeMission(this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        btnAllVideo = findViewById(R.id.btn_main_all_video);  // 戏曲大全
        btnPerson = findViewById(R.id.btn_main_person);       //  个人中心
        btnPromotion = findViewById(R.id.btn_main_promotion); // 活动中心
        btn_main_history = findViewById(R.id.btn_main_history); // 活动中心
        btn_main_sign = findViewById(R.id.btn_main_sign); // 签到
        //
        imageVideoBtn1 = findViewById(R.id.main_image_video_1);
        imageVideoBtn2 = findViewById(R.id.main_image_video_2);
        imageVideoBtn3 = findViewById(R.id.main_image_video_3);
        imageVideoBtn4 = findViewById(R.id.main_image_video_4);
    }

    private void initData() {
        // 取首页VOD列表
        // APIManager.getSingleton().getPortalVodList(respHandler);
    }

    private void setClick() {
        btnAllVideo.setOnClickListener(this);
        btnPerson.setOnClickListener(this);
        btnPromotion.setOnClickListener(this);
        btn_main_history.setOnClickListener(this);
        btn_main_sign.setOnClickListener(this);
        //
        imageVideoBtn1.setOnClickListener(this);
        imageVideoBtn2.setOnClickListener(this);
        imageVideoBtn3.setOnClickListener(this);
        imageVideoBtn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_all_video:
                startActivity(VodCategoryActivity.class);  // 戏曲大全
                break;
            case R.id.btn_main_person:
                startActivity(PersonalActivity.class);    // 个人中心
                break;
            case R.id.btn_main_promotion:
                startActivity(PromotionActivity.class);   // 活动中心
                break;
            case R.id.btn_main_history:
                startActivity(PlayHistoryActivity.class); // 观看记录
                break;
            case R.id.btn_main_sign:
                if (userSign()) {
                    showToast("签到成功");
                } else {
                    showToast("您今天已签到过");
                }
                break;
            case R.id.main_image_video_1:
                Log.i(Config.PROJECT_TAG, "这是1");
                startActivity(VodCategoryActivity.class);  // 戏曲大全
                break;
            case R.id.main_image_video_2:
                Log.i(Config.PROJECT_TAG, "这是2");
                startActivity(VodCategoryActivity.class);  // 戏曲大全
                break;
            case R.id.main_image_video_3:
                Log.i(Config.PROJECT_TAG, "这是3");
                startActivity(VodCategoryActivity.class);  // 戏曲大全
                break;
            case R.id.main_image_video_4:
                Log.i(Config.PROJECT_TAG, "这是4");
                startActivity(VodCategoryActivity.class);  // 戏曲大全
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("king", "keycode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                WeiLaiLog.logUpload(88, "1");
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean userSign() {
        String data = DateTime.getCurrentDateTime2();
        String preData = MySharedPre.getSignData(this);
        if (!data.equals(preData)) {
            com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
                    "10086", "", this);
            String mobile = sysinfo.getEpgAccountIdentity();
            APIManager.getSingleton().userSign(signHandler, mobile, data);
            MySharedPre.setSignData(this, data);
            return true;
        } else {
            return false;
        }
    }

    AsyncHttpResponseHandler signHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Log.i(Config.PROJECT_TAG, "签到返回:" + responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        }
    };

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
