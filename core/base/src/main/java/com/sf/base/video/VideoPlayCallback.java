package com.sf.base.video;

import android.media.MediaPlayer;

/**
 * 视频播放回调
 */
public interface VideoPlayCallback {

  // 开始播放
  void onPlayStart(MediaPlayer mp);

  // 当前歌曲播放完成
  void onCompletion(MediaPlayer mp);

  // 歌曲缓冲回调
  void onBufferingUpdate(MediaPlayer mp, int percent);
}
