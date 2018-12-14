package com.sf.demo.api;

import com.sf.libnet.retrofit.RetrofitFactory;
import com.sf.libnet.retrofit.SofarRetrofitConfig;

public class ApiProvider {

  private static class MusicApiServiceHolderClass {
    private final static DemoApiService INSTANCE =
        RetrofitFactory.newBuilder(new SofarRetrofitConfig()).build().create(DemoApiService.class);
  }

  public static DemoApiService getDemoApiService() {
    return MusicApiServiceHolderClass.INSTANCE;
  }
}
