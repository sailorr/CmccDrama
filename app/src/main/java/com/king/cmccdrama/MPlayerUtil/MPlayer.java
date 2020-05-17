package com.king.cmccdrama.MPlayerUtil;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * 播放器
 */
public class MPlayer implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnErrorListener, SurfaceHolder.Callback {

    private MediaPlayer player;
    private String source;
    private MinimalDisplay display;
    private boolean isVideoSizeMeasured = false;  //视频宽高是否已获取，且不为0
    private boolean isMediaPrepared = false;      //视频资源是否准备完成
    private boolean isSurfaceCreated = false;     //Surface是否被创建
    private boolean isUserWantToPlay = false;     //使用者是否打算播放
    private boolean isResumed = false;            //是否在Resume状态
    private IMPlayListener mPlayListener;
    private IMPlayerPrepared imPlayerPrepared;
    private ISeekCompleted iSeekCompleted;
    private IMPlayerSurfaceDestroyed imPlayerSurfaceDestroyed;
    private IMPlayerBufferUpdate imPlayerBufferUpdate;
    //private int currentVideoWidth;              //当前视频宽度
    //private int currentVideoHeight;             //当前视频高度

    public interface IMPlayerPrepared {
        void onMPlayerPrepare();
    }

    public interface ISeekCompleted {
        void onSeekCompleted();
    }

    public interface IMPlayerSurfaceDestroyed {
        void onSurfaceDestroyed();
    }

    public interface IMPlayerBufferUpdate {
        void onBufferUpdate(int percent);
    }

    public void setImPlayerBufferUpdate(IMPlayerBufferUpdate onPlayerBufferUpdate) {
        this.imPlayerBufferUpdate = onPlayerBufferUpdate;
    }

    public void setOnPrepared(IMPlayerPrepared onPrepare) {
        this.imPlayerPrepared = onPrepare;
    }

    public void setiSeekCompleted(ISeekCompleted onSeekCompleted) {
        this.iSeekCompleted = onSeekCompleted;
    }

    public void setiSurfaceDestroyed(IMPlayerSurfaceDestroyed onSurfaceDestroyed) {
        this.imPlayerSurfaceDestroyed = onSurfaceDestroyed;
    }

    private void createPlayerIfNeed() {
        if (null == player) {
            player = new MediaPlayer();
            player.setScreenOnWhilePlaying(true);
            player.setOnBufferingUpdateListener(this);
            player.setOnVideoSizeChangedListener(this);
            player.setOnCompletionListener(this);
            player.setOnPreparedListener(this);
            player.setOnSeekCompleteListener(this);
            player.setOnErrorListener(this);
        }
    }

    private void playStart() {
        if (isVideoSizeMeasured && isMediaPrepared && isSurfaceCreated && isUserWantToPlay && isResumed) {
            player.setDisplay(display.getHolder());
            player.start();
            log("视频开始播放");
            display.onStart(this);
            if (mPlayListener != null) {
                mPlayListener.onStart(this);
            }
        }
    }

    private void playPause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            display.onPause(this);
            if (mPlayListener != null) {
                mPlayListener.onPause(this);
            }
        }
    }

    public void seekTo(int msec) {
        player.seekTo(msec);
    }

    public int getDuration() {
        return player.getDuration();
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    private boolean checkPlay() {
        /*
        if (source.length() == 0) {
            return false;
        }
        return true;
        */
        return source.length() != 0;
    }

    public void setPlayListener(IMPlayListener listener) {
        this.mPlayListener = listener;
    }

    /**
     * 设置是否裁剪视频，若裁剪，则视频按照DisplayView的父布局大小显示。
     * 若不裁剪，视频居中于DisplayView的父布局显示
     *
     * @param isCrop 是否裁剪视频
     */
    /*
    //private boolean mIsCrop = false;              //是否剪裁视频

    public void setCrop(boolean isCrop) {
        this.mIsCrop = isCrop;
        if (display != null && currentVideoWidth > 0 && currentVideoHeight > 0) {
            tryResetSurfaceSize(display.getDisplayView(), currentVideoWidth, currentVideoHeight);
        }
    }

    public boolean isCrop() {
        return mIsCrop;
    }

    //根据设置和视频尺寸，调整视频播放区域的大小
    private void tryResetSurfaceSize(final View view, int videoWidth, int videoHeight) {
        ViewGroup parent = (ViewGroup) view.getParent();
        int width = parent.getWidth();
        int height = parent.getHeight();
        if (width > 0 && height > 0) {
            final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            if (mIsCrop) {
                float scaleVideo = videoWidth / (float) videoHeight;
                float scaleSurface = width / (float) height;
                if (scaleVideo < scaleSurface) {
                    params.width = width;
                    params.height = (int) (width / scaleVideo);
                    params.setMargins(0, (height - params.height) / 2, 0, (height - params.height) / 2);
                } else {
                    params.height = height;
                    params.width = (int) (height * scaleVideo);
                    params.setMargins((width - params.width) / 2, 0, (width - params.width) / 2, 0);
                }
            } else {
                if (videoWidth > width || videoHeight > height) {
                    float scaleVideo = videoWidth / (float) videoHeight;
                    float scaleSurface = width / height;
                    if (scaleVideo > scaleSurface) {
                        params.width = width;
                        params.height = (int) (width / scaleVideo);
                        params.setMargins(0, (height - params.height) / 2, 0, (height - params.height) / 2);
                    } else {
                        params.height = height;
                        params.width = (int) (height * scaleVideo);
                        params.setMargins((width - params.width) / 2, 0, (width - params.width) / 2, 0);
                    }
                }
            }
            view.setLayoutParams(params);
        }
    }
    */

    /**
     * 视频状态
     *
     * @return 视频是否正在播放
     */
    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void setSource(String url) throws MPlayerException {
        this.source = url;
        createPlayerIfNeed();
        isMediaPrepared = false;
        isVideoSizeMeasured = false;
        //currentVideoWidth = 0;
        //currentVideoHeight = 0;
        player.reset();
        try {
            player.setDataSource(url);
            player.prepareAsync();
            log("异步准备视频");
        } catch (IOException e) {
            throw new MPlayerException("set source error", e);
        }
    }

    public void setDisplay(MinimalDisplay display) {
        if (this.display != null && this.display.getHolder() != null) {
            this.display.getHolder().removeCallback(this);
        }
        this.display = display;
        this.display.getHolder().addCallback(this);
    }

    public void play() throws MPlayerException {
        if (!checkPlay()) {
            throw new MPlayerException("Please setSource");
        }
        createPlayerIfNeed();
        isUserWantToPlay = true;
        playStart();
    }

    public void pause() {
        isUserWantToPlay = false;
        playPause();
    }

    public void onPause() {
        isResumed = false;
        playPause();
    }

    public void onResume() {
        isResumed = true;
        playStart();
    }

    public void onDestroy() {
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.imPlayerBufferUpdate.onBufferUpdate(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        display.onComplete(this);
        if (mPlayListener != null) {
            mPlayListener.onComplete(this);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isMediaPrepared = true;
        playStart();
        imPlayerPrepared.onMPlayerPrepare();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (width > 0 && height > 0) {
            //this.currentVideoWidth = width;
            //this.currentVideoHeight = height;
            isVideoSizeMeasured = true;
            playStart();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        iSeekCompleted.onSeekCompleted();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (display != null && holder == display.getHolder()) {
            isSurfaceCreated = true;
            //此举保证以下操作下，不会黑屏。（或许还是会有手机黑屏）
            //暂停，然后切入后台，再切到前台，保持暂停状态
            if (player != null) {
                player.setDisplay(holder);
                //不加此句360f4不会黑屏、小米note1会黑屏，其他机型未测
                player.seekTo(player.getCurrentPosition());
            }
            log("surface被创建");
            playStart();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        log("surface大小改变");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (display != null && holder == display.getHolder()) {
            log("surface被销毁");
            isSurfaceCreated = false;
            imPlayerSurfaceDestroyed.onSurfaceDestroyed();
        }
    }

    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ?
                String.format("%02d:%02d:%02d", hours, minutes, seconds) :
                String.format("%02d:%02d", minutes, seconds);
    }

    private void log(String content) {
        Log.e("MPlayer", content);
    }
}
