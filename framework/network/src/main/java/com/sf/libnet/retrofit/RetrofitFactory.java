package com.sf.libnet.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitFactory {

  public static Retrofit.Builder newBuilder(final RetrofitConfig config) {
    return new Retrofit.Builder()
        .client(config.buildClient())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(config.buildBaseUrl());
  }
}
