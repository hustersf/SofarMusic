package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

/**
 * 存放歌曲信息
 */
public class Song {

  //当前歌曲是否正在播放
  public boolean play;

  @SerializedName("song_id")
  public String songId;

  @SerializedName("title")
  public String name;

  @SerializedName("author")
  public String author;

  @SerializedName("album_id")
  public String albumId;

  @SerializedName("album_title")
  public String albumTitle;

  @SerializedName("pic_small")
  public String smallThumbUrl;

  @SerializedName("pic_big")
  public String bigThumbUrl;

  @SerializedName("lrclink")
  public String lrcLink;

  @SerializedName("songurl")
  public SongUrl songUrl;

}
