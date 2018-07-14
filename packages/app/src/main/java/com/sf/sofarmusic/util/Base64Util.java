package com.sf.sofarmusic.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by sufan on 17/7/26.
 */

public class Base64Util {

  public static String decrypt(String str) {
    if (str == null || "".equals(str)) {
      return "";
    }
    String decryptStr = null;
    byte[] bs = Base64.decode(str, Base64.DEFAULT);
    try {
      decryptStr = new String(bs, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return decryptStr;

  }


  public static String encrypt(String str) {
    if (str == null || "".equals(str)) {
      return "";
    }

    String encryptStr = null;
    try {
      encryptStr = Base64.encodeToString(str.getBytes("UTF-8"), Base64.DEFAULT);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    // 字符串反转
    // encryptStr = reverse(encryptStr);
    return encryptStr;
  }

  public static String reverse(String str) {
    return new StringBuffer(str).reverse().toString();
  }
}
