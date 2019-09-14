package com.sf.sofarmusic.search.category.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import io.reactivex.Observable;

/**
 * 歌单
 */
public class SearchAllPageList
    extends SofarRetrofitPageList<SearchCategoryResponse.AllInfo, SearchAllInfo> {

  private int pageNo = 1;
  private String key;
  private int type;

  public SearchAllPageList(String key, int type) {
    this.key = key;
    this.type = type;
  }

  @Override
  protected Observable<SearchCategoryResponse.AllInfo> onCreateRequest() {
    return ApiProvider.getMusicApiService().searchAll(key, pageNo, 10, type)
        .map(resultResponse -> resultResponse.result)
        .map(searchCategoryResponse -> searchCategoryResponse.allInfo);
  }
}
