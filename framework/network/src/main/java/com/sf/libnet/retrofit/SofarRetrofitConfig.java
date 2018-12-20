package com.sf.libnet.retrofit;

import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.sf.libnet.cookie.MemoryCookieJar;
import com.sf.libnet.gson.Gsons;
import com.sf.libnet.https.HttpsUtil;
import com.sf.libnet.interceptor.ContentLengthInterceptor;
import com.sf.libnet.interceptor.HeadersInterceptor;
import com.sf.libnet.interceptor.ParamsInterceptor;

import okhttp3.OkHttpClient;

public class SofarRetrofitConfig implements RetrofitConfig {
  private static final String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/";
  private static final int DEFAULT_TIME_OUT = 30;// 超时时间 30s

  @Override
  public Params buildParams() {
    return new SofarParams();
  }

  @Override
  public String buildBaseUrl() {
    return BASE_URL;
  }

  @Override
  public Gson buildGson() {
    return Gsons.SOFAR_GSON;
  }

  @Override
  public OkHttpClient buildClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);// 连接超时时间
    builder.writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);// 写操作 超时时间
    builder.readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);// 读操作超时时间

    // 后续会添加一些拦截器
    Params params = buildParams();
    builder.addInterceptor(new HeadersInterceptor(params));
    builder.addInterceptor(new ParamsInterceptor(params));
    builder.addInterceptor(new ContentLengthInterceptor());

    // cookie配置
    builder.cookieJar(new MemoryCookieJar());

    // 添加https证书认证
    builder.sslSocketFactory(HttpsUtil.getStandardSocketFactory());
    return builder.build();
  }

}
