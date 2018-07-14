package com.sf.sofarmusic.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by sufan on 16/12/12.
 */

public class AppUtil {

  private final static int kSystemRootStateUnknow = -1;
  private final static int kSystemRootStateDisable = 0;
  private final static int kSystemRootStateEnable = 1;
  private static int systemRootState = kSystemRootStateUnknow;


  // 判断手机是否已经ROOT
  public static boolean isRootSystem() {
    if (systemRootState == kSystemRootStateEnable) {
      return true;
    } else if (systemRootState == kSystemRootStateDisable) {
      return false;
    }
    File f = null;
    final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
        "/system/sbin/", "/sbin/", "/vendor/bin/"};
    try {
      for (int i = 0; i < kSuSearchPaths.length; i++) {
        f = new File(kSuSearchPaths[i] + "su");
        if (f != null && f.exists()) {
          systemRootState = kSystemRootStateEnable;
          return true;
        }
      }
    } catch (Exception e) {}
    systemRootState = kSystemRootStateDisable;
    return false;
  }

  public static String getTopActivity(Context context) {
    ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
    if (runningTaskInfos != null)
      return (runningTaskInfos.get(0).topActivity).toString();
    else
      return null;
  }


  public static boolean isHome(Context context) {
    ActivityManager mActivityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
    List<String> strs = getHomes(context);
    if (strs != null && strs.size() > 0) {
      return strs.contains(rti.get(0).topActivity.getPackageName());
    } else {
      return false;
    }
  }

  private static List<String> getHomes(Context context) {
    List<String> names = new ArrayList<String>();
    PackageManager packageManager = context.getPackageManager();
    // 属性
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
        PackageManager.MATCH_DEFAULT_ONLY);
    for (ResolveInfo ri : resolveInfo) {
      names.add(ri.activityInfo.packageName);
    }
    return names;
  }


  // 去应用市场
  public static void goMarket(Context context) {
    final Uri uri = Uri.parse("market://details?id="
        + context.getPackageName());
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  /**
   * 判断APP是否是在后台运行
   *
   * @param ctx
   * @return boolean
   */
  public static boolean isAppOnForeground(Context ctx) {
    android.app.ActivityManager activityManager = (android.app.ActivityManager) ctx
        .getApplicationContext().getSystemService(
            Context.ACTIVITY_SERVICE);
    String packageName = ctx.getApplicationContext().getPackageName();

    List<ActivityManager.RunningAppProcessInfo> appProcesses =
        ((android.app.ActivityManager) activityManager)
            .getRunningAppProcesses();
    if (appProcesses == null) {
      return false;
    }
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
      if (appProcess.processName.equals(packageName)
          && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取应用程序包名
   */
  public static String getPackageName(Context context) {
    return context.getPackageName();
  }

  /**
   * 获取应用程序图标
   */
  public Drawable getAppIcon(Context context) {
    try {
      // 包管理操作管理类
      PackageManager pm = context.getPackageManager();
      // 获取到应用信息
      ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
      return info.loadIcon(pm);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取应用程序名称
   */
  public static String getAppName(Context context) {
    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(
          context.getPackageName(), 0);
      int labelRes = packageInfo.applicationInfo.labelRes;
      return context.getResources().getString(labelRes);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * [获取应用程序版本名称信息]
   *
   * @param context
   * @return 当前应用的版本名称
   */
  public static String getVersionName(Context context) {
    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packageInfo = packageManager.getPackageInfo(
          context.getPackageName(), 0);
      return packageInfo.versionName;

    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取当前本地apk的版本
   */
  public static int getVersionCode(Context context) {
    int versionCode = 0;
    try {
      // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
      versionCode =
          context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionCode;
  }
}


