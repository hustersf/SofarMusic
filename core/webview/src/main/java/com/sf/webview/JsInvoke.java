package com.sf.webview;

import android.text.TextUtils;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;

/**
 * 原生和JS相互调用
 * 
 * @param <T>
 */
public abstract class JsInvoke<T extends Serializable> {
  private WebView mWebView;

  public JsInvoke(WebView webView) {
    mWebView = webView;
  }

  public void invoke(String jsParams) {
    T params = null;
    try {
      if (!TextUtils.isEmpty(jsParams)) {
        Type type = getClass().getGenericSuperclass();
        Class clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        Gson gson = new Gson();
        params = (T) gson.fromJson(jsParams, clazz);
      }
      safeRun(params);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public abstract void safeRun(T params);


  /**
   * 原生调用JS javascript:callback('params')
   * 
   * @param callback 回调方法名
   * @param params 回调传参
   * 
   */
  public void callJs(String callback, Object params) {
    if (params instanceof CharSequence || params instanceof Number) {
      mWebView.loadUrl("javascript:" + callback + "('" + String.valueOf(params) + "')");
    } else if (params != null) {
      String content = "";
      try {
        Gson gson = new Gson();
        content = URLEncoder.encode(gson.toJson(params)
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\'", "\\\'"), "UTF-8");
        // +号替换成空格 原来是+号的还原
        content = content.replace("+", "%20").replace("%2b", "+");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      mWebView.loadUrl("javascript:" + callback + "('" + content + "')");
    } else {
      mWebView.loadUrl("javascript:" + callback + "()");
    }
  }
}
