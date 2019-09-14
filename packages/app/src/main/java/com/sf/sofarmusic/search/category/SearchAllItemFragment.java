package com.sf.sofarmusic.search.category;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.search.category.model.SearchAllPageList;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 全部
 */
public class SearchAllItemFragment extends RecyclerFragment<SearchAllInfo> {

  private SearchCategoryResponse response;
  private String key;

  @Override
  protected RecyclerAdapter<SearchAllInfo> onCreateAdapter() {
    return new SearchAllAdapter();
  }

  @Override
  protected PageList<?, SearchAllInfo> onCreatePageList() {
    return new SearchAllPageList(key,SearchAllInfo.TYPE_NONE);
  }

  public void setData(SearchCategoryResponse response) {
    this.response = response;
    if (response != null) {
      key = response.query;
    }
  }
}
