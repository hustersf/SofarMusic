package com.sf.base.recycler;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.BaseFragment;
import com.sf.base.R;
import com.sf.base.network.page.PageList;
import com.sf.base.network.page.PageListObserver;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerHeaderFooterAdapter;
import com.sf.widget.tip.TipHelper;

/**
 * 封装通用的列表加载页
 */
public abstract class RecyclerFragment<MODEL> extends BaseFragment implements PageListObserver {

  private SwipeRefreshLayout mRefreshLayout;
  private View mRootView;

  private RecyclerView mRecyclerView;
  private RecyclerAdapter<MODEL> mOriginAdapter;
  private RecyclerHeaderFooterAdapter mHeaderFooterAdapter;

  private PageList<?, MODEL> mPageList;
  private TipHelper mTipHelper;

  protected int getLayoutResId() {
    return R.layout.layout_base_recycler_fragment;
  }

  protected RecyclerView.LayoutManager onCreateLayoutManager() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    return layoutManager;
  }

  protected abstract RecyclerAdapter<MODEL> onCreateAdapter();

  protected abstract PageList<?, MODEL> onCreatePageList();

  protected TipHelper onCreateTipHelper() {
    return new RecyclerViewTipHelper(this);
  }


  protected View onCreateHeaderView() {
    return null;
  }

  protected View onCreateFooterView() {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mPageList.unRegisterObserver(this);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mRootView = inflater.inflate(getLayoutResId(), container, false);
    mRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
    mRecyclerView = mRootView.findViewById(R.id.recycler_view);
    return mRootView;
  }


  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecyclerView();

    mPageList = onCreatePageList();
    mPageList.registerObserver(this);
    mTipHelper = onCreateTipHelper();

    refresh();
  }

  private void initRecyclerView() {
    mRecyclerView.setLayoutManager(onCreateLayoutManager());
    mOriginAdapter = onCreateAdapter();
    mHeaderFooterAdapter = new RecyclerHeaderFooterAdapter(mOriginAdapter);
    mHeaderFooterAdapter.addHeaderView(onCreateHeaderView());
    mHeaderFooterAdapter.addFooterView(onCreateFooterView());
    mRecyclerView.setAdapter(mHeaderFooterAdapter);
  }


  private void refresh() {
    mPageList.refresh();
  }

  @Override
  public void onStartLoading(boolean firstPage, boolean cache) {
    mTipHelper.hideError();
    mTipHelper.showLoading(firstPage, cache);
  }

  @Override
  public void onFinishLoading(boolean firstPage, boolean cache) {
    mTipHelper.hideLoading();
    mTipHelper.hideError();

    if (mPageList.isEmpty()) {
      mTipHelper.showEmpty();
    }

    mOriginAdapter.setList(mPageList.getItems());
    mOriginAdapter.notifyDataSetChanged();
  }

  @Override
  public void onError(boolean firstPage, Throwable throwable) {
    mTipHelper.hideLoading();
    mTipHelper.showError(firstPage, throwable);
  }

  public RecyclerAdapter<MODEL> getOriginAdapter() {
    return mOriginAdapter;
  }

  public PageList<?, MODEL> getPageList() {
    return mPageList;
  }

  public RecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  public void setRefreshEnable(boolean enable) {
    mRefreshLayout.setEnabled(enable);
  }

}
