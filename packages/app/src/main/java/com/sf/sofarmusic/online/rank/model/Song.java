package com.sf.sofarmusic.online.rank.model;

import com.google.gson.annotations.SerializedName;

/**
 * 歌曲信息
 */
public class Song {

  @SerializedName("song_id")
  public String songId;

  @SerializedName("title")
  public String title;

  @SerializedName("author")
  public String author;

  @SerializedName("album_id")
  public String albumId;

  @SerializedName("album_title")
  public String albumTitle;

  @SerializedName("pic_small")
  public String smallThumbUrl;

  @SerializedName("pic_big")
  public String bigThurmUrl;
}
