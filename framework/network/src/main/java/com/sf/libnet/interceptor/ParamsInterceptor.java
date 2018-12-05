package com.sf.libnet.interceptor;

import com.sf.libnet.retrofit.RetrofitConfig;
import com.sf.libnet.retrofit.SofarParams;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 为请求添加统一的参数，包括
 * query参数
 * body参数，仅适用于post请求
 */
public class ParamsInterceptor implements Interceptor {

  private RetrofitConfig.Params mParams;

  public ParamsInterceptor(RetrofitConfig.Params params) {
    mParams = params;
  }


  @Override
  public Response intercept(Chain chain) throws IOException {
    Map<String, String> urlParams = mParams.getUrlParams();
    Map<String, String> bodyParams = mParams.getBodyParams();
    Request request = chain.request();
    Request.Builder requestBuilder = request.newBuilder();

    // 统一添加query参数
    HttpUrl.Builder urlBuilder = request.url().newBuilder();
    for (Map.Entry<String, String> entry : urlParams.entrySet()) {
      if (request.url().queryParameter(entry.getKey()) == null) {
        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
      } else {
        urlBuilder.setQueryParameter(entry.getKey(), entry.getValue());
      }
    }
    requestBuilder.url(urlBuilder.build());


    // 统一添加body参数,只适用于post请求
    boolean post = "POST".equalsIgnoreCase(request.method());
    if (post && request.body() != null) {
      // form表单
      if (request.body() instanceof FormBody) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        FormBody formBody = (FormBody) request.body();
        // 添加公共参数
        for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
          formBuilder.add(entry.getKey(), entry.getValue());
        }
        // 把原来的参数添加到新的构造器
        for (int i = 0; i < formBody.size(); i++) {
          formBuilder.add(formBody.encodedName(i), formBody.encodedValue(i));
        }
        requestBuilder.post(formBuilder.build());
      }

      // 后续可增加json格式的body和文件类型的body
    }

    // 构造出最新的request
    request = requestBuilder.build();
    return chain.proceed(request);
  }
}
