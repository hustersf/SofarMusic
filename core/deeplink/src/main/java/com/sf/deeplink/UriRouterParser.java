package com.sf.deeplink;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sf.deeplink.interceptor.AbortInterceptor;
import com.sf.deeplink.interceptor.Interceptor;
import com.sf.deeplink.interceptor.InterceptorChainImpl;
import com.sf.deeplink.interceptor.LogInterceptor;
import com.sf.deeplink.interceptor.LoginInterceptor;
import com.sf.deeplink.interceptor.UriInterceptor;
import com.sf.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析uri {@link okhttp3.RealCall#getResponseWithInterceptorChain}
 */
public class UriRouterParser {
  private static final String TAG = "UriRouterParser";

  @NonNull
  private Activity mActivity;

  public UriRouterParser(@NonNull UriRouterActivity activity) {
    mActivity = activity;
  }

  private @Nullable Uri parseUri() {
    Intent intent = mActivity.getIntent();
    return intent.getData();
  }

  public void parse() {
    List<Interceptor> interceptors = new ArrayList<>();
    interceptors.add(new LogInterceptor());
    interceptors.add(new LoginInterceptor());
    interceptors.add(new UriInterceptor());

    // 总是放在最后
    interceptors.add(new AbortInterceptor());
    Uri uri = parseUri();
    if (uri != null) {
      Interceptor.Chain chain = new InterceptorChainImpl(interceptors, mActivity, uri, 0);
      chain.proceed(uri);
      LogUtil.d(TAG, "uri:" + uri.toString());
    }

  }
}
