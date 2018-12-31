package com.sf.webview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.sf.utility.DensityUtil;
import com.sf.webview.util.CookieInjectManager;

public class SofarWebview extends WebView {

  private ProgressBar mLoadingProgressBar;

  public SofarWebview(Context context) {
    this(context, null);
  }

  public SofarWebview(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SofarWebview(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initWebViewSettings();
    supportVirtualKeyboardFocus();
    initProgressBar();
    CookieInjectManager.initCookie(this);
  }

  private void initWebViewSettings() {
    // 一些基本的配置
    getSettings().setJavaScriptEnabled(true);
    getSettings().setBuiltInZoomControls(true);
    getSettings().setSupportZoom(true);
    getSettings().setUseWideViewPort(true);
    getSettings().setLoadWithOverviewMode(true);
    getSettings().setDomStorageEnabled(true);

    // 关闭文件协议，解决 webview 漏洞导致的问题
    getSettings().setAllowFileAccessFromFileURLs(false);
    getSettings().setAllowUniversalAccessFromFileURLs(false);
    getSettings().setAllowFileAccess(false);

    // support https request load http resource
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    // 缓存模式
    getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

    // 开启webview的debug模式，在google浏览器输入[chrome://inspect]
    if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      WebView.setWebContentsDebuggingEnabled(true);
    }


    addJavascriptInterface(new SofarJsBridge((WebViewActivity) getContext()), SofarJsBridge.NAME);
    setWebViewClient(new SofarWebViewClient((WebViewActivity) getContext()));
    setWebChromeClient(new SofarWebChromeClient((WebViewActivity) getContext()));
    setDownloadListener(new SofarWebViewDownloadListener((WebViewActivity) getContext()));

  }

  /**
   * 为WebView加一个进度条
   */
  private void initProgressBar() {
    mLoadingProgressBar =
        new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
    mLoadingProgressBar.setMax(100);
    mLoadingProgressBar
        .setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_webview));
    addView(mLoadingProgressBar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        DensityUtil.dp2px(getContext(), 3)));
  }

  /**
   * link: https://code.google.com/p/android/issues/detail?id=7189
   * 解决WebView获取不到键盘焦点问题
   */
  private void supportVirtualKeyboardFocus() {
    setFocusable(true);
    setFocusableInTouchMode(true);
    requestFocus(View.FOCUS_DOWN);
    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
          case MotionEvent.ACTION_UP:
            if (!v.hasFocus()) {
              v.requestFocus();
            }
            break;
        }
        return false;
      }
    });
  }

}
