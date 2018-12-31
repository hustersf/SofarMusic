package com.sf.deeplink.interceptor;

/**
 * 总是刚在最后
 */
public class AbortInterceptor implements Interceptor {
  @Override
  public void interceptor(Chain chain) {
    chain.abort();
  }
}
