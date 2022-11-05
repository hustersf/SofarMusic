package com.sf.sofarmusic.online.rank;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.online.rank.model.Rank;
import com.sf.sofarmusic.online.rank.model.RankPageList;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 榜单页面
 */
public class RankFragment extends RecyclerFragment<Rank> {

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  protected RecyclerAdapter onCreateAdapter() {
    return new RankAdapter();
  }

  @Override
  protected PageList onCreatePageList() {
    return new RankPageList();
  }
}
