package com.sf.sofarmusic.job;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sf.demo.window.notification.NotifyUtil;
import com.sf.utility.AppUtil;
import com.sf.utility.LogUtil;

public class ProcessProtectedService extends Service {

  private static int id = 0;

  // 8.0不允许在后台起服务，会崩溃
  public static void startService(Context context) {
    Intent intent = new Intent(context, ProcessProtectedService.class);
    context.startService(intent);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    LogUtil.d(JobConstant.TAG, "ProcessProtectedService:" + AppUtil.getProcessName(this));
    createNotify();
    return super.onStartCommand(intent, flags, startId);
  }

  private void createNotify() {
    NotifyUtil util = new NotifyUtil(this, id++);
    util.showNotify();
  }
}
