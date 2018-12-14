package com.sf.sofarmusic.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 存放歌手信息
 */
public class Artist {

  @SerializedName("image")
  public List<ArtistImage> mArtistImages;

  public static class ArtistImage {
    @SerializedName("#text")
    public String mImageUrl;
  }
}
