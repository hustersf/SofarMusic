package com.sf.utility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sufan on 16/11/5.
 */

public class ToastUtil {

    public static void startShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void startLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
