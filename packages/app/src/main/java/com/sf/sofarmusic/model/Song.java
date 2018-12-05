package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

public class Song {

  @SerializedName("song_id")
  public String mId;

  @SerializedName("title")
  public String mName;

  @SerializedName("author")
  public String mAuthor;

  @SerializedName("pic_small")
  public String mCoverUrl;

  @SerializedName("pic_big")
  public String mBigCoverUrl;

  @SerializedName("lrclink")
  public String mLrcLink;
}
