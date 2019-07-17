package com.sf.sofarmusic.online.artist.model;

        import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
        import com.sf.sofarmusic.api.ApiProvider;
        import com.sf.sofarmusic.model.Artist;
        import com.sf.sofarmusic.model.MV;

        import io.reactivex.Observable;

public class ArtistMVPageList extends SofarRetrofitPageList<ArtistMVResponse, MV> {

  private Artist artist;

  public ArtistMVPageList(Artist artist) {
    this.artist = artist;
  }


  @Override
  protected Observable<ArtistMVResponse> onCreateRequest() {
    int offset = isFirstPage() ? 0 : getItems().size();
    int limit = isFirstPage() ? 20 : 10;
    return ApiProvider.getMusicApiService().artistMVList(artist.tingUid, offset, limit)
            .map(resultResponse -> resultResponse.result);
  }
}
