package com.sf.sofarmusic.online.rank.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Song;

import io.reactivex.Observable;

public class RankDetailPageList extends SofarRetrofitPageList<RankDetailResponse, Song> {

  private int type;

  public RankDetailPageList(int type) {
    this.type = type;
  }

  @Override
  protected Observable<RankDetailResponse> onCreateRequest() {
    return ApiProvider.getMusicApiService().rankSongs(type, 0, 100);
  }
}
