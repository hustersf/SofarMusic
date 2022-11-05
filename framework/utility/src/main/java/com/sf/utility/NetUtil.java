package com.sf.utility;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    // 判断当前是否有网络
    public static boolean isNetAvailable(Context context) {
        boolean isNetAvailable = false;
        ConnectivityManager nManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = nManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            isNetAvailable = networkInfo.isAvailable();
        }
        return isNetAvailable;
    }

    // 判断是否在wifi状态下
    public static boolean isWifiAvailable(Context context) {
        boolean isWifiAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            isWifiAvailable = true;
        }
        return isWifiAvailable;
    }

    // 判断是否是手机数据
    public static boolean isMobileAvailable(Context context) {
        boolean isWifiAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            isWifiAvailable = true;
        }
        return isWifiAvailable;
    }

    // 获得当前的网络类型
    public static String getNetWorkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();

        return info.getTypeName();
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
