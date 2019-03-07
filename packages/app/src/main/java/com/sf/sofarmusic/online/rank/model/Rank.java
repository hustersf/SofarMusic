package com.sf.sofarmusic.online.rank.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 榜单信息
 */
public class Rank {

  @SerializedName("name")
  public String name;

  @SerializedName("type")
  public int type;

  @SerializedName("count")
  public int count;

  @SerializedName("comment")
  public String comment;

  @SerializedName("pic_s260")
  public String squareThumbUrl;

  @SerializedName("pic_s444")
  public String rectangleThumbUrl;

  @SerializedName("content")
  public List<Song> songs;

}
