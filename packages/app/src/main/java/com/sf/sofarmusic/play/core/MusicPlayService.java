package com.sf.sofarmusic.play.core;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.sf.utility.LogUtil;

/**
 * 音乐播放服务
 */
public class MusicPlayService extends Service {

  private static final String TAG = "MusicPlayService";

  private MusicPlayer playerHelper;

  public class PlayBinder extends Binder {
    public MusicPlayer getMusicPlayerHelper() {
      return playerHelper;
    }
  }

  private PlayBinder binder = new PlayBinder();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    LogUtil.d(TAG, "onBind");
    return binder;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    playerHelper = MusicPlayer.getInstance();
    LogUtil.d(TAG, "onCreate");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    LogUtil.d(TAG, "onStartCommand");
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    LogUtil.d(TAG, "onDestroy");
  }
}
