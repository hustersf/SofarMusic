package com.sf.sofarmusic.search.category.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import io.reactivex.Observable;

/**
 * 单曲
 */
public class SearchSongPageList
    extends SofarRetrofitPageList<SearchCategoryResponse.SongInfo, Song> {

  private int pageNo = 1;
  private String key;
  private int type;

  public SearchSongPageList(String key, int type) {
    this.key = key;
    this.type = type;
  }

  @Override
  protected Observable<SearchCategoryResponse.SongInfo> onCreateRequest() {
    return ApiProvider.getMusicApiService().searchAll(key, pageNo, 10, type)
        .map(resultResponse -> resultResponse.result)
        .map(searchCategoryResponse -> searchCategoryResponse.songInfo);
  }
}
