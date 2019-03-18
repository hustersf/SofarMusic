package com.sf.sofarmusic.online.rank.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;

import io.reactivex.Observable;

public class RankPageList extends SofarRetrofitPageList<RankResponse, Rank> {

  @Override
  protected Observable<RankResponse> onCreateRequest() {
    return ApiProvider.getMusicApiService().rankList();
  }

  @Override
  protected boolean allowDuplicate() {
    return false;
  }
}
