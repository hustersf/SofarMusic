package com.sf.utility;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;

public class LanguageUtil {

  public static String getCurrentLanguage(Context context) {

    String language =
        context.getApplicationContext().getResources().getConfiguration().locale.getLanguage();
    if (TextUtils.isEmpty(language)) {
      language = Locale.getDefault().getLanguage();
    }
    return language;

  }

  public static String getCurrentCountry(Context context) {
    String country =
        context.getApplicationContext().getResources().getConfiguration().locale.getCountry();
    if (TextUtils.isEmpty(country)) {
      country = Locale.getDefault().getCountry();
    }
    return country;
  }


}
