package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 排行榜
 */
public class Rank {

  @SerializedName("type")
  public String mType;

  @SerializedName("name")
  public String mName;

  @SerializedName("pic_s260")
  public String mCoverUrl;

  @SerializedName("pic_s444")
  public String mBigCoverUrl;

  @SerializedName("content")
  public List<Song> mSongs;

}
