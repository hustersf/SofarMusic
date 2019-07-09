package com.sf.webview;

import android.webkit.JavascriptInterface;

import com.sf.webview.model.JsDialogParams;

/**
 * 供JS调用原生组件
 */
public final class SofarJsBridge {

  public static final String NAME = "sofar";

  private final WebViewActivity mWebViewActivity;
  private final SofarWebview mSofarWebview;


  public SofarJsBridge(WebViewActivity activity, SofarWebview sofarWebview) {
    mWebViewActivity = activity;
    mSofarWebview = sofarWebview;
  }

  /**
   * 原生弹窗
   */
  @JavascriptInterface
  public void showDialog(String params) {
    new JsInvoke<JsDialogParams>(mSofarWebview) {
      @Override
      public void safeRun(JsDialogParams params) {
        callJs(params.callback,new JsSuccessResult());
        callJs(params.callback,new JsErrorResult(JsErrorCode.UNKNOWN_ERROR,"404"));
      }
    }.invoke(params);
  }

}
