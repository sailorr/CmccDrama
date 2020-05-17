package com.king.cmccdrama.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.cmccdrama.BuildConfig;
import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.R;
import com.king.cmccdrama.Utils.ImageLoadOptions;
import com.king.cmccdrama.Utils.WeiLaiAd;
import com.king.cmccdrama.Utils.WeiLaiLog;
import com.king.cmccdrama.Utils.WeiLaiLogin;
import com.king.cmccdrama.manager.SDKManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WelComeActivity extends BaseActivity {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView startAdImg;
    private TextView startAdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);
     Log.d("Test", "WelComeActivity  onCreate---->: ");
        //
        startAdImg = findViewById(R.id.main_start_ad_img);
        startAdText = findViewById(R.id.start_ad_text);
        if (!SDKManager.INSTANCE.isLoginWeilai) {
            finish();
        } else {
            showToast("直接打开应用！");
            playAd();
        }
    }

    private boolean loginWeiLai() {
        // 登录未来
        String result = WeiLaiLogin.sdkInit(WelComeActivity.this);
        WeiLaiLog.logUpload(88, "0," + BuildConfig.VERSION_CODE);
        Log.i(Config.PROJECT_TAG, "login SDK version:" + WeiLaiLogin.getValueByKey("EXT_VERSION_CODE"));
        if (!result.equals("1")) {
            showToast(result);
            finish();
            return false;
        }
        return true;
    }



    private void playAd() {
        // 开机广告
        Map adMap = WeiLaiAd.playStartAd(this);
        if (null != adMap) {
            if (null != adMap.get("filePath")) {
                String filePath = adMap.get("filePath").toString();
                final int playTime = Integer.valueOf(adMap.get("playTime").toString());
                imageLoader.displayImage(
                        filePath, startAdImg, ImageLoadOptions.getDisplayImageOptions()); // 加载网络图片
                String mid = adMap.get("mid").toString();
                String aid = adMap.get("aid").toString();
                String mtid = adMap.get("mtid").toString();
                WeiLaiAd.report(this, mid, aid, mtid, "", "", "", "");
                Timer timer = new Timer();
                TimerTask tast = new TimerTask() {
                    int time = playTime;
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startAdText.setText("广告剩余" + time + "秒");
                                time--;
                                if (time == 0) {
                                    startActivity(MainActivity.class);
                                    finish();
                                }
                            }
                        });
                    }
                };
                timer.schedule(tast, playTime , 1000); // playTime秒后
            } else {
                startActivity(MainActivity.class);
                finish();
            }
        } else {
            startActivity(MainActivity.class);
            finish();
        }


    }
}
