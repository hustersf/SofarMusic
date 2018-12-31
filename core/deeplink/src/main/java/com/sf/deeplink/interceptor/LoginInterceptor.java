package com.sf.deeplink.interceptor;

/**
 * 在这里可以进行是否登录的拦截
 */
public class LoginInterceptor implements Interceptor {
  @Override
  public void interceptor(Chain chain) {
    // 在这里判断是否需要登录
    chain.proceed(chain.uri());
  }
}
