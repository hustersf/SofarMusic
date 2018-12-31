package com.sf.webview;

import com.sf.base.UIRootActivity;

/**
 * 呈现web页面
 */
public class WebViewActivity extends UIRootActivity {

  private SofarWebview mWebView;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_webview;
  }

  @Override
  protected void initTitle() {}

  @Override
  protected void initView() {
    mWebView = findViewById(R.id.webview);
  }

  @Override
  protected void initData() {
    mWebView.loadUrl("file:///android_asset/h5.html");
  }

  @Override
  protected void initEvent() {

  }

  public void setHeadTitle(String title) {
    mHeadTitleTv.setText(title);
  }
}
