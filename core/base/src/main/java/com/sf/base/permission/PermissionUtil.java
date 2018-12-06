package com.sf.base.permission;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.sf.utility.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;

/**
 * 权限工具类
 */
public class PermissionUtil {

  /**
   * 申请单个权限
   */
  public static Observable<Permission> requestPermission(FragmentActivity activity,
      String permissionName) {
    return requestPermission(activity, permissionName, "", "");
  }

  /**
   * 申请单个权限
   * refuseHint 权限被拒绝后的提示
   * settingHint 权限被拒绝，并勾选不再询问的提示
   */
  public static Observable<Permission> requestPermission(FragmentActivity activity,
      String permissionName, String refuseHint, String settingHint) {
    if (activity == null || TextUtils.isEmpty(permissionName)) {
      return Observable.just(new Permission("", false));
    }

    return new RxPermissions(activity).requestEach(permissionName).doOnNext(permission -> {
      if (permission.granted) {
        // 权限允许
      } else if (permission.shouldShowRequestPermissionRationale) {
        // 权限拒绝，但未勾选不再提示
        showRefuseHint(activity, refuseHint);
      } else {
        // 权限拒绝，并勾选不再提示,需要跳转至设置界面
        showSettingDialog(activity, settingHint);
      }
    });
  }


  /**
   * 申请多个权限,有一个拒绝则返回false
   */
  public static Observable<Boolean> requestPermissions(FragmentActivity activity,
      String[] permissions) {
    return requestPermissions(activity, permissions, "", "");
  }

  /**
   * 申请多个权限,有一个拒绝则返回false
   */
  public static Observable<Boolean> requestPermissions(FragmentActivity activity,
      String[] permissions, String refuseHint, String settingHint) {
    if (activity == null || permissions.length == 0) {
      return Observable.just(Boolean.FALSE);
    }
    return new RxPermissions(activity).request(permissions).doOnNext(aBoolean -> {
      if (!aBoolean) {
        // 权限被拒
        List<String> denyPermissions = new ArrayList<>();
        // 筛选出被拒绝的权限
        for (int i = 0; i < permissions.length; i++) {
          if (!hasPermission(activity, permissions[i])) {
            denyPermissions.add(permissions[i]);
          }
        }

        boolean shouldShowRequestPermissionRationale = false;
        for (String permision : denyPermissions) {
          // 有一个未勾选不再提示，则置为true
          if (shouldShowRationale(activity, permision)) {
            shouldShowRequestPermissionRationale = true;
          }
        }
        if (shouldShowRequestPermissionRationale) {
          showRefuseHint(activity, refuseHint);
        } else {
          showSettingDialog(activity, settingHint);
        }
      }
    });
  }


  /**
   * 拒绝权限，但未勾选不再提醒的提示
   */
  private static void showRefuseHint(FragmentActivity activity, String hint) {
    if (TextUtils.isEmpty(hint)) {
      return;
    }
    ToastUtil.startShort(activity, hint);
  }

  /**
   * 拒绝权限，并勾选不再提醒的弹窗
   */
  private static void showSettingDialog(FragmentActivity activity, String hint) {
    if (TextUtils.isEmpty(hint)) {
      return;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(activity)
        .setMessage(hint)
        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            startAppSettingActivity(activity);
          }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    builder.show();
  }

  /**
   * 跳转至设置界面
   * 此方法并不能适配所有手机
   */
  public static void startAppSettingActivity(FragmentActivity activity) {
    try {
      Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
      activity.startActivity(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * 判断是否用某个权限
   */
  public static boolean hasPermission(Context context, String permission) {
    if (context == null || TextUtils.isEmpty(permission)) {
      return false;
    }
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
        || ContextCompat.checkSelfPermission(context,
            permission) == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * 返回true，表示权限被拒绝，但是未勾选不再提示
   */
  public static boolean shouldShowRationale(FragmentActivity activity, String permission) {
    if (activity == null || TextUtils.isEmpty(permission)) {
      return false;
    }
    return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
  }
}
