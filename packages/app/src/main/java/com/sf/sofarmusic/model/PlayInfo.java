package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

/**
 * 歌单信息
 */
public class PlayInfo {

  @SerializedName("firstSongid")
  public String firstSongId;

  @SerializedName("song_num")
  public long songNum;

  @SerializedName("diy_title")
  public String title;

  @SerializedName("diy_id")
  public String diyId;

  @SerializedName("diy_pic")
  public String thumbUrl;

  @SerializedName("style_tag")
  public String styleTag;

  @SerializedName("userinfo")
  public UserInfo userInfo;

}
