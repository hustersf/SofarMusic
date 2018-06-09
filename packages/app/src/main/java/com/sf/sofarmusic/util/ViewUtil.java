package com.sf.sofarmusic.util;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

public class ViewUtil {

	private static final float LARGE_WIDTH_DEVICE_SCALE = 0.15f;
	private static final float DEFAULT_SCALE = 0.07f;
	private static final int LARGE_SCREEN_DP = 480;
	private static final String TAG_ALPHA = "TAG_ALPHA";
	private static final String TAG_COLOR = "TAG_COLOR";

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

	public static <T extends View> T inflate(Context context, int resId) {
		return (T) getInflater(context).inflate(resId, null);
	}

	private static LayoutInflater getInflater(Context context) {
		return LayoutInflater.from(context);
	}

}
