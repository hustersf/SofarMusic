package com.sf.sofarmusic.online.recommend.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;

import io.reactivex.Observable;

public class RecommendPageList extends SofarRetrofitPageList<RecommendResponse, FeedGroup> {

  @Override
  protected Observable<RecommendResponse> onCreateRequest() {
    return ApiProvider.getMusicApiService().recommendList();
  }
}
