/**
 * @Title: SharedPreUtil.java
 * @Package cn.com.csii.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015-3-12 下午2:43:25
 * @version V1.0
 */
package com.sf.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by sufan on 16/12/13.
 */

public class SharedPreUtil {

    /**
     * sharedpreferences 存储文件名
     * */
    public static final String SPConfig = "app_config";
    public SharedPreferences preferences;

    public SharedPreUtil(Context context) {
        preferences = context.getSharedPreferences(SPConfig, Context.MODE_PRIVATE);
    }

    /**
     * SharedPreferences本地存储
     * @return boolean
     * */
    public boolean getToggleState(String key) {
        return preferences.getBoolean(key, false);
    }

    /**
     * SharedPreferences本地存储
     * @param boolean
     * */
    public void setToggleState(String key, boolean state) {
        preferences.edit().putBoolean(key, state).commit();
    }

    /**
     * SharedPreferences本地存储
     * @return String
     * */
    public String getToggleString(String key) {
        return preferences.getString(key, "");
    }

    /**
     * SharedPreferences本地存储
     * @param String
     * */
    public void setToggleString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    /**
     * SharedPreferences本地存储
     * @return int
     * */
    public int getToggleInt(String key) {
        return preferences.getInt(key, -1);
    }

    /**
     * SharedPreferences本地存储
     * @param int
     * */
    public void setToggleInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public Editor getToggleEdit() {
        return preferences.edit();
    }

}
