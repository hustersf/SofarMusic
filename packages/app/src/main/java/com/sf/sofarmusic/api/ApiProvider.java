package com.sf.sofarmusic.api;

import com.sf.libnet.retrofit.RetrofitFactory;

public class ApiProvider {

  private static class MusicApiServiceHolderClass {
    private final static MusicApiService INSTANCE =
        RetrofitFactory.newBuilder(new MusicRetrofitConfig()).build().create(MusicApiService.class);
  }

  public static MusicApiService getMusicApiService() {
    return MusicApiServiceHolderClass.INSTANCE;
  }
}
