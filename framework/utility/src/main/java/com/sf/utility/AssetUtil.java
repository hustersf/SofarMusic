package com.sf.utility;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sufan on 17/6/20.
 */

public class AssetUtil {

  public static String getTextFromAssets(Context context, String fileName) {
    String result = "";
    try {
      InputStream is = context.getAssets().open(fileName);
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      result = new String(buffer, "utf-8");
      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

}
