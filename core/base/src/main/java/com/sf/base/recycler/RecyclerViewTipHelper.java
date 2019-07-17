package com.sf.base.recycler;

import android.view.View;
import com.sf.base.view.LoadView;
import com.sf.utility.ViewUtil;
import com.sf.widget.tip.TipHelper;
import com.sf.widget.tip.TipType;
import com.sf.widget.tip.TipUtil;
import com.sf.base.R;

public class RecyclerViewTipHelper implements TipHelper {

  private RecyclerFragment mFragment;
  private View mTipHost;

  protected View mLoadingView;
  protected View mErrorView;
  protected View mLoadMoreView;

  private boolean hasNoMoreTip = false;
  protected View mNoMoreView;

  public RecyclerViewTipHelper(RecyclerFragment fragment) {
    mFragment = fragment;
    mTipHost = createTipsHost();
    mLoadMoreView = createLoadMoreView();
    mLoadMoreView.setVisibility(View.GONE);
    mFragment.getHeaderFooterAdapter().addFooterView(mLoadMoreView);
  }

  @Override
  public void showEmpty() {
    TipUtil.showTip(mTipHost, getEmptyTipsType());
  }

  @Override
  public void hideEmpty() {
    TipUtil.hideTip(mTipHost, getEmptyTipsType());
  }

  @Override
  public void showLoading(boolean firstPage, boolean isCache) {
    if (firstPage) {
      if (mFragment.getOriginAdapter().isEmpty()) {
        mLoadingView = TipUtil.showTip(mTipHost, getLoadingTipsType());
      }
      return;
    }
    mLoadMoreView.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideLoading() {
    TipUtil.hideTip(mTipHost, getLoadingTipsType());
    mLoadMoreView.setVisibility(View.GONE);
  }

  @Override
  public void showNoMoreTips() {
    if (hasNoMoreTip) {
      if (mNoMoreView == null) {
        mNoMoreView = ViewUtil.inflate(mFragment.getRecyclerView(), getNoMoreTipsLayoutRes());
      }
      mFragment.getHeaderFooterAdapter().addFooterView(mNoMoreView);
    }
  }

  @Override
  public void hideNoMoreTips() {
    if (hasNoMoreTip) {
      if (mNoMoreView != null) {
        mFragment.getHeaderFooterAdapter().removeFooterView(mNoMoreView);
      }
    }
  }

  @Override
  public void showError(boolean firstPage, Throwable error) {
    mErrorView = TipUtil.showTip(mTipHost, getFailedTipsType());
    mErrorView.findViewById(R.id.tv_error).setOnClickListener(listener -> {
      mFragment.getPageList().refresh();
    });
  }

  @Override
  public void hideError() {
    TipUtil.hideTip(mTipHost, getFailedTipsType());
  }


  protected View createTipsHost() {
    return mFragment.getRecyclerView();
  }

  protected int getNoMoreTipsLayoutRes() {
    return R.layout.tip_nomore;
  }

  protected TipType getLoadingTipsType() {
    return TipType.LOADING;
  }

  protected TipType getFailedTipsType() {
    return TipType.LOADING_FAILED;
  }

  protected TipType getEmptyTipsType() {
    return TipType.EMPTY;
  }

  public void setHasNoMoreTip(boolean hasNoMoreTip) {
    this.hasNoMoreTip = hasNoMoreTip;
  }

  protected View createLoadMoreView() {
    LoadView loadView = new LoadView(mFragment.getContext());
    return loadView;
  }
}
