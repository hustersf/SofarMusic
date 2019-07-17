package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Album implements Serializable {

  @SerializedName("album_id")
  public String albumId;

  @SerializedName("title")
  public String name;

  @SerializedName("author")
  public String author;

  @SerializedName("songs_total")
  public long songCount;

  @SerializedName("publishcompany")
  public String publishCompany;

  @SerializedName("publishtime")
  public String publishTime;

  @SerializedName("artist_ting_uid")
  public String tingUid;

  @SerializedName("artist_id")
  public String artistId;

  @SerializedName("pic_small")
  public String smallThumbUrl;

  @SerializedName("pic_big")
  public String bigThumbUrl;
}
