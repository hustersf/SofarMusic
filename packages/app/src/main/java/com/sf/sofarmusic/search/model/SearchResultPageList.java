package com.sf.sofarmusic.search.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.Song;
import com.sf.utility.CollectionUtil;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class SearchResultPageList
    extends SofarRetrofitPageList<SearchResultResponse, SearchInfo> {

  private String key;

  public SearchResultPageList(String key) {
    this.key = key;
  }

  public void switchKey(String key) {
    this.key = key;
  }

  @Override
  protected Observable<SearchResultResponse> onCreateRequest() {
    return ApiProvider.getMusicApiService().searchResult(key).doOnNext(response -> {
      List<SearchInfo> list = new ArrayList<>();
      String[] ss = response.order.split(",");
      int count = 3;
      for (int i = 0; i < ss.length; i++) {
        if (SearchInfo.ARTIST.equals(ss[i]) && !CollectionUtil.isEmpty(response.artists)) {
          for (int j = 0; j < response.artists.size() && j < count; j++) {
            Artist artist = response.artists.get(j);
            SearchInfo info = new SearchInfo();
            info.linkType = SearchInfo.ARTIST_LINK_TYPE;
            info.linkUrl = artist.artistId;
            info.word = artist.name;
            list.add(info);
          }
        } else if (SearchInfo.SONG.equals(ss[i]) && !CollectionUtil.isEmpty(response.songs)) {
          for (int j = 0; j < response.songs.size() && j < count; j++) {
            Song song = response.songs.get(j);
            SearchInfo info = new SearchInfo();
            info.linkType = SearchInfo.SONG_LINK_TYPE;
            info.linkUrl = song.songId;
            info.word = song.name + "-" + song.author;
            list.add(info);
          }
        } else if (SearchInfo.ALBUM.equals(ss[i]) && !CollectionUtil.isEmpty(response.albums)) {
          for (int j = 0; j < response.albums.size() && j < count; j++) {
            Album album = response.albums.get(j);
            SearchInfo info = new SearchInfo();
            info.linkType = SearchInfo.ALBUM_LINK_TYPE;
            info.linkUrl = album.albumId;
            info.word = album.name + "-" + album.author;
            list.add(info);
          }
        }
        count--;
      }
      response.searchInfos = list;
    });
  }
}
