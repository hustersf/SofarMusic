package com.sf.libnet.interceptor;


import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by allen on 2016/12/20.
 * <p>
 * okHttp请求拦截器
 * 统一添加请求头
 */

public class OkHttpRequestInterceptor implements Interceptor {

  private Map<String, Object> headerMaps = new TreeMap<>();

  public OkHttpRequestInterceptor(Map<String, Object> headerMaps) {
    this.headerMaps = headerMaps;
  }

  public OkHttpRequestInterceptor() {}

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request.Builder request = chain.request().newBuilder();

    /**
     * 在这里可以根据不同的交易设置不同的头，比如下载文件addHeader("Cache-Control", "no-cache")
     * 其他可以addHeader("Cache-Control", "max-age=60")
     */

    if (headerMaps == null || headerMaps.size() == 0) {
      request
          .addHeader("OS", "android")
          .addHeader("Content-Type", "application/json")
          // .addHeader("Connection", "keep-alive")
          .addHeader("Accept-Encoding", "identity")
          .addHeader("Cache-Control", "no-cache") // 缓存可能导致无法正常显示文件下载进度（getContentLength=-1）
          .addHeader("User-Agent", System.getProperty("http.agent")); // 没有这个ua,报403错误
      // .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML,
      // like Gecko) Chrome/27.0.1453.94 Safari/537.36");


    } else {
      for (Map.Entry<String, Object> entry : headerMaps.entrySet()) {
        request.addHeader(entry.getKey(), (String) entry.getValue());
      }
    }

    return chain.proceed(request.build());
  }


}
