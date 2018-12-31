package com.sf.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sf.webview.util.UriComponent;

/**
 * 监听各种事件的回调
 */
public class SofarWebViewClient extends WebViewClient {

  public final WebViewActivity mWebViewActivity;

  public SofarWebViewClient(WebViewActivity activity) {
    mWebViewActivity = activity;
  }


  /**
   * 在页面开始加载时回调
   */
  @Override
  public void onPageStarted(WebView view, String url, Bitmap favicon) {
    super.onPageStarted(view, url, favicon);
    // 可以在这里显示进度条
  }

  /**
   * 在页面加载结束后回调
   */
  @Override
  public void onPageFinished(WebView view, String url) {
    super.onPageFinished(view, url);
    // 可以在这里隐藏进度条
  }


  /**
   * 拦截url跳转
   * 返回true,表示对url进行拦截，进行自己的操作
   * 返回false,不拦截url,交由系统操作
   */
  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    // 在这里可以对url，进行解析，可以和前端协定某种规则，如果url是这种规则，可以跳转到我们的原生页面
    if (TextUtils.isEmpty(url)) {
      return false;
    }

    Uri uri = Uri.parse(url);
    Intent intent = UriComponent.createIntentWithUri(mWebViewActivity, uri);
    if (intent != null) {
      mWebViewActivity.startActivity(intent);
      return true;
    }

    return false;
  }

  /**
   * 页面加载出现错误时回调
   */
  @Override
  public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    super.onReceivedError(view, errorCode, description, failingUrl);
    // 在这里可以提示错误信息，或者展示自己的错误页面
    view.loadUrl("https://jingyan.baidu.com/article/6d704a1336575b28db51ca02.html");
  }

  /**
   * 加载的页面的url是https类型，并且出现错误时回调
   *
   * cancel 默认行为，停止加载不收信任的https页面
   * proceed 忽略SSL证书错误，继续加载页面
   */
  @Override
  public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    super.onReceivedSslError(view, handler, error);
  }
}
