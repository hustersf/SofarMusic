package com.sf.demo.system.notification;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import com.sf.demo.api.ApiProvider;
import com.sf.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.core.app.NotificationManagerCompat;

/**
 * 用户监听通知内容
 */
public class NotifyListenerHelper {

  private NotifyContentAdapter mAdapter;
  private Context mContext;
  private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

  private final String[] mPakageNames = new String[]{"com.jifen.qukan",
    "com.ss.android.article.news",
    "com.ss.android.article.lite",
    "com.sina.news",
    "com.netease.newsreader.activity",
    "com.netease.news.lite",
    "com.sohu.newsclient",
    "com.tencent.news",
    "com.UCMobile",
    "com.baidu.searchbox"};
  private final String[] mAppNames = new String[]{"趣头条",
    "今日头条",
    "今日头条极速版",
    "新浪新闻",
    "网易新闻",
    "网易新闻极速版",
    "搜狐新闻",
    "腾讯新闻",
    "UC浏览器",
    "手机百度"};
  private List<String> mPackageList = new ArrayList<>();


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

    for (String packageName : mPakageNames) {
      mPackageList.add(packageName);
    }

    // testData();
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

    // 过滤特定的包名
    if (mPackageList.contains(notifyContent.mPackageName)
      && !TextUtils.isEmpty(notifyContent.mContent)) {
      int index = mPackageList.indexOf(notifyContent.mPackageName);
      notifyContent.mAppName = mAppNames[index];
      uploadNews(notifyContent);
      mAdapter.setData(notifyContent);
    }
  }

  /**
   * 将信息上传至服务端
   */
  private void uploadNews(NotifyContent notifyContent) {
    String[] texts = notifyContent.mContent.split("\n");
    String title = "";
    String content = texts[0];
    if (texts.length > 1) {
      title = texts[0];
      content = texts[1];
    }

    if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
      return;
    }

    notifyContent.mTitle = title;
    notifyContent.mContent = content;

    uploadNotifyMessage(notifyContent);
  }

  private void uploadNotifyMessage(NotifyContent notifyContent) {
    String url = "http://getkwai.test.gifshow.com/pearl-admin-server/api/v1/push/upload";
    ApiProvider.getDemoApiService().uploadNotifyMessage(url, notifyContent)
      .subscribe(actionResponse -> {
        LogUtil.d("NotifyService", "上传成功");

      }, throwable -> {
        LogUtil.d("NotifyService", "上传报错：" + throwable.getMessage());
      });
  }

  private void testData() {
    NotifyContent notifyContent = new NotifyContent();
    notifyContent.mPackageName = "packageName";
    notifyContent.mAppName = "appName";
    notifyContent.mSoruce = "source";
    notifyContent.mTitle = "测试标题";
    notifyContent.mContent = "测试内容";

    uploadNotifyMessage(notifyContent);
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
