package com.sf.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;

public class DeviceUtil {

  /**
   * 是否横屏
   */
  public static boolean isLandscape(Context context) {
    Configuration configuration = context.getResources().getConfiguration();
    int ori = configuration.orientation;
    return ori == configuration.ORIENTATION_LANDSCAPE;
  }

  public static int getStatusBarHeight(Context context) {
    int statusBarHeight = 0;
    try {
      Class c = Class.forName("com.android.internal.R$dimen");
      Object o = c.newInstance();
      Field field = c.getField("status_bar_height");
      int x = (Integer) field.get(o);
      statusBarHeight = context.getResources().getDimensionPixelSize(x);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return statusBarHeight;
  }

  public static int getNavigationBarHeight(Context context) {
    Resources resources = context.getResources();
    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
    int height = resources.getDimensionPixelSize(resourceId);
    return height;
  }

  // 获取是否存在NavigationBar
  public static boolean checkDeviceHasNavigationBar(Context context) {
    boolean hasNavigationBar = false;
    Resources rs = context.getResources();
    int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
    if (id > 0) {
      hasNavigationBar = rs.getBoolean(id);
    }
    try {
      Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
      Method m = systemPropertiesClass.getMethod("get", String.class);
      String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
      if ("1".equals(navBarOverride)) {
        hasNavigationBar = false;
      } else if ("0".equals(navBarOverride)) {
        hasNavigationBar = true;
      }
    } catch (Exception e) {

    }
    return hasNavigationBar;

  }

  // 获取手机屏幕尺寸 宽度
  public static int getMetricsWidth(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    int screenWidth = dm.widthPixels;// 屏幕高（像素，如：800px）
    return screenWidth;
  }

  // 获取手机屏幕尺寸 高度
  public static int getMetricsHeight(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    int screenHeight = dm.heightPixels;// 屏幕高（像素，如：800px）
    return screenHeight;
  }

  // 获取手机dpi
  public static int getMetricsDensityDpi(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.densityDpi;// 屏幕dpi（像素，如：240dpi）

  }

  // 获取手机基准比例
  public static float getMetricsDensity(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    return dm.density;// 屏幕基准比例（像素，如：3x）
  }


  // 获取设备型号
  public static String getModel() {
    return Build.MODEL;
  }

  // 获取操作系统
  public static String getOS() {
    return "Android版本" + Build.VERSION.RELEASE;
  }

  /**
   * 获取手机IMEI获取手机IMEI(设备唯一标示号)
   * 有的手机返回全0（华为手机没权限时返回全0，小米5 没权限直接崩溃）
   * 并且需要READ_PHONE_STATE权限，这个权限在6.0还需要动态获取
   * 综上所述，不再用IMEI作为设置唯一标示号
   */
  @Deprecated
  public static String getIMEI(Context context) {
    TelephonyManager tm = (TelephonyManager) context
        .getSystemService(Context.TELEPHONY_SERVICE);
    return tm.getDeviceId();
  }

  /**
   * 设备唯一标示号（设备首次启动，系统随机生成的一个64位的数字，并已16进制字符串保存下来）
   * 缺陷 厂商bug
   * 1.不同的设备可能会产生相同的ANDROID_ID
   * 2.有些设备返回null
   */
  public static String getUUid(Context context) {
    String uuid = Settings.Secure.getString(context.getContentResolver(),
        Settings.Secure.ANDROID_ID);
    return uuid;
  }

  /**
   * 设备唯一标示号
   * 2.3版本以上可用该方法
   * 但是该方法已经过时（27版本）
   */
  public static String getSerialNumber(Context context) {
    return Build.SERIAL;
  }

  /**
   * 获得当前的网络类型
   * 需要ACCESS_WIFI_STATE权限，不是危险权限
   */
  public static String getNetWorkType(Context context) {
    ConnectivityManager connectMgr = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo info = connectMgr.getActiveNetworkInfo();

    return info == null ? "" : info.getTypeName();

  }

  // 获取ip地址
  public static String getIP(Context context) {
    WifiManager wifiManager = (WifiManager) context
        .getSystemService(Context.WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    int i = wifiInfo.getIpAddress();
    return int2ip(i);
  }

  private static String int2ip(int ipInt) {
    StringBuilder sb = new StringBuilder();
    sb.append(ipInt & 0xFF).append(".");
    sb.append((ipInt >> 8) & 0xFF).append(".");
    sb.append((ipInt >> 16) & 0xFF).append(".");
    sb.append((ipInt >> 24) & 0xFF);
    return sb.toString();
  }

  // 运营商的名字
  public static String getCarrier(final Context context) {
    String carrier = "";
    final TelephonyManager manager =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (manager != null) {
      carrier = manager.getNetworkOperatorName();
    }
    if (carrier == null || carrier.length() == 0) {
      carrier = "";
      Log.i("TAG", "No carrier found");

    }
    return carrier;
  }

  /**
   * 获取Activity Content区域宽度.
   * note：在刚触发onConfigurationChanged事件时调用，会获取到配置变更之前的高宽
   */
  public static int getDisplayWidth(@NonNull Activity activity) {
    return getContentView(activity).getWidth();
  }

  /**
   * 获取Activity Content区域高度.(沉浸式包含了状态栏的高度)
   * note：在刚触发onConfigurationChanged事件时调用，会获取到配置变更之前的高宽
   */
  public static int getDisplayHeight(@NonNull Activity activity) {
    return getContentView(activity).getHeight();
  }

  public static View getContentView(@NonNull Activity activity) {
    return getContentView(activity.getWindow());
  }

  public static View getContentView(@NonNull Window window) {
    return window.getDecorView().findViewById(android.R.id.content);
  }

}
