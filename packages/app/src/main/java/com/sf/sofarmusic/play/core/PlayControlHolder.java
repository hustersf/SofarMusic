package com.sf.sofarmusic.play.core;


/**
 * 播放控制相关
 */
public class PlayControlHolder {

  private int status = PlayStatus.PAUSE;

  private static final PlayControlHolder holder = new PlayControlHolder();

  public static PlayControlHolder getInstance() {
    return holder;
  }

  /**
   * 设置播放状态
   */
  public void setStatus(@PlayStatus int status) {
    this.status = status;
  }

  /**
   * 是否正在播放
   */
  public boolean isPlaying() {
    return status == PlayStatus.PLAY;
  }

  /**
   * 播放状态
   */
  public @interface PlayStatus {
    int PLAY = 0;
    int PAUSE = 1;
  }
}
