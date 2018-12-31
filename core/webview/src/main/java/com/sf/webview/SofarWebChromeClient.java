package com.sf.webview;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class SofarWebChromeClient extends WebChromeClient {

  public final WebViewActivity mWebViewActivity;

  public SofarWebChromeClient(WebViewActivity activity) {
    mWebViewActivity = activity;
  }

  /**
   * 页面加载进度
   */
  @Override
  public void onProgressChanged(WebView view, int newProgress) {
    super.onProgressChanged(view, newProgress);
    // 在这里可以设置进度条的进度
  }

  /**
   * 获取加载页面的标题
   */
  @Override
  public void onReceivedTitle(WebView view, String title) {
    super.onReceivedTitle(view, title);
    mWebViewActivity.setHeadTitle(title);
  }

  /**
   * 拦截js原生的alert，默认返回false
   * 返回true,表示将js的弹窗替换为原生的弹窗
   * 覆盖window.alert
   *
   * 注意：在我们的弹窗回调中，要调用JsResult对应的confirm和cancel
   */
  @Override
  public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
    // 在这里可以替换成app的弹窗，便于统一UI
    return super.onJsAlert(view, url, message, result);
  }

  /**
   * 同onJsAlert，覆盖的是window.confirm
   */
  @Override
  public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
    return super.onJsConfirm(view, url, message, result);
  }

  /**
   * 同onJsAlert，覆盖的是window.prompt
   */
  @Override
  public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
      JsPromptResult result) {
    return super.onJsPrompt(view, url, message, defaultValue, result);
  }
}
