package com.sf.libnet.interceptor;

import java.io.IOException;
import java.util.Map;

import com.sf.libnet.retrofit.RetrofitConfig;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 为请求添加统一的请求头
 */
public class HeadersInterceptor implements Interceptor {

  private RetrofitConfig.Params mParams;

  public HeadersInterceptor(RetrofitConfig.Params params) {
    mParams = params;
  }


  @Override
  public Response intercept(Chain chain) throws IOException {
    // 添加header参数
    Map<String, String> headers = mParams.getHeaderParams();
    Request request = chain.request();
    Headers.Builder builder = request.headers().newBuilder();
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      builder.add(entry.getKey(), entry.getValue());
    }
    request = request.newBuilder().headers(builder.build()).build();
    return chain.proceed(request);
  }
}
