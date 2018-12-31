package com.sf.webview;

import android.webkit.JavascriptInterface;

/**
 * 供JS调用原生组件
 */
public final class SofarJsBridge {

  public static final String NAME = "sofar";

  public final WebViewActivity mWebViewActivity;

  public SofarJsBridge(WebViewActivity activity) {
    mWebViewActivity = activity;
  }

  /**
   * 原生弹窗
   */
  @JavascriptInterface
  public void showDialog(String dialogJson) {
    // 展示原生的dialog
  }

}
