package com.sf.sofarmusic.online;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.BaseFragment;
import com.sf.base.network.page.PageList;
import com.sf.base.network.page.PageListObserver;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.RankAdapter2;
import com.sf.sofarmusic.online.rank.model.Rank;
import com.sf.sofarmusic.online.rank.model.RankPageList;

/**
 * Created by sufan on 16/11/9.
 * 电台
 */

public class RadioFragment extends BaseFragment implements PageListObserver {

  private SwipeRefreshLayout mRefreshLayout;
  private View mRootView;

  private RecyclerView mRecyclerView;
  private PageList<?, Rank> mPageList;
  private RankAdapter2 mAdapter2 = new RankAdapter2();

  protected int getLayoutResId() {
    return R.layout.base_recycler_fragment;
  }

  protected RecyclerView.LayoutManager onCreateLayoutManager() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    return layoutManager;
  }

  protected PageList<?, Rank> onCreatePageList() {
    return new RankPageList();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mRootView = inflater.inflate(getLayoutResId(), container, false);
    mRefreshLayout = mRootView.findViewById(com.sf.base.R.id.refresh_layout);
    mRecyclerView = mRootView.findViewById(com.sf.base.R.id.recycler_view);
    return mRootView;
  }



  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecyclerView();
    initRefreshLayout();

    mPageList = onCreatePageList();
    mPageList.registerObserver(this);

    refresh();
  }

  private void initRecyclerView() {
    mRecyclerView.setLayoutManager(onCreateLayoutManager());
    mAdapter2 = new RankAdapter2();
    mRecyclerView.setAdapter(mAdapter2);
  }

  private void initRefreshLayout() {
    mRefreshLayout.setOnRefreshListener(() -> {
      refresh();
    });
  }


  private void refresh() {
    mPageList.refresh();
  }

  @Override
  public void onStartLoading(boolean firstPage, boolean cache) {

  }

  @Override
  public void onFinishLoading(boolean firstPage, boolean cache) {
    mRefreshLayout.setRefreshing(false);
    mAdapter2.setList(mPageList.getItems());
    mAdapter2.notifyDataSetChanged();
  }

  @Override
  public void onError(boolean firstPage, Throwable throwable) {


  }
}
