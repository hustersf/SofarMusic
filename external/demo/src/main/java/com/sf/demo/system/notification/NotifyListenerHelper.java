package com.sf.demo.system.notification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.content.pm.PackageManager;
import android.content.ComponentName;

import java.util.Set;

/**
 * 用户监听通知内容
 */
public class NotifyListenerHelper {

  private NotifyContentAdapter mAdapter;
  private Context mContext;
  private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

  private BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      updateData(intent);
    }
  };

  public NotifyListenerHelper(Context context, NotifyContentAdapter adapter) {
    mContext = context;
    mAdapter = adapter;
    registBroadCast();
  }

  private void registBroadCast() {
    IntentFilter filter = new IntentFilter(NotifyService.SEND_NOTIFY_BROADCAST);
    mContext.registerReceiver(receiver, filter);
  }

  public void unRegistBroadCast() {
    mContext.unregisterReceiver(receiver);
  }

  private void updateData(Intent intent) {
    Bundle bundle = intent.getExtras();
    NotifyContent notifyContent = new NotifyContent();
    notifyContent.mPackageName = bundle.getString("package");
    notifyContent.mSoruce = bundle.getString("source");
    notifyContent.mContent = bundle.getString("text");
    mAdapter.setData(notifyContent);
  }

  /**
   * 去设置页面，打开开关
   */
  public void openSetting() {
    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
    if (!(mContext instanceof Activity)) {
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    mContext.startActivity(intent);
  }

  /**
   * 判断是否已授权
   */
  public boolean isEnabled() {
    String pkgName = mContext.getPackageName();
    final String flat =
        Settings.Secure.getString(mContext.getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
    if (!TextUtils.isEmpty(flat)) {
      final String[] names = flat.split(":");
      for (int i = 0; i < names.length; i++) {
        final ComponentName cn = ComponentName.unflattenFromString(names[i]);
        if (cn != null) {
          if (TextUtils.equals(pkgName, cn.getPackageName())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * 判断监听服务是否在授权列表中
   */
  private boolean isNotificationListenerServiceEnabled() {
    Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(mContext);
    if (packageNames.contains(mContext.getPackageName())) {
      return true;
    }
    return false;
  }

  /**
   * 被杀后再次启动时，监听不生效的问题
   * 重新绑定该服务
   */
  public void toggleNotificationListenerService() {
    PackageManager pm = mContext.getPackageManager();
    pm.setComponentEnabledSetting(
        new ComponentName(mContext, NotifyService.class),
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    pm.setComponentEnabledSetting(
        new ComponentName(mContext, NotifyService.class),
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
  }

}
