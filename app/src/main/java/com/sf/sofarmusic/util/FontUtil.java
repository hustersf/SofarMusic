package com.sf.sofarmusic.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by sufan on 16/11/18.
 */

//便于统一修改
public class FontUtil {

    public static Typeface setFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.ttf");
    }
}
