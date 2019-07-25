package com.sf.sofarmusic.play.core;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 音乐播放服务
 */
public class MusicPlayService extends Service {

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
    return binder;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    playerHelper = new MusicPlayer(this);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
