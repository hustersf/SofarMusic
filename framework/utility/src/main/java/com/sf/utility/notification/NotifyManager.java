package com.sf.utility.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.sf.utility.LogUtil;
import com.sf.utility.reflect.ResUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 通知帮助类
 * 不适用于自定义RemoteViews
 */
public class NotifyManager {

  private static final String TAG = "NotifyManager";
  private Context mContext;

  public NotifyManager(Context context) {
    mContext = context;
  }

  /**
   * type，传入的通知的种类，8.0之后要为通知分渠道
   * title，通知栏标题
   * text，通知栏内容
   */
  public void sendNotifyMsg(@ChannelType int type, String title, String text) {
    NotificationManager manager =
        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel(getChannelId(type), getChannelName(type),
          getChannelImportance(type));
      openNotifySetting(getChannelId(type), manager);
    }

    if (TextUtils.isEmpty(getChannelId(type))) {
      LogUtil.d(TAG, "传入的type不对");
      return;
    }

    int iconId = ResUtil.getMipmapId(mContext, "ic_launcher");
    Notification notification =
        new NotificationCompat.Builder(mContext, getChannelId(type))
            .setContentTitle(title)
            .setContentText(text)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setSmallIcon(iconId)
            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), iconId))
            .build();

    // 发送通知
    manager.notify(getNotifyId(type), notification);
  }


  /**
   * 用于删除渠道
   */
  public void deleteChannel(@ChannelType int type) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager manager =
          (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
      manager.deleteNotificationChannel(getChannelId(type));
    }
  }

  @TargetApi(Build.VERSION_CODES.O)
  private void createNotificationChannel(String channelId, String channelName, int importance) {
    NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
    NotificationManager notificationManager =
        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.createNotificationChannel(channel);
  }


  /**
   * 用户关闭了通知栏,提示打开
   */
  @TargetApi(Build.VERSION_CODES.O)
  private void openNotifySetting(String channelId, NotificationManager manager) {
    NotificationChannel channel = manager.getNotificationChannel(channelId);
    if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
      Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
      intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
      intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
      mContext.startActivity(intent);
      Toast.makeText(mContext, "请手动将通知打开", Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 发送通知所需的id
   */
  private int getNotifyId(@ChannelType int type) {
    int id = 1;
    if (type == ChannelType.CHAT) {
      id = 2;
    } else if (type == ChannelType.NEWS) {
      id = 3;
    }
    return id;
  }

  private String getChannelId(@ChannelType int type) {
    String channelId = "";
    if (type == ChannelType.CHAT) {
      channelId = "chat";
    } else if (type == ChannelType.NEWS) {
      channelId = "news";
    }
    return channelId;
  }

  private String getChannelName(@ChannelType int type) {
    String channelName = "";
    if (type == ChannelType.CHAT) {
      channelName = "聊天消息";
    } else if (type == ChannelType.NEWS) {
      channelName = "新闻";
    }
    return channelName;
  }

  private int getChannelImportance(@ChannelType int type) {
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    if (type == ChannelType.CHAT) {
      importance = NotificationManager.IMPORTANCE_HIGH;
    } else if (type == ChannelType.NEWS) {
      importance = NotificationManager.IMPORTANCE_LOW;
    }
    return importance;
  }

  /**
   * 8.0以上需要为通知 添加分类信息
   * 可根据业务需求，自己定义通知类型
   */
  @Retention(RetentionPolicy.SOURCE)
  public @interface ChannelType {

    int CHAT = 0; // 聊天消息

    int NEWS = 1; // 新闻
  }
}
