package com.sf.sofarmusic.model.response;

import com.google.gson.annotations.SerializedName;
import com.sf.sofarmusic.model.Song;

import java.util.List;

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

    @SerializedName("pic_s260")
    public String mCoverUrl;

    @SerializedName("pic_s444")
    public String mBigCoverUrl;

  }
}
