package com.sf.sofarmusic.online.artist.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.Song;
import io.reactivex.Observable;

public class ArtistSongPageList extends SofarRetrofitPageList<ArtistSongResponse, Song> {

  private Artist artist;

  public ArtistSongPageList(Artist artist) {
    this.artist = artist;
  }

  @Override
  protected Observable<ArtistSongResponse> onCreateRequest() {
    int offset = isFirstPage() ? 0 : getItems().size();
    int limit = isFirstPage() ? 20 : 10;
    return ApiProvider.getMusicApiService().artistSongList(artist.tingUid, offset, limit);
  }
}
