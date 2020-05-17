package com.king.cmccdrama.Const;

import android.os.Environment;

import java.io.File;

public class Config {

    public static final String PROJECT_TAG = "HongBaLeDebug";
    public static final String PRO_SHARED_NAME = "HongBaLe";

    /**
     * 此APP文件保存目录
     */
    public static final String DIR_ROOT =
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "HongBaLe";
    /**
     * 临时目录
     */
    public static final String DIR_TEMP = DIR_ROOT + File.separator + "temp";

    // API查询结果result参数返回值
    public static final int API_RESULT_OK = 10000;
    public static final int API_RESULT_200 = 200;
    // API请求数据加密
    public static final String SECRET_KEY = "um9AfcA7iFQE%AW8";
    public static final String SECRET_IV = "SkN55%SQTV5K9J3&";

    /**
     * 服务器接口地址
     */
    public static final String API_BASE_URL = "http://liyuanchun.a172.ottcn.com/app/interface.php";

    // 咪咕计费地址
    public static final String MIGU_URL_CALLBACK = "http://liyuanchun.a172.ottcn.com/app/jump.php"; // 计费跳转地址
    public static final String MIGU_AUTHORIZE_URL = "http://cashier.itv.cmvideo.cn/cashier-api/authorize"; // 鉴权地址

    // 咪咕计费信息
    public static final String MIGU_SPID = "699382";
    public static final String MIGU_CONSUME_LOCAL = "22"; // 22：海南
    public static final String MIGU_CONSUME_SCENE = "01"; // 互联网电视
    public static final String MIGU_CONSUME_BEHAVIOUR = "02"; // 点播

    // 未来电视
    public static final String WEILAI_APP_ID = "2df2d46c0937473"; // 正式用APP ID
    public static final String WEILAI_CHANNELCODE = "2626046001"; // 渠道ID/channelCode
    public static final String WEILAI_APPKEY = "cfa0048ead932ef16ab6b5646932c85a"; // Appkey
    public static final String WEILAI_APPSECRET = "4bb844ccd56a485b39df3be218508181"; // AppSecret
}