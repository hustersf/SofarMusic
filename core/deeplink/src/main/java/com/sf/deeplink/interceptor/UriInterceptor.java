package com.sf.deeplink.interceptor;

import com.sf.deeplink.matcher.UriMatcher;

public class UriInterceptor implements Interceptor {

  @Override
  public void interceptor(Chain chain) {
    UriMatcher uriMatcher = new UriMatcher();
    if (uriMatcher.match(chain.uri())) {
      // 启动activity
      chain.context().startActivity(uriMatcher.result(chain.context(), chain.uri()));
      chain.abort();
      return;
    }
    chain.proceed(chain.uri());

  }
}
