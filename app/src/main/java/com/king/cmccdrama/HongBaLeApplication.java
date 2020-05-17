package com.king.cmccdrama;

import android.app.Application;

import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Utils.FileManager;
import com.king.cmccdrama.Utils.ImageLoadOptions;
import com.king.cmccdrama.Utils.WeiLaiAd;
import com.king.cmccdrama.manager.SDKManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

public class HongBaLeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                SDKManager.INSTANCE.initSDK(getApplicationContext());
//                SDKManager.INSTANCE.insertUser(getApplicationContext());
//            }
//        }).start();
        //
        File cacheDir = new File(Config.DIR_TEMP);
        FileManager.makeDir(Config.DIR_ROOT); // 创建程序目录
        FileManager.makeDir(Config.DIR_TEMP); // 创建缓存目录
        ImageLoader.getInstance().init(ImageLoadOptions.getImageLoaderConfiguration(this, cacheDir));

    }
}
