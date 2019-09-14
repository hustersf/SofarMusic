package com.sf.sofarmusic.search.category;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.model.PlayInfo;
import com.sf.sofarmusic.search.category.model.SearchPlayPageList;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 歌单
 */
public class SearchPlayItemFragment extends RecyclerFragment<PlayInfo> {

  private SearchCategoryResponse response;
  private String key;

  @Override
  protected RecyclerAdapter<PlayInfo> onCreateAdapter() {
    return new SearchPlayAdapter();
  }

  @Override
  protected PageList<?, PlayInfo> onCreatePageList() {
    return new SearchPlayPageList(key, SearchAllInfo.TYPE_PLAY);
  }

  public void setData(SearchCategoryResponse response) {
    this.response = response;
    if (response != null) {
      key = response.query;
    }
  }
}
