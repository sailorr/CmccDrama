package com.king.cmccdrama.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Const.MySharedPre;
import com.king.cmccdrama.Interface.APIManager;
import com.king.cmccdrama.R;
import com.king.cmccdrama.Utils.WeiLaiAd;
import com.king.cmccdrama.Utils.WeiLaiLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import tv.icntv.been.IcntvPlayerInfo;
import tv.icntv.icntvplayersdk.IcntvPlayer;
import tv.icntv.icntvplayersdk.iICntvPlayInterface;

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_ENTER;

// 未来电视播放器
public class VodPlayer2Activity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private FrameLayout weiLaiPlayer;
    // 未来电视
    private IcntvPlayerInfo playInfo = new IcntvPlayerInfo();
    IcntvPlayer player;
    // 自定义进度条
    private SeekBar seekBar;
    private TextView tv_time_total, tv_time_current, tv_file_name, tvMoveTime;
    private RelativeLayout layoutSeekBar;
    private ImageButton btn_play_pause;
    private boolean flag_enable_seek = false;//是否允许快进，默认打开播放器后5s内不允许快进
    private static final int
            HIDE = 1, SHOW = 2, FINISH = 3, ENABLE_SEEK = 4, ENABLE_PLAY = 5,
            timeout = 5000;//超时时间，毫秒;
    private int videoDuration;
    private Timer timer;
    private TimerTask timerTask;
    // 节目基础信息
    int categoryID, videoID, duration, status;
    String contentID, ftpFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_player2);
        //
        weiLaiPlayer = findViewById(R.id.weiLaiPlayer);
        //
        initView();
        initData();
    }

    private void initView() {
        seekBar = findViewById(R.id.seekBar);
        btn_play_pause = findViewById(R.id.btn_play_pause);
        tv_time_total = findViewById(R.id.tv_time_total);
        tv_time_current = findViewById(R.id.tv_time_current);
        layoutSeekBar = findViewById(R.id.player_layout_seekbar);
        tv_file_name = findViewById(R.id.tv_file_name);
        tvMoveTime = findViewById(R.id.tvMoveTime);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void initData() {
        Intent intent = this.getIntent();
        int videoID = Integer.valueOf(intent.getStringExtra("videoID"));
        APIManager.getSingleton().getVodDetail(respHandler, videoID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        mHandler.removeCallbacksAndMessages(null); // king, 0323
        mHandler.removeMessages(HIDE);
        mHandler.removeMessages(SHOW);//快进到最后，特别处理
        mHandler.removeMessages(FINISH);
        mHandler.removeMessages(ENABLE_SEEK);
        mHandler.removeMessages(ENABLE_PLAY);
        if (timer != null) timer.cancel();
        if (timerTask != null) timerTask.cancel();
    }

    private void play() {
        String playUrl = getPlayUrl(ftpFile,contentID);
        playInfo.setPlayUrl(playUrl);
        playInfo.setApp_id(Config.WEILAI_APP_ID); // APPID
        playInfo.setCheckType("program"); // 审核类型;program代表节目播放鉴权，programset代表节目 集播放鉴权 。
        playInfo.setProgramID(String.valueOf(videoID)); // 设置节目id方法，从播控平台取值     // 396:下线节目;;378:可以播放
        playInfo.setProgramListID(String.valueOf(categoryID)); // 设置节目集ID方法，从播控平台取值
        playInfo.setDuration(duration); // 设置节目时长方法，从播控平台取值
        player = new IcntvPlayer(this, weiLaiPlayer, playInfo, new callback());
        //
        bufferEnd = System.currentTimeMillis();
        bufferDuration = bufferEnd - bufferStart;
        //
        com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
                "10086", "", this);
        String mobile = sysinfo.getEpgAccountIdentity();
        //APIManager.getSingleton().user(userHandler,mobile);
        APIManager.getSingleton().userInfo(userInfoHandler, mobile);
    }

    private String getPlayUrl(String ftpFile, String contentID) {
        com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
                "10086", "", this);
        String token = sysinfo.getEpgToken();
        String stbID = sysinfo.getSnNum();
        String userGroup = sysinfo.getEpgUserGroup();
        String playUrl = String.format("http://gslbserv.itv.cmvideo.cn/%s?channel-id=hongblsp&Contentid=%s&authCode=3a&stbId=%s&usergroup=%s&userToken=%s\")",
                ftpFile,contentID,stbID,userGroup,token);
        Log.i(Config.PROJECT_TAG,"视频播放地址:" + playUrl);
        return playUrl;
    }

    int userID = 0;
    AsyncHttpResponseHandler userInfoHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Log.i(Config.PROJECT_TAG, responseString);
                JSONObject response = new JSONObject(responseString);
                int result = response.getInt("Result");
                if (result == Config.API_RESULT_OK) {
                    JSONArray data = response.getJSONArray("Data");
                    JSONObject value = data.getJSONObject(0);
                    Log.i(Config.PROJECT_TAG,"value:" + value.toString());
                    userID = value.getInt("user_id");
                }
                APIManager.getSingleton().setPlayHistory(setPlayHandler,userID,videoID);
            } catch (org.json.JSONException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };

    AsyncHttpResponseHandler setPlayHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Log.i(Config.PROJECT_TAG, responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };

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
                    for (int i = 0; i < count; i++) {
                        JSONObject value = data.getJSONObject(i);
                        categoryID = value.getInt("category_id");
                        contentID = value.getString("content_id");
                        videoID = value.getInt("video_id");
                        ftpFile = value.getString("ftp_file").replaceAll("mp4","ts");
                        duration = value.getInt("duration");
                        status = value.getInt("status");
                    }
                }
                if (status == 0) {
                    showToast("该节目已下线");
                    finishPage();
                }
                // 播前贴广告
                String adList = WeiLaiAd.getAD(VodPlayer2Activity.this, "before");
                Log.i(Config.PROJECT_TAG,adList);
                play(); // 开始播放
            } catch (org.json.JSONException ex) {
                ex.printStackTrace();
            }
        }
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };

    long bufferDuration = 0; // 缓冲时间
    long bufferStart = 0;
    long bufferEnd = 0;
    class callback implements iICntvPlayInterface {

        @Override
        public void onPrepared() {
            // 角标广告
            String adList = WeiLaiAd.getAD(VodPlayer2Activity.this, "float");
            Log.i(Config.PROJECT_TAG,adList);
            Log.i(Config.PROJECT_TAG,"节目加载完成");
            //
            if ((player.isPlaying() && !player.isADPlaying())) {
                //播放器准备好后开始计时5秒内不允许快进
                mHandler.sendMessageDelayed(mHandler.obtainMessage(ENABLE_SEEK), timeout);
                //playerSetListen();
                videoDuration = player.getDuration();
                seekBar.setMax(videoDuration);
                seekBar.setProgress(player.getCurrentPosition());
                String time = generateTime(videoDuration);
                tv_time_total.setText(" / " + time);
                tv_time_current.setText("00:00");

                mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        final int currentPosition = player.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        VodPlayer2Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String time = generateTime(currentPosition);
                                tv_time_current.setText(time);
                                tvMoveTime.setText(time);
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 0, 100);
                showSeekbar();
            }
        }

        @Override
        public void onCompletion() {
            showToast("节目播放结束");
            //player.release();
            Log.i(Config.PROJECT_TAG,"节目播放结束");
            //
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FINISH), 0);
            finishPage();
        }

        @Override
        public void onBufferStart(String s) {
            bufferStart = System.currentTimeMillis();
            Log.i(Config.PROJECT_TAG,"开始加载数据，加载视频或卡顿时调用这个方法，调用些方法后可以调出等待框 等操作");
            showToast("当前网速不给力，正在缓冲中");
        }

        @Override
        public void onBufferEnd(String s) {
            Log.i(Config.PROJECT_TAG,"结束加载数据，加载视频或结束卡顿时调用这个方法，调用些方法后可以收起等 待框等操作");
        }

        @Override
        public void onError(int what, int extra, String msg) {
            if (what == CHECK_QUERY_RETURN_FALSE) {
                Log.i(Config.PROJECT_TAG,"未来文档原文：审核查询未通过时，调用 onError 方法时错误类型为此值");
                showToast("该节目已下线");
                finishPage();
            }
            Log.i(Config.PROJECT_TAG,"播放器出错了,what:" + what);
            Log.i(Config.PROJECT_TAG,"播放器出错了,extra:" + extra);
            Log.i(Config.PROJECT_TAG,"播放器出错了,msg:" + msg);
        }

        @Override
        public void onTimeout() {
            Log.i(Config.PROJECT_TAG,"播放器加载视频超时回调方法");
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (player.isPlaying()) {
            mHandler.removeMessages(HIDE);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishPage() {
        String backUrl = MySharedPre.getBackUrl(this);
        if (backUrl.equals("history")) {
            startActivity(new Intent(VodPlayer2Activity.this, PlayHistoryActivity.class));
        } else if (backUrl.equals("category")) {
            startActivity(new Intent(VodPlayer2Activity.this, VodCategoryActivity.class));
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(Config.PROJECT_TAG,"keycode:" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: // 返回键
                //WeiLaiLog.logUpload(4,
                //        "3," + categoryID + "," + videoID + ",1,1," + duration * 1000 + "," + player.getCurrentPosition() + ",1");
                //player.release();
                finishPage();
                break;
            case 22: // 快进
                if (player.isPlaying()) { // 广告没播放时响应
                    showSeekbar();
                    player.seekTo(player.getCurrentPosition() + 5 * 1000);
                }
                break;
            case 21: // 快退
                if (player.isPlaying()) { // 广告没播放时响应
                    showSeekbar();
                    int duration = player.getCurrentPosition() - 5 * 1000;
                    if (duration > 0) {
                        player.seekTo(duration);
                    }
                }
                break;
            case KEYCODE_ENTER: // 66,OK键
                if (!player.isADPlaying()) { // 广告没播放时响应
                    showSeekbar();
                    playOrPause();
                }
            case KEYCODE_DPAD_CENTER: // 23，OK键
                if (!player.isADPlaying()) { // 广告没播放时响应
                    showSeekbar();
                    playOrPause();
                }
                break;
            case 19: // 向上键
                if (player.isPlaying()) {
                    showSeekbar();
                }
                break;
            case 20: // 向下键
                if (player.isPlaying()) {
                    showSeekbar();
                }
                break;
            /*
                default:
                mHandler.sendMessageDelayed(mHandler.obtainMessage(SHOW), 0);
                break;
             */
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playOrPause() {
        if (!player.isADPlaying()) { // 广告没播放时响应
            if (player.isPlaying()) { // 暂停
                btn_play_pause.setImageResource(R.drawable.mediacontroller_pause);
                btn_play_pause.setVisibility(View.VISIBLE);
                // 暂停广告
                String adList = WeiLaiAd.getAD(VodPlayer2Activity.this, "pause");
                Log.i(Config.PROJECT_TAG,adList);
                player.pauseVideo();
                //WeiLaiLog.logUpload(4,
                //        "12," + categoryID + "," + videoID + ",1,1," + duration * 1000 + "," + player.getCurrentPosition() + "," + contentID);
            } else {
                btn_play_pause.setImageResource(R.drawable.mediacontroller_play);
                mHandler.removeMessages(HIDE);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), 0);
                //
                player.startVideo();
                //WeiLaiLog.logUpload(4,
                //        "2," + categoryID + "," + videoID + ",1,1," + duration * 1000 + "," + player.getCurrentPosition() + "," + contentID);
            }
        }
    }

    protected void showToast(String msg) {
        Toast toast = new Toast(this);
        TextView view = new TextView(this);
        view.setBackgroundResource(android.R.color.black);
        view.setTextSize(28);
        view.setTextColor(Color.WHITE);
        view.setText(msg);
        view.setPadding(10, 10, 10, 10);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }

    private void hideSeekBar() {
        if (player.isPlaying() && (!player.isADPlaying())) {
            layoutSeekBar.setVisibility(View.GONE);
            btn_play_pause.setVisibility(View.GONE);
            tv_file_name.setVisibility(View.GONE);
        }
    }

    private void showSeekbar() {
        layoutSeekBar.setVisibility(View.VISIBLE);
        tv_file_name.setVisibility(View.VISIBLE);
        if (!player.isPlaying()) {
            btn_play_pause.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int seekBarRealWidth = seekBar.getWidth() - 2 * ConvertUtils.dp2px(15);//15在xml中写死了
        float average = (float) (seekBarRealWidth * 1.0 / seekBar.getMax());
        int left = (int) (progress * average) + tvMoveTime.getWidth() / 4;
        tvMoveTime.setX(left);//当前播放时间的位置跟随seekbar
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (flag_enable_seek) player.seekTo(seekBar.getProgress());
        String time = generateTime(player.getCurrentPosition());
        tv_time_current.setText(time);
        tvMoveTime.setText(time);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), 0);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE:
                    hideSeekBar();
                    break;
                case SHOW:
                    showSeekbar();
                    break;
                case FINISH:
                    finishPage();
                    break;
                case ENABLE_SEEK:
                    flag_enable_seek = true;
                    break;
                case ENABLE_PLAY:
                    player.startVideo();
                    break;
            }
        }
    };

    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ?
                String.format("%02d:%02d:%02d", hours, minutes, seconds) :
                String.format("%02d:%02d", minutes, seconds);
    }
}
