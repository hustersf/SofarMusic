package com.sf.sofarmusic.online.recommend.model;

import com.google.gson.annotations.SerializedName;

/**
 * 首页推荐信息流
 */
public class Feed {

  @SerializedName("con_id")
  public String itemId;

  @SerializedName("con_title")
  public String title;

  @SerializedName("en_title")
  public String enTitle;

  @SerializedName("jump")
  public int jumpType;

  @SerializedName("pic_url")
  public String thumbUrl;

  @SerializedName("genre")
  public String genre;

  @SerializedName("author")
  public String author;

  @SerializedName("author_pic")
  public String authorThumbUrl;

  @SerializedName("song_num")
  public int songNum;

  @SerializedName("bg_color")
  public String bgColor;

}
