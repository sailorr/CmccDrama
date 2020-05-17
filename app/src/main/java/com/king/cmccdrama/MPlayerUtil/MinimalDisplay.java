package com.king.cmccdrama.MPlayerUtil;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Description:
 */
public class MinimalDisplay {

    private SurfaceView surfaceView;

    public MinimalDisplay(SurfaceView surfaceView){
        this.surfaceView=surfaceView;
    }

    //@Override
    public View getDisplayView() {
        return surfaceView;
    }

    //@Override
    public SurfaceHolder getHolder() {
        return surfaceView.getHolder();
    }

    //@Override
    public void onStart(MPlayer player) {

    }

    //@Override
    public void onPause(MPlayer player) {

    }

    //@Override
    public void onResume(MPlayer player) {

    }

    //@Override
    public void onComplete(MPlayer player) {

    }
}
