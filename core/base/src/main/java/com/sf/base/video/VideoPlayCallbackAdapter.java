package com.sf.base.video;


import android.media.MediaPlayer;

/**
 * 视频播放回调
 */
public abstract class VideoPlayCallbackAdapter implements VideoPlayCallback {

  @Override
  public void onPlayStart(MediaPlayer mp) {}

  @Override
  public void onCompletion(MediaPlayer mp) {}

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {}

}
