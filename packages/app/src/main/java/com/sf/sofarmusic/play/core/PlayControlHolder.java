package com.sf.sofarmusic.play.core;


import com.sf.sofarmusic.base.SofarApp;
import com.sf.utility.SharedPreUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 播放控制相关
 */
public class PlayControlHolder {

  private final static String KEY_MODE = "play_mode";

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
  @Retention(RetentionPolicy.SOURCE)
  public @interface PlayStatus {
    int PLAY = 0;
    int PAUSE = 1;
  }

  /**
   * 播放模式
   */
  @Retention(RetentionPolicy.SOURCE)
  public @interface PlayMode {
    int LIST_CYCLE = 0;
    int SINGLE_CYCLE = 1;
    int RANDOM_CYCLE = 2;
  }

  public void setMode(@PlayMode int mode) {
    SharedPreUtil sharedPreUtil = new SharedPreUtil(SofarApp.getAppContext());
    sharedPreUtil.setToggleInt(KEY_MODE, mode);
  }

  public int getMode() {
    SharedPreUtil sharedPreUtil = new SharedPreUtil(SofarApp.getAppContext());
    return sharedPreUtil.getToggleInt(KEY_MODE) == -1 ? 0 : sharedPreUtil.getToggleInt(KEY_MODE);
  }
}
