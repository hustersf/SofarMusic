package com.sf.base;

import android.content.Intent;

import com.sf.base.callback.CallBackIntent;

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
}
