package com.sf.sofarmusic.play.core;


import android.media.MediaPlayer;

/**
 * 音乐播放回调
 */
public abstract class MusicPlayCallbackAdapter implements MusicPlayCallback {

  @Override
  public void onPlayStart(MediaPlayer mp) {}

  @Override
  public void onCompletion(MediaPlayer mp) {}

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {}

}
