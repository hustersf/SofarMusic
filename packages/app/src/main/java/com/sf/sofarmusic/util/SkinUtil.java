package com.sf.sofarmusic.util;

import static com.sf.libnet.http.HttpConfig.context;

import android.content.Context;

import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.listener.ILoaderListener;
import com.sf.libskin.loader.SkinManager;
import com.sf.utility.SharedPreUtil;

/**
 * Created by sufan on 16/11/5.
 */

public class SkinUtil {


    //从皮肤包中读取资源
    public static void changeSkin(final Context context, String filename) {
        SkinConfig.isSPColor = false;
        SharedPreUtil sp = new SharedPreUtil(context);
        sp.setToggleState("isSpColor", false);

        //加个日夜间模式的判断
        if (!"night.skin".equals(filename)) {
            sp.setToggleString("skinName", filename);
            sp.setToggleState("isNight", false);
        } else {
            sp.setToggleState("isNight", true);
        }

        SkinManager.getInstance().loadSkin(filename, new ILoaderListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String errMsg) {


            }

            @Override
            public void onProgress(int progress) {

            }
        });

    }


    //从sp中读取主题颜色
    public static void changeColorFromSp(Context context, int color) {

        SkinConfig.isSPColor = true;
        SharedPreUtil sp = new SharedPreUtil(context);
        sp.setToggleInt("themeColor", color);
        sp.setToggleState("isSpColor", true);

        sp.setToggleString("skinName", "sp.skin");  //皮肤是从sp中所选颜色的标记
        sp.setToggleState("isNight", false);

        SkinManager.getInstance().loadSkinFromSp();

    }


    //恢复默认主题
    public static void restoreDefaultTheme() {
        SkinConfig.isSPColor = false;
        SharedPreUtil sp = new SharedPreUtil(context);
        sp.setToggleState("isSpColor", false);

        sp.setToggleString("skinName", "");  //置为空
        sp.setToggleState("isNight", false);

        SkinManager.getInstance().restoreDefaultTheme();
    }
}
