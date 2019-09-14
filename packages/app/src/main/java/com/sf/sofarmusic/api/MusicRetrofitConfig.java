package com.sf.sofarmusic.api;

import com.google.gson.Gson;
import com.sf.libnet.retrofit.SofarRetrofitConfig;

public class MusicRetrofitConfig extends SofarRetrofitConfig {

  @Override
  public Gson buildGson() {
    return MusicGsons.GSON;
  }
}
