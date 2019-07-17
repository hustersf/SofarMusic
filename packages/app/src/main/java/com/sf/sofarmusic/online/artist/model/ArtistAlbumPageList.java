package com.sf.sofarmusic.online.artist.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Artist;
import io.reactivex.Observable;

public class ArtistAlbumPageList extends SofarRetrofitPageList<ArtistAlbumResponse, Album> {

  private Artist artist;

  public ArtistAlbumPageList(Artist artist) {
    this.artist = artist;
  }

  @Override
  protected Observable<ArtistAlbumResponse> onCreateRequest() {
    int offset = isFirstPage() ? 0 : getItems().size();
    int limit = isFirstPage() ? 20 : 10;
    return ApiProvider.getMusicApiService().artistAlbumList(artist.tingUid, offset, limit);
  }
}
