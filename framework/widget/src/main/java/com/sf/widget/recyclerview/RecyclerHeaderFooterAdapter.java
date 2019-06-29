package com.sf.widget.recyclerview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.sf.utility.CollectionUtil;

/**
 * 这个类基于{@link android.widget.HeaderViewListAdapter}
 * 头布局的viewType范围[1024,2048)
 * 尾布局的viewType范围[2048,MAX_VALUE]
 * 因此RecyclerView本身的viewType<1024(默认是0)
 *
 * {@link RecyclerView.AdapterDataObserver}
 *
 * 用法：将RecyclerView的adapter直接塞进来即可(装饰者模式)
 */
public class RecyclerHeaderFooterAdapter extends RecyclerView.Adapter {

  private final RecyclerView.Adapter mAdapter;
  ArrayList<FixedViewInfo> mHeaderViewInfos;
  ArrayList<FixedViewInfo> mFooterViewInfos;

  static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>();

  private static final int BASE_HEADER_VIEW_TYPE = 1 << 10; // [1024,2048)
  private static final int BASE_FOOTER_VIEW_TYPE = 1 << 11; // [2048,MAX_VALUE]

  private final List<HeaderFooterAdapterDataObserver> mHeaderFooterAdapterDataObservers =
      new CopyOnWriteArrayList<>();

  private boolean mIsStaggeredGrid;

  public RecyclerHeaderFooterAdapter(RecyclerView.Adapter adapter) {
    this(null, null, adapter);
  }

  public RecyclerHeaderFooterAdapter(ArrayList<FixedViewInfo> headerViewInfos,
      ArrayList<FixedViewInfo> footerViewInfos, RecyclerView.Adapter adapter) {
    mAdapter = adapter;

    if (headerViewInfos == null) {
      mHeaderViewInfos = EMPTY_INFO_LIST;
    } else {
      mHeaderViewInfos = headerViewInfos;
    }

    if (footerViewInfos == null) {
      mFooterViewInfos = EMPTY_INFO_LIST;
    } else {
      mFooterViewInfos = footerViewInfos;
    }
  }



  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (isHeaderViewType(viewType)) {
      int headerIndex = viewType - BASE_HEADER_VIEW_TYPE;
      View headerView = mHeaderViewInfos.get(headerIndex).view;
      return createHeaderFooterViewHolder(headerView);
    } else if (isFooterViewType(viewType)) {
      int footerIndex = viewType - BASE_FOOTER_VIEW_TYPE;
      View footerView = mFooterViewInfos.get(footerIndex).view;
      return createHeaderFooterViewHolder(footerView);
    } else {
      return mAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (isHeaderPosition(position) || isFooterPosition(position)) {
      return;
    }
    mAdapter.onBindViewHolder(holder, position - getHeadersCount());
  }

  @Override
  public int getItemViewType(int position) {
    if (isHeaderPosition(position)) {
      return mHeaderViewInfos.get(position).viewType;
    } else if (isFooterPosition(position)) {
      return mFooterViewInfos.get(position - getHeadersCount() - mAdapter.getItemCount()).viewType;
    } else {
      return mAdapter.getItemViewType(position - getHeadersCount());
    }
  }

  @Override
  public int getItemCount() {
    if (mAdapter != null) {
      return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
    } else {
      return getHeadersCount() + getFootersCount();
    }
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    mAdapter.onAttachedToRecyclerView(recyclerView);
    if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
      final GridLayoutManager gridManager = (GridLayoutManager) recyclerView.getLayoutManager();
      gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
          boolean isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position);
          return isHeaderOrFooter ? gridManager.getSpanCount() : 1;
        }
      });
    } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
      mIsStaggeredGrid = true;
    }
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    mAdapter.onDetachedFromRecyclerView(recyclerView);
  }

  public RecyclerView.Adapter getAdapter() {
    return mAdapter;
  }

  public int getHeadersCount() {
    return mHeaderViewInfos.size();
  }

  public int getFootersCount() {
    return mFooterViewInfos.size();
  }

  public boolean isEmpty() {
    return mAdapter == null || mAdapter.getItemCount() == 0;
  }

  /**
   * 
   * @param views 要添加到RecyclerView的头部的view列表
   */
  public void addHeaderViews(List<View> views) {
    if (CollectionUtil.isEmpty(views)) {
      return;
    }

    for (int i = 0; i < views.size(); i++) {
      addHeaderView(views.get(i));
    }
  }

  /**
   * @param v 要添加的View
   *          通过此方法，将view添加到RecyclerView的头部
   */
  public void addHeaderView(View v) {
    if (v == null) {
      return;
    }

    if (containsHeaderView(v)) {
      return;
    }

    FixedViewInfo info = new FixedViewInfo();
    info.view = v;
    info.viewType = BASE_HEADER_VIEW_TYPE + mHeaderViewInfos.size();
    mHeaderViewInfos.add(info);
    notifyItemInserted(getHeadersCount() - 1);
  }

  public boolean removeHeaderView(View v) {
    for (int i = 0; i < mHeaderViewInfos.size(); i++) {
      FixedViewInfo info = mHeaderViewInfos.get(i);
      if (info.view == v) {
        mHeaderViewInfos.remove(i);
        notifyItemRemoved(i);
        return true;
      }
    }
    return false;
  }

  /**
   * 
   * @param views 要添加到RecyclerView的尾部的view列表
   */
  public void addFooterViews(List<View> views) {
    if (CollectionUtil.isEmpty(views)) {
      return;
    }

    for (int i = 0; i < views.size(); i++) {
      addFooterView(views.get(i));
    }
  }

  /**
   * @param v 要添加的View
   *          通过此方法，将view添加到RecyclerView的尾部
   */
  public void addFooterView(View v) {
    if (v == null) {
      return;
    }

    if (containsFooterView(v)) {
      return;
    }

    FixedViewInfo info = new FixedViewInfo();
    info.view = v;
    info.viewType = BASE_FOOTER_VIEW_TYPE + mFooterViewInfos.size();
    mFooterViewInfos.add(info);
    notifyItemInserted(getItemCount() - 1);
  }

  public boolean removeFooterView(View v) {
    for (int i = 0; i < mFooterViewInfos.size(); i++) {
      FixedViewInfo info = mFooterViewInfos.get(i);
      if (info.view == v) {
        mFooterViewInfos.remove(i);
        notifyItemRemoved(getHeadersCount() + mAdapter.getItemCount() + i);
        return true;
      }
    }
    return false;
  }

  public boolean isHeaderPosition(int position) {
    return position < mHeaderViewInfos.size();
  }

  public boolean isFooterPosition(int position) {
    return position >= mHeaderViewInfos.size() + mAdapter.getItemCount();
  }


  public boolean containsHeaderView(View v) {
    for (int i = 0; i < mHeaderViewInfos.size(); i++) {
      FixedViewInfo info = mHeaderViewInfos.get(i);
      if (info.view == v) {
        return true;
      }
    }
    return false;
  }


  public boolean containsFooterView(View v) {
    for (int i = 0; i < mFooterViewInfos.size(); i++) {
      FixedViewInfo info = mFooterViewInfos.get(i);
      if (info.view == v) {
        return true;
      }
    }
    return false;
  }


  private boolean isHeaderViewType(int viewType) {
    return viewType >= BASE_HEADER_VIEW_TYPE
        && viewType < BASE_HEADER_VIEW_TYPE + getHeadersCount();

  }

  private boolean isFooterViewType(int viewType) {
    return viewType >= BASE_FOOTER_VIEW_TYPE
        && viewType < BASE_FOOTER_VIEW_TYPE + getFootersCount();
  }

  private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
    if (mIsStaggeredGrid) {
      StaggeredGridLayoutManager.LayoutParams layoutParams =
          new StaggeredGridLayoutManager.LayoutParams(
              StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,
              StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
      layoutParams.setFullSpan(true);
      view.setLayoutParams(layoutParams);
    } else {
      RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
          RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
      view.setLayoutParams(layoutParams);
    }
    return new RecyclerView.ViewHolder(view) {};
  }


  private static class FixedViewInfo {
    public int viewType;
    public View view;
  }


  @Override
  public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.registerAdapterDataObserver(observer);

    HeaderFooterAdapterDataObserver headerFooterAdapterDataObserver =
        new HeaderFooterAdapterDataObserver(observer);
    mHeaderFooterAdapterDataObservers.add(headerFooterAdapterDataObserver);
    mAdapter.registerAdapterDataObserver(headerFooterAdapterDataObserver);
  }

  @Override
  public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.unregisterAdapterDataObserver(observer);

    for (HeaderFooterAdapterDataObserver headerFooterAdapterDataObserver : mHeaderFooterAdapterDataObservers) {
      if (headerFooterAdapterDataObserver.mAdapterDataObserver == observer) {
        mHeaderFooterAdapterDataObservers.remove(headerFooterAdapterDataObserver);
        mAdapter.unregisterAdapterDataObserver(headerFooterAdapterDataObserver);
        return;
      }
    }
  }

  private class HeaderFooterAdapterDataObserver extends RecyclerView.AdapterDataObserver {
    @NonNull
    RecyclerView.AdapterDataObserver mAdapterDataObserver;

    public HeaderFooterAdapterDataObserver(
        @NonNull RecyclerView.AdapterDataObserver adapterDataObserver) {
      this.mAdapterDataObserver = adapterDataObserver;
    }

    @Override
    public void onChanged() {
      super.onChanged();
      this.mAdapterDataObserver.onChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
      this.mAdapterDataObserver.onItemRangeChanged(positionStart + mHeaderViewInfos.size(),
          itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
      this.mAdapterDataObserver.onItemRangeChanged(positionStart + mHeaderViewInfos.size(),
          itemCount, payload);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      this.mAdapterDataObserver.onItemRangeInserted(positionStart + mHeaderViewInfos.size(),
          itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      this.mAdapterDataObserver.onItemRangeRemoved(positionStart + mHeaderViewInfos.size(),
          itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      this.mAdapterDataObserver.onItemRangeMoved(fromPosition + mHeaderViewInfos.size(),
          toPosition + mHeaderViewInfos.size(), itemCount);
    }
  }
}
