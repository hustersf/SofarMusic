package com.sf.sofarmusic.online.artist.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Song;

public class ArtistAlbumResponse implements ListResponse<Album> {

  @SerializedName("albumlist")
  public List<Album> albums;

  @SerializedName("havemore")
  public int haveMore;

  @Override
  public boolean hasMore() {
    return haveMore == 1;
  }

  @Override
  public List<Album> getItems() {
    return albums;
  }
}
