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
import com.sf.widget.recyclerview.RecyclerHeaderFooterAdapter2;
import com.sf.widget.tip.TipHelper;
import java.util.List;

/**
 * 封装通用的列表加载页
 */
public abstract class RecyclerFragment<MODEL> extends BaseFragment implements PageListObserver {

  private SwipeRefreshLayout mRefreshLayout;
  private View mRootView;

  protected RecyclerView mRecyclerView;
  private RecyclerAdapter<MODEL> mOriginAdapter;
  private RecyclerHeaderFooterAdapter2 mHeaderFooterAdapter;

  private PageList<?, MODEL> mPageList;
  private TipHelper mTipHelper;
  private RecyclerView.OnScrollListener mAutoLoadEventDetector;

  protected int getLayoutResId() {
    return R.layout.base_recycler_fragment;
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

  protected RecyclerView.OnScrollListener onCreateAutoLoadEventDetector() {
    return new AutoLoadEventDetector(this, getPageList());
  }

  protected List<View> onCreateHeaderViews() {
    return null;
  }

  protected List<View> onCreateFooterViews() {
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
    initRefreshLayout();

    mPageList = onCreatePageList();
    mPageList.registerObserver(this);
    mTipHelper = onCreateTipHelper();
    mAutoLoadEventDetector = onCreateAutoLoadEventDetector();
    mRecyclerView.addOnScrollListener(mAutoLoadEventDetector);
    if (autoLoad()) {
      refresh();
    }
  }

  private void initRecyclerView() {
    mRecyclerView.setLayoutManager(onCreateLayoutManager());
    mOriginAdapter = onCreateAdapter();
    mHeaderFooterAdapter = new RecyclerHeaderFooterAdapter2(mOriginAdapter, onCreateHeaderViews(),
        onCreateFooterViews());
    mRecyclerView.setAdapter(mHeaderFooterAdapter);
  }

  private void initRefreshLayout() {
    if (mRefreshLayout != null) {
      mRefreshLayout.setOnRefreshListener(() -> {
        refresh();
      });
    }
  }


  protected void refresh() {
    if (mPageList != null) {
      mPageList.refresh();
    }
  }

  @Override
  public void onStartLoading(boolean firstPage, boolean cache) {
    mTipHelper.hideError();
    mTipHelper.showLoading(firstPage, cache);
  }

  @Override
  public void onFinishLoading(boolean firstPage, boolean cache) {
    if (mRefreshLayout != null) {
      mRefreshLayout.setRefreshing(false);
    }

    mTipHelper.hideLoading();
    mTipHelper.hideError();
    mTipHelper.hideEmpty();

    if (mPageList.isEmpty()) {
      mTipHelper.showEmpty();
    } else if (mPageList.hasMore()) {
      mTipHelper.hideNoMoreTips();
    } else {
      mTipHelper.showNoMoreTips();
    }

    mOriginAdapter.setList(mPageList.getItems());
    mOriginAdapter.notifyDataSetChanged();
  }

  @Override
  public void onError(boolean firstPage, Throwable throwable) {
    if (mRefreshLayout != null) {
      mRefreshLayout.setRefreshing(false);
    }

    mTipHelper.hideLoading();
    mTipHelper.showError(firstPage, throwable);
  }

  public RecyclerAdapter<MODEL> getOriginAdapter() {
    return mOriginAdapter;
  }

  public RecyclerHeaderFooterAdapter2 getHeaderFooterAdapter() {
    return mHeaderFooterAdapter;
  }

  public PageList<?, MODEL> getPageList() {
    return mPageList;
  }

  public RecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  public TipHelper getTipHelper() {
    return mTipHelper;
  }

  public void setRefreshEnable(boolean enable) {
    if (mRefreshLayout != null) {
      mRefreshLayout.setEnabled(enable);
    }
  }

  protected boolean autoLoad() {
    return true;
  }

}
