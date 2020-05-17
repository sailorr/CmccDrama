package com.king.cmccdrama.MPlayerUtil;

/**
 * Description:
 */
public interface IMPlayListener {
    void onStart(MPlayer player);
    void onPause(MPlayer player);
    void onResume(MPlayer player);
    void onComplete(MPlayer player);
}
