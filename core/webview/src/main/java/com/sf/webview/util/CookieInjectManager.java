package com.sf.webview.util;

import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import java.util.Arrays;
import java.util.List;

/**
 * 为WebView注入cookie
 */
public class CookieInjectManager {

  private static final List<String> SOFAR_HOSTS =
      Arrays.asList("www.sofar.com", "www.sofarmusic.com");

  public static void initCookie(final WebView webView) {
    CookieSyncManager.createInstance(webView.getContext());
    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      cookieManager.setAcceptThirdPartyCookies(webView, true);
    }

    for (String host : SOFAR_HOSTS) {
      setCookie(host);
    }
  }

  private static void setCookie(String host) {
    // 这里需要去获取http的cookie，然后设置即可
    CookieManager.getInstance().setCookie(host, "");
  }
}
