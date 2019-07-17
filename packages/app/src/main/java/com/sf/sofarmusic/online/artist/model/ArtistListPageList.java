package com.sf.sofarmusic.online.artist.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Artist;

import io.reactivex.Observable;

public class ArtistListPageList extends SofarRetrofitPageList<ArtistResponse, Artist> {

  ArtistGroup artistGroup;

  public ArtistListPageList(ArtistGroup artistGroup) {
    this.artistGroup = artistGroup;
  }

  @Override
  protected Observable<ArtistResponse> onCreateRequest() {
    int offset = isFirstPage() ? 0 : getItems().size();
    int limit = isFirstPage() ? 20 : 10;
    return ApiProvider.getMusicApiService().artistList(artistGroup.area, artistGroup.sex, offset,
        limit);
  }

}
