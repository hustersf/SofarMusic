package com.sf.sofarmusic.util;

import android.content.Context;
import android.support.annotation.StringRes;

public class TextUtil {

  public static String getString(Context context, @StringRes int strRes, Object... args) {
    String result = context.getString(strRes);
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        result = result.replace("${" + i + "}", String.valueOf(args[i]));
      }
    }
    return result;
  }
}
