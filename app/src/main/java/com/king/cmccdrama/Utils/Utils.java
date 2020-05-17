package com.king.cmccdrama.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Utils {

    /**
     * setSharedPreferences
     *
     * @param context        Context
     * @param sharedFileName 存储的文件名
     * @param key            存储的变量名
     * @param value          存储变量名值
     */
    public static Boolean setSharedString(Context context, String sharedFileName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static Boolean setSharedBool(Context context, String sharedFileName, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static Boolean setSharedInt(Context context, String sharedFileName, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedFileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 从HTTP URL路径取文件名
     */
    public static String getFileNameFromUrl(String url) {
        int lastIndex = url.lastIndexOf("/");
        int endIndex = url.lastIndexOf(".");
        return url.substring(lastIndex + 1,endIndex);
    }

    /**
     * 是否是第一次运行
     */
    public static boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FirstRun",0);
        Boolean first_run = sharedPreferences.getBoolean("First",true);
        if (first_run){
            sharedPreferences.edit().putBoolean("First",false).commit();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 当前打开的URL是否有效
     * @param url
     * @return
     */
    public static boolean urlIsRight(String url) {
        if ((url != null) && (!url.equals("about:blank")) && (!url.equals("")) && (!url.equals("null"))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBlank(String string){
        if(string == null || "".equals(string)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 模拟向下键
     */
    public static void sendKey20() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Instrumentation inst=new Instrumentation();
                //inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
                Utils.execByRuntime("input keyevent 20");
            }
        }).start();
    }

    /**
     * 执行shell 命令， 命令中不必再带 adb shell
     *
     * @param cmd
     * @return Sting  命令执行在控制台输出的结果
     */
    public static String execByRuntime(String cmd) {
        Process process = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            inputStreamReader = new InputStreamReader(process.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);

            int read;
            char[] buffer = new char[4096];
            StringBuilder output = new StringBuilder();
            while ((read = bufferedReader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                } catch (Throwable t) {
                    //
                }
            }
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (Throwable t) {
                    //
                }
            }
            if (null != process) {
                try {
                    process.destroy();
                } catch (Throwable t) {
                    //
                }
            }
        }
    }
}
