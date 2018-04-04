package com.sf.sofarmusic.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DensityUtil {

	public static int dp2px(Context context, float dpVal) {
		int value = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
						.getDisplayMetrics());
		return value;
	}

	public static int sp2px(Context context, float spVal) {
		int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
		return value;
	}

	public static float px2dp(Context context, float pxVal) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (pxVal / scale);
	}

	public static float px2sp(Context context, float pxVal) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (pxVal / scale);
	}

	public static int getScreenDPI(Context context) {
		DisplayMetrics metric =context.getResources().getDisplayMetrics();
		int dpi = metric.densityDpi;
		return dpi;
	}

}
