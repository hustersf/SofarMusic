package com.sf.sofarmusic.model.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.sf.sofarmusic.model.Song;

public class RankSongsResponse {

  @SerializedName("song_list")
  public List<Song> mSongList;

  @SerializedName("billboard")
  public BillBoard mBillBoard;


  public static class BillBoard {

    @SerializedName("billboard_type")
    public String mType;

    @SerializedName("name")
    public String mName;

    @SerializedName("billboard_songnum")
    public int mCount;

    @SerializedName("pic_s260")
    public String mCoverUrl;

    @SerializedName("pic_s444")
    public String mBigCoverUrl;

  }
}
