package com.sf.sofarmusic.online.artist.model;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.sofarmusic.model.Song;

import java.util.List;

public class ArtistSongResponse implements ListResponse<Song> {

  @SerializedName("songlist")
  public List<Song> songs;

  @SerializedName("havemore")
  public int haveMore;

  @Override
  public boolean hasMore() {
    return haveMore == 1;
  }

  @Override
  public List<Song> getItems() {
    return songs;
  }
}
