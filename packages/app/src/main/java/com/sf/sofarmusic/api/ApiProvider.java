package com.sf.sofarmusic.api;

import com.sf.libnet.retrofit.RetrofitFactory;
import com.sf.libnet.retrofit.SofarRetrofitConfig;

public class ApiProvider {

  private static class MusicApiServiceHolderClass {
    private final static MusicApiService INSTANCE =
        RetrofitFactory.newBuilder(new SofarRetrofitConfig()).build().create(MusicApiService.class);
  }

  public static MusicApiService getMusicApiService() {
    return MusicApiServiceHolderClass.INSTANCE;
  }
}
