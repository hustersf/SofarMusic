package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class MV implements Serializable {

  @SerializedName("mv_id")
  public String mvId;

  @SerializedName("title")
  public String name;

  @SerializedName("thumbnail")
  public String thumbUrl;

  @SerializedName("artist_id")
  public String artistId;

  @SerializedName("artist")
  public String artist;

  @SerializedName("publishtime")
  public String publishTime;

  @SerializedName("play_nums")
  public String playCount;

  @SerializedName("artist_list")
  public List<Artist> artists;

}
