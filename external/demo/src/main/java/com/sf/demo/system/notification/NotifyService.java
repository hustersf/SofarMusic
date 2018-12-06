package com.sf.demo.system.notification;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.sf.utility.LogUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * https://yuqirong.me/2017/02/09/NotificationListenerService%E7%9A%84%E9%82%A3%E4%BA%9B%E4%BA%8B%E5%84%BF/
 * https://www.jianshu.com/p/cfba0d59ec1b?from=timeline
 */
public class NotifyService extends NotificationListenerService {

  public static final String SEND_NOTIFY_BROADCAST = "send_notify_broadcast";
  public static final String TAG = "NotifyService";

  /**
   * 当 NotificationListenerService 是可用的并且和通知管理器连接成功时回调。
   */
  @Override
  public void onListenerConnected() {
    super.onListenerConnected();
  }

  /**
   * 当有通知移除时会回调
   */
  @Override
  public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
    super.onNotificationRemoved(sbn, rankingMap);
  }


  /**
   * 当有新通知到来时会回调
   */
  @Override
  public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
    super.onNotificationPosted(sbn, rankingMap);
    Notification notification = sbn.getNotification();
    String packageName = sbn.getPackageName();
    String content = "";
    String source = NotifyObtainSource.UNKNOWN;

    StringBuffer sb = new StringBuffer();

    if (TextUtils.isEmpty(content)) {
      Bundle extras = notification.extras;
      if (extras != null) {
        // 获取通知内容
        sb.append(extras.getString(Notification.EXTRA_TITLE, ""));
        sb.append("\n");
        content = extras.getString(Notification.EXTRA_TEXT, "");
        sb.append(content);
        source = NotifyObtainSource.EXTRA_TEXT;
      }
    }

    if (TextUtils.isEmpty(content) && !TextUtils.isEmpty(sbn.getNotification().tickerText)) {
      content = sbn.getNotification().tickerText.toString();
      sb.append(content);
      source = NotifyObtainSource.TRICKER_TEXT;
    }


    // 反射获取内容
    if (TextUtils.isEmpty(content)) {
      content = getText(notification);
      sb.append(content);
      source = NotifyObtainSource.REFLECT;
    }


    // 发送广播
    Intent intent = new Intent();
    intent.setAction(SEND_NOTIFY_BROADCAST);
    Bundle bundle = new Bundle();
    bundle.putString("package", packageName);
    bundle.putString("source", source);
    bundle.putString("text", sb.toString());
    intent.putExtras(bundle);
    sendBroadcast(intent);
    LogUtil.d(TAG, "receive a notify:" + packageName + "-" + source + "-" + sb.toString());
  }

  /**
   * 通过反射获取通知内容
   * 7.0以上失效，RemoteViews获取不到
   */
  private String getText(Notification notification) {
    if (null == notification) {
      return null;
    }
    RemoteViews views = notification.bigContentView;
    if (views == null) {
      views = notification.contentView;
    }
    if (views == null) {
      return null;
    }
    // Use reflection to examine the m_actions member of the given RemoteViews object.
    // It's not pretty, but it works.
    List<String> text = new ArrayList<>();
    try {
      Field field = views.getClass().getDeclaredField("mActions");
      field.setAccessible(true);
      @SuppressWarnings("unchecked")
      ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);
      // Find the setText() and setTime() reflection actions
      for (Parcelable p : actions) {
        Parcel parcel = Parcel.obtain();
        p.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        // The tag tells which type of action it is (2 is ReflectionAction, from the source)
        int tag = parcel.readInt();
        if (tag != 2) continue;
        // View ID
        parcel.readInt();
        String methodName = parcel.readString();
        if (null == methodName) {
          continue;
        } else if (methodName.equals("setText")) {
          // Parameter type (10 = Character Sequence)
          parcel.readInt();
          // Store the actual string
          String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
          text.add(t);
        }
        parcel.recycle();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    StringBuffer sb = new StringBuffer();
    for (String s : text) {
      sb.append(s);
      sb.append("\n");
    }
    return sb.toString();
  }


  @Retention(RetentionPolicy.SOURCE)
  public @interface NotifyObtainSource {
    String UNKNOWN = "unknown";
    String TRICKER_TEXT = "ticker_text";
    String EXTRA_TEXT = "extras";
    String REFLECT = "reflect";
  }
}
