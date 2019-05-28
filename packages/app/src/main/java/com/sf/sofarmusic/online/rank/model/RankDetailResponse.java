package com.sf.sofarmusic.online.rank.model;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.sofarmusic.model.Song;

import java.util.List;

/**
 * 榜单歌曲列表
 */
public class RankDetailResponse implements ListResponse<Song> {

  @SerializedName("song_list")
  public List<Song> songs;

  @SerializedName("billboard")
  public BillBoard billboard;

  @Override
  public List<Song> getItems() {
    return songs;
  }
}
