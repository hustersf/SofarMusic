package com.sf.libnet.interceptor;

import com.sf.libnet.multipart.SofarResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ContentLengthInterceptor implements Interceptor {

  private static final int UNKNOWN_LENGTH = -1;
  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    // 如果服务器没有返回 content-length 的时候，我们自己计算
    if (response.body().contentLength() == UNKNOWN_LENGTH) {
      Response.Builder responseBuilder = response.newBuilder();
      responseBuilder.body(new SofarResponseBody(response.body()));
      return responseBuilder.build();
    } else {
      return response;
    }
  }
}
