package com.sf.sofarmusic.base;

import android.content.Intent;

import com.sf.sofarmusic.callback.CallBackIntent;
import com.sf.sofarmusic.callback.PermissionsResultListener;

/**
 * Created by sufan on 17/7/13.
 */

public interface ActivityInterface {

    /**
     * 启动一个Activity，并且通过onActivityResult()方法将回调事件返回。通过callBackIntent回调将事件传回调用方。
     *
     * @param intent
     * @param callBackIntent 回调方法
     */
    void startActivityForResult(Intent intent, CallBackIntent callBackIntent);

    /**
     * Activity页面关闭时回调方法
     *
     * @param intent
     */
    void setActivityResultCallback(Intent intent);


    /**
     * 权限接口
     *
     * @param desc        首次申请权限被拒绝后再次申请给用户的描述提示
     * @param permissions 要请求的权限
     * @param requestCode 权限请求码
     * @param listener    权限回调接口
     */
    void requestPermissions(String desc, String[] permissions, int requestCode, PermissionsResultListener listener);
}
