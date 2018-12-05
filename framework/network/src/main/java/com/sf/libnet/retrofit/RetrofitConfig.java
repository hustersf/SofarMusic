package com.sf.libnet.retrofit;

import com.google.gson.Gson;

import java.util.Map;

import okhttp3.OkHttpClient;

public interface RetrofitConfig {

  Params buildParams();

  String buildBaseUrl();

  Gson buildGson();

  OkHttpClient buildClient();


  interface Params {

    Map<String, String> getHeaderParams();

    Map<String, String> getUrlParams();

    Map<String, String> getBodyParams();
  }

}
