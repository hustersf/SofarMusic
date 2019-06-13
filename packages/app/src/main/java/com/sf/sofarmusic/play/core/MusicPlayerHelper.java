package com.sf.sofarmusic.play.core;

import android.media.AudioManager;
import android.media.MediaPlayer;


public class MusicPlayerHelper
    implements
      MediaPlayer.OnBufferingUpdateListener,
      MediaPlayer.OnCompletionListener,
      MediaPlayer.OnPreparedListener,
      MediaPlayer.OnErrorListener {

  private MediaPlayer mediaPlayer;

  private int totalDuration; // 总时长
  private int secondProgress; // 缓冲的百分比

  public MusicPlayerHelper() {
    init();
  }

  private void init() {
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
    mediaPlayer.setOnBufferingUpdateListener(this);
    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnErrorListener(this);
  }

  /**
   * 播放音乐
   * 
   * @param path file path or http path
   */
  public void play(String path) {
    try {
      mediaPlayer.setDataSource(path);
      mediaPlayer.prepareAsync();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 暂停播放
   */
  public void pause() {
    mediaPlayer.pause();
  }

  /**
   * 停止播放
   */
  public void stop() {
    mediaPlayer.stop();
    mediaPlayer.release();
    mediaPlayer = null;
  }

  /**
   * 移动到指定位置播放
   * 
   * @param progress 音频的百分比
   */
  public void seekTo(int progress) {
    int i = (int) (progress * 1.0f / 100 * totalDuration);
    mediaPlayer.seekTo(i);
  }

  /**
   *
   * @return 当前的播放位置
   */
  public int getCurrentPosition() {
    return mediaPlayer.getCurrentPosition();
  }

  /**
   *
   * @return 播放总时长
   */
  public int getTotalDuration() {
    return totalDuration;
  }

  /**
   *
   * @return 已缓冲好的百分比
   */
  public int getSecondProgress() {
    return secondProgress;
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    secondProgress = percent;
  }

  @Override
  public void onCompletion(MediaPlayer mp) {

  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    return false;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    totalDuration = mp.getDuration();
  }
}
