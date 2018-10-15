package com.sf.utility;


import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

public class ViewUtil {

    private static final float LARGE_WIDTH_DEVICE_SCALE = 0.15f;
    private static final float DEFAULT_SCALE = 0.07f;
    private static final int LARGE_SCREEN_DP = 480;

    /**
     * 获取边缘滚动距离
     * 用户是否触发了边缘滚动操作的临界值
     */
    public static float getEdgeSlop(Context context) {
        ViewConfiguration config = ViewConfiguration.get(context);
        float edgeSlop = config.getScaledEdgeSlop();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            float density = context.getResources().getDisplayMetrics().density;
            float deviceDp = (float) Math.floor(screenWidth / density);
            float scale = (edgeSlop * 1.0f / (screenWidth * 1.0f));
            if (deviceDp >= LARGE_SCREEN_DP) {
                if (scale < LARGE_WIDTH_DEVICE_SCALE) {
                    edgeSlop = screenWidth * LARGE_WIDTH_DEVICE_SCALE;
                }
            } else {
                if (scale < DEFAULT_SCALE) {
                    edgeSlop = screenWidth * DEFAULT_SCALE;
                }
            }
        }
        return edgeSlop;
    }

    /**
     * 创建View
     */
    public static <T extends View> T inflate(ViewGroup parent, int resId) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
        return (T) view;
    }

    /**
     * 创建View
     */
    public static <T extends View> T inflate(Context context, int resId) {
        return (T) LayoutInflater.from(context).inflate(resId, null);
    }

    /**
     * 测量文字的宽度
     */
    public static float measureTextWidth(String text, float textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        return paint.measureText(text);
    }

}
