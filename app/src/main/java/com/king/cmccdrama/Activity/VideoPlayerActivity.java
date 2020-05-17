package com.king.cmccdrama.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

import com.king.cmccdrama.Const.MySharedPre;
import com.king.cmccdrama.Interface.APIManager;
import com.king.cmccdrama.MPlayerUtil.IMPlayListener;
import com.king.cmccdrama.MPlayerUtil.MPlayer;
import com.king.cmccdrama.MPlayerUtil.MPlayerException;
import com.king.cmccdrama.MPlayerUtil.MinimalDisplay;
import com.king.cmccdrama.Const.Config;

import com.king.cmccdrama.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

// 这个是自己做的播放器，必须要使用未来的播放器
public class VideoPlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private SurfaceView mPlayerView;
    private MPlayer player;
    private SeekBar seekBar;
    private Timer timer;
    private TimerTask timerTask;
    private ImageButton btn_play_pause;
    private TextView tv_time_total, tv_time_current, tv_file_name, tvMoveTime;
    private static final int
            HIDE = 1, SHOW = 2, FINISH = 3, ENABLE_SEEK = 4, ENABLE_PLAY = 5,
            timeout = 5000;//超时时间，毫秒;
    private boolean flag_enable_seek = false;//是否允许快进，默认打开播放器后5s内不允许快进
    private RelativeLayout layoutSeekBar;
    private int videoDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        initView();
        initData();
        initPlayer();
    }

    private void initView() {
        seekBar = findViewById(R.id.seekBar);
        mPlayerView = findViewById(R.id.mPlayerView);
        btn_play_pause = findViewById(R.id.btn_play_pause);
        tv_time_total = findViewById(R.id.tv_time_total);
        tv_time_current = findViewById(R.id.tv_time_current);
        layoutSeekBar = findViewById(R.id.player_layout_seekbar);
        tv_file_name = findViewById(R.id.tv_file_name);
        tvMoveTime = findViewById(R.id.tvMoveTime);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void initData() {
        Utils.init(VideoPlayerActivity.this);
    }

    private void initPlayer() {
        player = new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
        playerOnPrepared();
        playerSurfaceDestroyed();
        playerOnBufferUpdateListen();
        seedCompletedListen();
        starPlayUrl(getVideoUrl());
    }

    private String getVideoUrl() {
        Intent intent = this.getIntent();
        int videoID = Integer.valueOf(intent.getStringExtra("videoID"));
        // 插入播放记录
        int userID = 1; // TODO,写死了,不用管，此文件没用了
        APIManager.getSingleton().setPlayHistory(respHandler,userID,videoID);
        String playUrl = "http://gslbserv.itv.cmvideo.cn/jingju08.ts?channel-id=hongblsp&Contentid=1060000082&authCode=3a&stbId=005903FF000100606001C0132B043D08&usergroup=g29097100000&userToken=6a83ea9ccbeaef860d6149adf107314029pb";
        showToast("测试信息，播放地址为：" + playUrl);
        return playUrl;
    }

    AsyncHttpResponseHandler respHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Log.i(Config.PROJECT_TAG, responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };

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
                    finish();
                    break;
                case ENABLE_SEEK:
                    flag_enable_seek = true;
                    break;
                case ENABLE_PLAY:
                    try {
                        player.play();
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void playerOnBufferUpdateListen() {
        player.setImPlayerBufferUpdate(new MPlayer.IMPlayerBufferUpdate() {
            @Override
            public void onBufferUpdate(final int percent) {
                VideoPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (videoDuration > 0)
                            seekBar.setSecondaryProgress((int) ((percent / 100.0) * videoDuration));
                    }
                });
            }
        });
    }

    private void playerSurfaceDestroyed() {
        player.setiSurfaceDestroyed(new MPlayer.IMPlayerSurfaceDestroyed() {
            @Override
            public void onSurfaceDestroyed() {
                mHandler.removeMessages(HIDE);
                mHandler.removeMessages(SHOW);//快进到最后，特别处理
                mHandler.removeMessages(FINISH);
                mHandler.removeMessages(ENABLE_SEEK);
                mHandler.removeMessages(ENABLE_PLAY);
                if (timer != null) timer.cancel();
                if (timerTask != null) timerTask.cancel();
            }
        });
    }

    private void seedCompletedListen() {
        player.setiSeekCompleted(new MPlayer.ISeekCompleted() {
            @Override
            public void onSeekCompleted() {
                mHandler.removeMessages(HIDE);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
            }
        });
    }

    private void playerSetListen() {
        player.setPlayListener(new IMPlayListener() {
            @Override
            public void onStart(MPlayer player) {

            }

            @Override
            public void onPause(MPlayer player) {
            }

            @Override
            public void onResume(MPlayer player) {
            }

            @Override
            public void onComplete(MPlayer player) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(FINISH), 0);
            }
        });
    }

    private void hideSeekBar() {
        if (player.isPlaying()) {
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

    private void starPlayUrl(String mUrl) {
        if (mUrl.length() > 0) {
            try {
                player.setSource(mUrl);
                player.play();

            } catch (MPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    private void playerOnPrepared() {
        player.setOnPrepared(new MPlayer.IMPlayerPrepared() {
            @Override
            public void onMPlayerPrepare() {
                //播放器准备好后开始计时5秒内不允许快进
                mHandler.sendMessageDelayed(mHandler.obtainMessage(ENABLE_SEEK), timeout);
                playerSetListen();
                videoDuration = player.getDuration();
                seekBar.setMax(videoDuration);
                seekBar.setProgress(player.getCurrentPosition());
                String time = MPlayer.generateTime(videoDuration);
                tv_time_total.setText(" / " + time);
                tv_time_current.setText("00:00");

                mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
                timer = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        final int currentPosition = player.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        VideoPlayerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String time = MPlayer.generateTime(currentPosition);
                                tv_time_current.setText(time);
                                tvMoveTime.setText(time);
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 0, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.onDestroy();
        mHandler.removeCallbacksAndMessages(null); // king, 0323
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
        String time = MPlayer.generateTime(player.getCurrentPosition());
        tv_time_current.setText(time);
        tvMoveTime.setText(time);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), 0);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), timeout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(SHOW), 0);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (player.isPlaying()) {
                    player.pause();
                    btn_play_pause.setImageResource(R.drawable.mediacontroller_pause);
                    btn_play_pause.setVisibility(View.VISIBLE);
                } else {
                    try {
                        player.play();
                        btn_play_pause.setImageResource(R.drawable.mediacontroller_play);
                        mHandler.removeMessages(HIDE);
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(HIDE), 0);
                    } catch (MPlayerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case KeyEvent.KEYCODE_SHIFT_RIGHT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (flag_enable_seek) player.seekTo(player.getCurrentPosition() + 5000);
                break;
            case KeyEvent.KEYCODE_SHIFT_LEFT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (flag_enable_seek) player.seekTo(player.getCurrentPosition() - 5000);
                break;
            case KeyEvent.KEYCODE_BACK:
                String backUrl = MySharedPre.getBackUrl(this);
                if (backUrl.equals("history")) {
                    startActivity(new Intent(VideoPlayerActivity.this, PlayHistoryActivity.class));
                } else if (backUrl.equals("category")) {
                    startActivity(new Intent(VideoPlayerActivity.this, VodCategoryActivity.class));
                }
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void showToast(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
}