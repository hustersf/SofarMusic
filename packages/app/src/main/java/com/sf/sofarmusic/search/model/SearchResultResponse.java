package com.sf.sofarmusic.search.model;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.Song;
import java.util.List;

public class SearchResultResponse implements ListResponse<SearchInfo> {

  @SerializedName("order")
  public String order = "artist,song,album";

  @SerializedName("song")
  public List<Song> songs;

  @SerializedName("album")
  public List<Album> albums;

  @SerializedName("artist")
  public List<Artist> artists;

  public List<SearchInfo> searchInfos;

  @Override
  public boolean hasMore() {
    return false;
  }

  @Override
  public List<SearchInfo> getItems() {
    return searchInfos;
  }
}
