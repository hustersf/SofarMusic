package com.sf.webview;

import android.webkit.DownloadListener;

/**
 * 实现WebView文件下载的功能
 */
public class SofarWebViewDownloadListener implements DownloadListener {

  public final WebViewActivity mWebViewActivity;

  public SofarWebViewDownloadListener(WebViewActivity activity) {
    mWebViewActivity = activity;
  }

  @Override
  public void onDownloadStart(String url, String userAgent, String contentDisposition,
      String mimetype, long contentLength) {
    // 在这里，调用我们自己的下载任务，实现文件的下载

  }
}
