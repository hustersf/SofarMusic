package com.sf.sofarmusic.model.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LrcResponse {

  @SerializedName("lrcys_list")
  public List<Lrc> mLrcList;

  public static class Lrc {
    @SerializedName("lrclink")
    public String mLrcLink;
  }
}
