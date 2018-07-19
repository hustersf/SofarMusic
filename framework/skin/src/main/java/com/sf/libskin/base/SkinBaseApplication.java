package com.sf.libskin.base;

import android.app.Application;
import android.util.Log;

import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;
import com.sf.libskin.utils.SharedPreUtil;
import com.sf.libskin.utils.SkinFileUtils;

import java.io.File;
import java.io.IOException;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:54
 */
public class SkinBaseApplication extends Application {

    public void onCreate() {

        super.onCreate();
        initSkinLoader();
    }

    /**
     * Must call init first
     */
    private void initSkinLoader() {
        //初始化是否从sp中读取主题颜色
        SharedPreUtil sp = new SharedPreUtil(getBaseContext());
        SkinConfig.isSPColor = sp.getToggleState("isSpColor");

        setUpSkinFile();
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().loadSkin();
    }

    private void setUpSkinFile() {
        try {
            String[] skinFiles = getAssets().list(SkinConfig.SKIN_DIR_NAME);
            for (String fileName : skinFiles) {
                File file = new File(SkinFileUtils.getSkinDir(this), fileName);
                if (!file.exists())
                    SkinFileUtils.copySkinAssetsToDir(this, fileName, SkinFileUtils.getSkinDir(this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
