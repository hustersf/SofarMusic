package com.sf.sofarmusic.online.recommend;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.online.recommend.model.FeedGroup;
import com.sf.sofarmusic.online.recommend.model.RecommendPageList;
import com.sf.widget.recyclerview.RecyclerAdapter;


/**
 * 推荐页
 */
public class RecommendFragment extends RecyclerFragment<FeedGroup> {


  @Override
  protected RecyclerAdapter<FeedGroup> onCreateAdapter() {
    return new FeedGroupAdapter();
  }

  @Override
  protected PageList<?, FeedGroup> onCreatePageList() {
    return new RecommendPageList();
  }
}
