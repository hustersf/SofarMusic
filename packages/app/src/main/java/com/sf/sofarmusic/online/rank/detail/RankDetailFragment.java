package com.sf.sofarmusic.online.rank.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.sf.base.mvp.Presenter;
import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.RankDetailAdapter;
import com.sf.sofarmusic.online.rank.model.RankDetailPageList;
import com.sf.sofarmusic.online.rank.presenter.RankDetailHeadPresenter;
import com.sf.sofarmusic.online.rank.presenter.RankDetailTitlePresenter;
import com.sf.widget.recyclerview.RecyclerAdapter;

public class RankDetailFragment extends RecyclerFragment {

  private int type;
  private RankDetailPageList pageList;
  private Presenter presenter = new Presenter();

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_rank_detail;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    presenter.destroy();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    type = getArguments().getInt(RankDetailActivity.KEY_RANK_TYPE);
    super.onViewCreated(view, savedInstanceState);
    setRefreshEnable(false);

    presenter.add(new RankDetailTitlePresenter());
    presenter.add(new RankDetailHeadPresenter());
    presenter.create(view);
  }

  @Override
  protected RecyclerAdapter onCreateAdapter() {
    return new RankDetailAdapter();
  }

  @Override
  protected PageList onCreatePageList() {
    pageList = new RankDetailPageList(type);
    return pageList;
  }

  @Override
  public void onFinishLoading(boolean firstPage, boolean cache) {
    super.onFinishLoading(firstPage, cache);
    presenter.bind(pageList.getPageResponse(), this);
  }
}
