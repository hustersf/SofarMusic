package com.sf.sofarmusic.online.rank.model;

import com.google.gson.annotations.SerializedName;

public class BillBoard {

  //主题颜色
  public int mainColor;

  @SerializedName("billboard_type")
  public String type;

  @SerializedName("name")
  public String name;

  @SerializedName("billboard_songnum")
  public int count;

  @SerializedName("pic_s260")
  public String coverUrl;

  @SerializedName("pic_s444")
  public String bigCoverUrl;

}
