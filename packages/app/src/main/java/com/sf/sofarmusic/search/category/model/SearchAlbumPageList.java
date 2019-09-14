package com.sf.sofarmusic.search.category.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import io.reactivex.Observable;

/**
 * 专辑
 */
public class SearchAlbumPageList
    extends SofarRetrofitPageList<SearchCategoryResponse.AlbumInfo, Album> {

  private int pageNo = 1;
  private String key;
  private int type;

  public SearchAlbumPageList(String key, int type) {
    this.key = key;
    this.type = type;
  }

  @Override
  protected Observable<SearchCategoryResponse.AlbumInfo> onCreateRequest() {
    return ApiProvider.getMusicApiService().searchAll(key, pageNo, 10, type)
        .map(resultResponse -> resultResponse.result)
        .map(searchCategoryResponse -> searchCategoryResponse.albumInfo);
  }
}
