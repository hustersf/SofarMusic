package com.sf.base.recycler;

import android.view.View;

import com.sf.widget.tip.TipHelper;
import com.sf.widget.tip.TipType;
import com.sf.widget.tip.TipUtil;
import com.sf.base.R;

public class RecyclerViewTipHelper implements TipHelper {

  private RecyclerFragment mFragment;
  private View mTipHost;

  protected View mLoadingView;
  protected View mErrorView;

  public RecyclerViewTipHelper(RecyclerFragment fragment) {
    mFragment = fragment;
    mTipHost = createTipsHost();
  }

  @Override
  public void showEmpty() {

  }

  @Override
  public void hideEmpty() {

  }

  @Override
  public void showLoading(boolean firstPage, boolean isCache) {
    if (mFragment.getOriginAdapter().isEmpty()) {
      mLoadingView = TipUtil.showTip(mTipHost, getLoadingTipsType());
    }
  }

  @Override
  public void hideLoading() {
    TipUtil.hideTip(mTipHost, getLoadingTipsType());
  }

  @Override
  public void showNoMoreTips() {

  }

  @Override
  public void hideNoMoreTips() {

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

  protected TipType getLoadingTipsType() {
    return TipType.LOADING;
  }

  protected TipType getFailedTipsType() {
    return TipType.LOADING_FAILED;
  }
}
