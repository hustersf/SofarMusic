package com.sf.sofarmusic.online.artist.model;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Song;

import java.util.List;

public class AlbumDetailResponse implements ListResponse<Song> {

  @SerializedName("albumInfo")
  public Album album;

  @SerializedName("songlist")
  public List<Song> songs;

  @Override
  public boolean hasMore() {
    return false;
  }

  @Override
  public List<Song> getItems() {
    return songs;
  }
}
