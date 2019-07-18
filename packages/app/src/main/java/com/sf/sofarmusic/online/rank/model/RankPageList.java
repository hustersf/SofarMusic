package com.sf.sofarmusic.online.rank.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import java.util.List;
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

  @Override
  public void onLoadItemFromResponse(RankResponse rankResponse, List<Rank> items) {
    super.onLoadItemFromResponse(rankResponse, items);
    for (Rank rank : items) {
      if (rank.type == 500) {
        items.remove(rank);
        break;
      }
    }
  }
}
