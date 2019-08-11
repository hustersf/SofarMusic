package com.sf.sofarmusic.search;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.online.rank.RankDetailAdapter;
import com.sf.sofarmusic.online.rank.model.RankDetailPageList;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 搜索结果（根据键盘输入值动态变化内容）
 */
public class SearchResultFragment extends RecyclerFragment {

  @Override
  protected RecyclerAdapter onCreateAdapter() {
    return new RankDetailAdapter();
  }

  @Override
  protected PageList onCreatePageList() {
    return new RankDetailPageList(20);
  }
}
