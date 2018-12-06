package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

/**
 * 存放歌曲信息
 */
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

  @SerializedName("bitrate")
  public Bitrate mBitrate;

  public static class Bitrate {

    @SerializedName("show_link")
    public String mShowUrl;

    @SerializedName("file_link")
    public String mFileUrl;

  }

}
