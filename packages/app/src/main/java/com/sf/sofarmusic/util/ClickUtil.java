package com.sf.sofarmusic.util;

import java.util.Calendar;

/**
 * Created by sufan on 17/9/11.
 */

public class ClickUtil {

    private static long lastClickTime;

    public synchronized static boolean isFastDoubleClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > 0 && currentTime - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }
}
