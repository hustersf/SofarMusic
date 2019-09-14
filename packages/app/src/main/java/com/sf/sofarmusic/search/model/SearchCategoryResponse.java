package com.sf.sofarmusic.search.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.PlayInfo;
import com.sf.sofarmusic.model.Song;

public class SearchCategoryResponse {

  @SerializedName("query")
  public String query;

  @SerializedName("syn_words")
  public String synWords;

  @SerializedName("all_info")
  public AllInfo allInfo;

  @SerializedName("playlist_info")
  public SearchPlayInfo playInfo;

  @SerializedName("album_info")
  public AlbumInfo albumInfo;

  @SerializedName("song_info")
  public SongInfo songInfo;

  @SerializedName("artist_info")
  public ArtistInfo artistInfo;

  public static final class AllInfo implements ListResponse<SearchAllInfo> {

    @SerializedName("all_list")
    public List<SearchAllInfo> searchAllInfos;

    @SerializedName("total")
    public long totalCount;

    @Override
    public boolean hasMore() {
      return searchAllInfos != null && totalCount > searchAllInfos.size();
    }

    @Override
    public List<SearchAllInfo> getItems() {
      return searchAllInfos;
    }
  }

  public static final class SearchPlayInfo implements ListResponse<PlayInfo> {

    @SerializedName("play_list")
    public List<PlayInfo> playInfos;

    @SerializedName("total")
    public long totalCount;

    @Override
    public boolean hasMore() {
      return playInfos != null && totalCount > playInfos.size();
    }

    @Override
    public List<PlayInfo> getItems() {
      return playInfos;
    }
  }

  public static final class AlbumInfo implements ListResponse<Album> {

    @SerializedName("album_list")
    public List<Album> albums;

    @SerializedName("total")
    public long totalCount;

    @Override
    public boolean hasMore() {
      return albums != null && totalCount > albums.size();
    }

    @Override
    public List<Album> getItems() {
      return albums;
    }
  }

  public static final class SongInfo implements ListResponse<Song> {

    @SerializedName("song_list")
    public List<Song> songs;

    @SerializedName("total")
    public long totalCount;

    @Override
    public boolean hasMore() {
      return songs != null && totalCount > songs.size();
    }

    @Override
    public List<Song> getItems() {
      return songs;
    }
  }
  public static final class ArtistInfo implements ListResponse<Artist> {

    @SerializedName("artist_list")
    public List<Artist> artists;

    @SerializedName("total")
    public long totalCount;

    @Override
    public boolean hasMore() {
      return artists != null && totalCount > artists.size();
    }

    @Override
    public List<Artist> getItems() {
      return artists;
    }
  }

}
