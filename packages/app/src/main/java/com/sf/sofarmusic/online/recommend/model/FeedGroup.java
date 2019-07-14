package com.sf.sofarmusic.online.recommend.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 推荐分组
 */
public class FeedGroup {

  public static final int STYLE_SCENE = 33;
  public static final int STYLE_RECOMMEND = 34;
  public static final int STYLE_UNSUPPORT = 10001;

  @SerializedName("id")
  public String groupId;

  @SerializedName("style")
  public int style; // 决定UI样式

  @SerializedName("title")
  public String title;

  @SerializedName("title_more")
  public String titleMore;

  @SerializedName("result")
  public List<Feed> feeds;


}
