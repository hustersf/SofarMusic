package com.sf.base.recycler;

import androidx.recyclerview.widget.RecyclerView;

import com.sf.base.network.page.PageList;

public class AutoLoadEventDetector extends RecyclerView.OnScrollListener {

  private PageList mPageList;
  private RecyclerFragment mFragment;

  public AutoLoadEventDetector(RecyclerFragment fragment, PageList pageList) {
    this.mPageList = pageList;
    this.mFragment = fragment;
  }

  @Override
  public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);
    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
      recyclerView.requestDisallowInterceptTouchEvent(true);
      tryToLoadMore(recyclerView);
    }
  }

  @Override
  public void onScrolled(RecyclerView view, int dx, int dy) {
    tryToLoadMore(view);
  }

  protected void tryToLoadMore(RecyclerView view) {
    RecyclerView.LayoutManager manager = view.getLayoutManager();
    if (manager.getChildCount() > 0 && isReadyLoadMore()) {
      int count = manager.getItemCount();
      int last = ((RecyclerView.LayoutParams) manager
        .getChildAt(manager.getChildCount() - 1).getLayoutParams()).getViewAdapterPosition();
      if (last == count - 1 && mPageList.hasMore()) {
        mPageList.load();
      }
    }
  }

  private boolean isReadyLoadMore() {
    return mPageList != null && !mPageList.isEmpty();
  }
}
