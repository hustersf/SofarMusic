package com.sf.sofarmusic.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.sf.utility.AppUtil;
import com.sf.utility.LogUtil;

public class ProcessProtectedService extends Service {

  public static void startService(Context context) {
    Intent intent = new Intent(context, ProcessProtectedService.class);
    // 8.0不允许在后台起服务，会崩溃
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(intent);
    } else {
      context.startService(intent);
    }
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
    // 8.0启动前台服务，被启动的服务需要在5S内调用startForground
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForeground(1, creatNotification());
    }
    return super.onStartCommand(intent, flags, startId);
  }


  private Notification creatNotification() {
    // 设定的通知渠道id，必须唯一
    String channelId = this.getPackageName();
    // 8.0 需要创建渠道信息
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // 设定的通知渠道名称
      String channelName = this.getPackageName();
      // 设置通知的重要程度
      int importance = NotificationManager.IMPORTANCE_LOW;
      // 构建通知渠道
      NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
      channel.setDescription("描述");
      // 向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
    return builder.build();
  }
}
