package com.sf.sofarmusic.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.search.model.SearchInfo;
import com.sf.sofarmusic.search.model.SearchResultPageList;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 搜索结果（根据键盘输入值动态变化内容）
 */
public class SearchResultFragment extends RecyclerFragment<SearchInfo> {

  private String key;
  private boolean created;

  @Override
  protected RecyclerAdapter<SearchInfo> onCreateAdapter() {
    return new SearchResultAdapter();
  }

  @Override
  protected PageList<?, SearchInfo> onCreatePageList() {
    return new SearchResultPageList(key);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    created = true;
    if (!TextUtils.isEmpty(key)) {
      refresh();
    }
  }

  /**
   * 刷新
   */
  public void switchWord(String word) {
    key = word;
    if (created && getPageList() instanceof SearchResultPageList) {
      ((SearchResultPageList) getPageList()).switchKey(key);
      refresh();
    }
  }

  @Override
  protected boolean autoLoad() {
    return false;
  }
}
