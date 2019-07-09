package com.sf.widget.recyclerview;

import java.util.List;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.sf.widget.BuildConfig;

/**
 * 替代{@link RecyclerHeaderFooterAdapter}
 */
public class RecyclerHeaderFooterAdapter2 extends RecyclerView.Adapter {
  private static final int BASE_HEADER_VIEW_TYPE = -1 << 10;
  private static final int BASE_FOOTER_VIEW_TYPE = -1 << 11;

  private final HeaderFooterViewGroup mHeaderGroup;
  private final HeaderFooterViewGroup mFooterGroup;

  private RecyclerView.Adapter mAdapter;
  private final RecyclerView.AdapterDataObserver mAdapterDataObserverProxy;

  private RecyclerView.Adapter mHeaderAdapter;
  private RecyclerView.Adapter mFooterAdapter;

  private int mMaxFooterViewType = BASE_FOOTER_VIEW_TYPE;
  private int mMaxHeaderViewType = BASE_HEADER_VIEW_TYPE;

  private boolean mIsStaggeredGrid;
  private boolean mIsLinearHorizontal;
  private int mLastAdapterContentCount = -1;

  private boolean mSmoothNotifyChange; // true的话，当数据集变化时，会尽量的通过notifyItemRangeChanged局部刷新，尽量不刷新headerView

  public RecyclerHeaderFooterAdapter2(RecyclerView.Adapter adapter) {
    this(adapter, null, null);
  }

  public RecyclerHeaderFooterAdapter2(RecyclerView.Adapter adapter,
      List<View> headerViewInfoList,
      List<View> footerViewInfoList) {
    this.mAdapter = adapter;
    if (mAdapter instanceof RecyclerAdapter) {
      ((RecyclerAdapter) mAdapter).setWrappedByHeaderFooterAdapter(true);
    }

    mHeaderGroup = new HeaderFooterViewGroup(headerViewInfoList);
    mFooterGroup = new HeaderFooterViewGroup(footerViewInfoList);

    mAdapterDataObserverProxy = new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        if (mSmoothNotifyChange) {
          smoothChange(mAdapter.getItemCount());
          return;
        }

        int contentAdapterCount = mAdapter.getItemCount();
        RecyclerHeaderFooterAdapter2.this.notifyDataSetChanged();
        if (mLastAdapterContentCount == -1
            || (contentAdapterCount != 0 && contentAdapterCount == mLastAdapterContentCount)) {
          try {
            RecyclerHeaderFooterAdapter2.this.notifyItemRangeChanged(getHeaderCount(),
                contentAdapterCount);
          } catch (Exception e) {
            if (BuildConfig.DEBUG) {
              throw new IllegalStateException(e);
            }
          }
        } else {
          try {
            RecyclerHeaderFooterAdapter2.this.notifyDataSetChanged();
          } catch (Exception e) {
            // 如果 recyclerView 的 parent 是 TipsContainer,
            // 在 hide 操作的时候需要 remove View 再 add 导致 recyclerView 出现问题
            if (BuildConfig.DEBUG) {
              throw new IllegalStateException(e);
            }
          }
        }
        mLastAdapterContentCount = contentAdapterCount;
      }

      @Override
      public void onItemRangeChanged(int positionStart, int itemCount) {
        mLastAdapterContentCount = mAdapter.getItemCount();
        try {
          RecyclerHeaderFooterAdapter2.this
              .notifyItemRangeChanged(positionStart + getHeaderCount(), itemCount);
        } catch (Exception e) {
          if (BuildConfig.DEBUG) {
            throw new IllegalStateException(e);
          }
        }
      }

      @Override
      public void onItemRangeInserted(int positionStart, int itemCount) {
        mLastAdapterContentCount = mAdapter.getItemCount();
        try {
          RecyclerHeaderFooterAdapter2.this
              .notifyItemRangeInserted(positionStart + getHeaderCount(), itemCount);
        } catch (Exception e) {
          if (BuildConfig.DEBUG) {
            throw new IllegalStateException(e);
          }
        }
      }

      @Override
      public void onItemRangeRemoved(int positionStart, int itemCount) {
        try {
          RecyclerHeaderFooterAdapter2.this
              .notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount);
        } catch (Exception e) {
          if (BuildConfig.DEBUG) {
            throw new IllegalStateException(e);
          }
        }
      }

      @Override
      public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        try {
          RecyclerHeaderFooterAdapter2.this.notifyItemMoved(
              fromPosition + getHeaderCount(), toPosition + getHeaderCount());
        } catch (Exception e) {
          if (BuildConfig.DEBUG) {
            throw new IllegalStateException(e);
          }
        }
      }
    };
    this.mAdapter.registerAdapterDataObserver(mAdapterDataObserverProxy);
  }

  public void setSmoothNotifyChange(boolean smoothNotifyChange) {
    mSmoothNotifyChange = smoothNotifyChange;
  }

  public void updateAdapter(RecyclerView.Adapter adapter) {
    mAdapter = adapter;
    try {
      this.mAdapter.registerAdapterDataObserver(mAdapterDataObserverProxy);
    } catch (Exception e) {}
  }

  public void addHeaderAdapter(RecyclerView.Adapter adapter) {
    mHeaderAdapter = adapter;
    try {
      mHeaderAdapter.registerAdapterDataObserver(mAdapterDataObserverProxy);
    } catch (Exception ignore) {}
    notifyDataSetChanged();
  }

  public void addFooterAdapter(RecyclerView.Adapter adapter) {
    mFooterAdapter = adapter;
    try {
      mFooterAdapter.registerAdapterDataObserver(mAdapterDataObserverProxy);
    } catch (Exception ignore) {}
    notifyDataSetChanged();
  }

  // 尽量使用notifyItemRangeChanged，使得headerView不刷新
  private void smoothChange(int newCount) {
    try {
      int oldCount = mLastAdapterContentCount;
      int headerCount = getHeaderCount();

      if (oldCount == -1) { // 初次刷新
        notifyDataSetChanged();

      } else if (newCount == oldCount) { // 数量一致
        notifyItemRangeChanged(headerCount, newCount);

      } else if (newCount > oldCount) { // 变多
        notifyItemRangeChanged(headerCount, oldCount);
        notifyItemRangeInserted(headerCount + oldCount, newCount - oldCount);

      } else { // 变少
        notifyItemRangeChanged(headerCount, newCount);
        notifyItemRangeRemoved(headerCount + newCount, oldCount - newCount);
      }
    } catch (Exception e) {
      if (BuildConfig.DEBUG) {
        throw new IllegalStateException(e);
      }
    }
    mLastAdapterContentCount = newCount;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (isHeaderViewType(viewType)) {
      int headerType = map2HeaderViewType(viewType);
      if (mHeaderAdapter == null) {
        View headerView = mHeaderGroup.getViewWithType(headerType);
        return createHeaderFooterViewHolder(headerView);
      } else {
        return mHeaderAdapter.onCreateViewHolder(parent, headerType);
      }
    } else if (isFooterViewType(viewType)) {
      int footerType = map2FooterViewType(viewType);
      if (mFooterAdapter == null) {
        View footerView = mFooterGroup.getViewWithType(footerType);
        return createHeaderFooterViewHolder(footerView);
      }
      return mFooterAdapter.onCreateViewHolder(parent, footerType);
    } else {
      return mAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    int realPosition = holder.getAdapterPosition();
    if (realPosition < getHeaderCount()
        || realPosition >= getHeaderCount() + mAdapter.getItemCount()) {
      if (realPosition < getHeaderCount() && mHeaderAdapter != null) {
        mHeaderAdapter.onBindViewHolder(holder, realPosition);
      } else if (realPosition >= getHeaderCount() + mAdapter.getItemCount()
          && mFooterAdapter != null) {
        mFooterAdapter.onBindViewHolder(holder,
            realPosition - getHeaderCount() - mAdapter.getItemCount());
      }
      return;
    }

    mAdapter.onBindViewHolder(holder, realPosition - getHeaderCount());
  }

  @Override
  public int getItemViewType(int position) {
    if (isHeaderPosition(position)) {
      int type = mHeaderAdapter != null
          ? mHeaderAdapter.getItemViewType(position)
          : mHeaderGroup.getViewTypeAtPos(position);
      type += BASE_HEADER_VIEW_TYPE;
      mMaxHeaderViewType = Math.max(type, mMaxHeaderViewType);
      return type;
    } else if (isFooterPosition(position)) {
      final int footerPosition = position - mAdapter.getItemCount() - getHeaderCount();
      int type = mFooterAdapter != null
          ? mFooterAdapter.getItemViewType(footerPosition)
          : mFooterGroup.getViewTypeAtPos(footerPosition);
      type += BASE_FOOTER_VIEW_TYPE;
      mMaxFooterViewType = Math.max(type, mMaxFooterViewType);
      return type;
    } else {
      return mAdapter.getItemViewType(position - getHeaderCount());
    }
  }

  @Override
  public int getItemCount() {
    return getFooterCount() + getHeaderCount() + mAdapter.getItemCount();
  }

  public int getHeaderCount() {
    return mHeaderAdapter != null ? mHeaderAdapter.getItemCount() : mHeaderGroup.viewCount();
  }

  public int getFooterCount() {
    return mFooterAdapter != null ? mFooterAdapter.getItemCount() : mFooterGroup.viewCount();
  }

  public boolean isHeaderPosition(int position) {
    return position < getHeaderCount();
  }

  public boolean isFooterPosition(int position) {
    return position >= getHeaderCount() + mAdapter.getItemCount();
  }

  public boolean isEmpty() {
    return mAdapter == null || mAdapter.getItemCount() == 0;
  }

  public boolean removeHeaderView(View v) {
    boolean res = mHeaderGroup.removeView(v);
    if (res) {
      notifyDataSetChangedCatchException();
    }
    return res;
  }

  public boolean removeFooterView(View v) {
    boolean res = mFooterGroup.removeView(v);
    if (res) {
      notifyDataSetChangedCatchException();
    }
    return res;
  }

  public void addHeaderView(View view) {
    if (null == view) {
      throw new IllegalArgumentException("the view to add must not be null");
    }
    if (mHeaderGroup.addView(view)) {
      notifyDataSetChangedCatchException();
    }
  }

  public void addFooterView(View view) {
    if (null == view) {
      throw new IllegalArgumentException("the view to add must not be null!");
    }
    // 最后一个元素新增
    if (mFooterGroup.addView(view)) {
      notifyDataSetChangedCatchException();
    }
  }

  public boolean containsFooterView(View v) {
    return mFooterGroup.containsView(v);
  }

  public boolean containsHeaderView(View v) {
    return mHeaderGroup.containsView(v);
  }

  public void setHeadersVisibility(boolean visible) {
    for (int i = 0; i < mHeaderGroup.viewCount(); ++i) {
      View view = mHeaderGroup.getViewAtPos(i);
      view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    try {
      notifyItemRangeChanged(0, getHeaderCount());
    } catch (Exception e) {
      if (BuildConfig.DEBUG) {
        throw new IllegalStateException(e);
      }
    }
  }

  public void setFootersVisibility(boolean visible) {
    for (int i = 0; i < mFooterGroup.viewCount(); i++) {
      View view = mFooterGroup.getViewAtPos(i);
      view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    try {
      notifyItemRangeChanged(getHeaderCount() + mAdapter.getItemCount(), getFooterCount());
    } catch (Exception e) {
      if (BuildConfig.DEBUG) {
        throw new IllegalStateException(e);
      }
    }
  }

  private void notifyDataSetChangedCatchException() {
    try {
      notifyDataSetChanged();
    } catch (Exception e) {
      if (BuildConfig.DEBUG) {
        throw new IllegalStateException(e);
      }
    }
  }

  public boolean isHeaderViewType(int viewType) {
    return viewType >= BASE_HEADER_VIEW_TYPE
        && viewType <= mMaxHeaderViewType;
  }

  public boolean isFooterViewType(int viewType) {
    return viewType >= BASE_FOOTER_VIEW_TYPE
        && viewType <= mMaxFooterViewType;
  }

  public int map2FooterViewType(int viewType) {
    return viewType - BASE_FOOTER_VIEW_TYPE;
  }

  public int map2HeaderViewType(int viewType) {
    return viewType - BASE_HEADER_VIEW_TYPE;
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
      RecyclerView.LayoutParams layoutParams;
      if (mIsLinearHorizontal) {
        layoutParams = new RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
      } else {
        layoutParams = new RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
      }
      view.setLayoutParams(layoutParams);
    }

    return new RecyclerView.ViewHolder(view) {};
  }

  public void adjustSpanSizeForHeaderFooter(RecyclerView recycler) {
    if (recycler.getLayoutManager() instanceof GridLayoutManager) {
      final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
      layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

        @Override
        public int getSpanSize(int position) {
          boolean isHeaderOrFooter =
              isHeaderPosition(position) || isFooterPosition(position);
          return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
        }

      });
    }

    if (recycler.getLayoutManager() instanceof StaggeredGridLayoutManager) {
      this.mIsStaggeredGrid = true;
    }

    if (recycler.getLayoutManager() instanceof LinearLayoutManager) {
      if (((LinearLayoutManager) recycler.getLayoutManager())
          .getOrientation() == LinearLayoutManager.HORIZONTAL) {
        this.mIsLinearHorizontal = true;
      }
    }
  }

  @Override
  public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.registerAdapterDataObserver(observer);
  }

  @Override
  public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);

    int itemViewType = holder.getItemViewType();
    if (isHeaderViewType(itemViewType)) {
      if (mHeaderAdapter != null) {
        mHeaderAdapter.onViewAttachedToWindow(holder);
      }
      if (isStaggeredGridLayout(holder)) {
        StaggeredGridLayoutManager.LayoutParams p =
            (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        p.setFullSpan(true);
      }
    } else if (isFooterViewType(itemViewType)) {
      if (mFooterAdapter != null) {
        mFooterAdapter.onViewAttachedToWindow(holder);
      }
      if (isStaggeredGridLayout(holder)) {
        StaggeredGridLayoutManager.LayoutParams p =
            (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        p.setFullSpan(true);
      }
    } else {
      mAdapter.onViewAttachedToWindow(holder);
      if (isStaggeredGridLayout(holder)) {
        StaggeredGridLayoutManager.LayoutParams p =
            (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        p.setFullSpan(false);
      }
    }
  }

  @Override
  public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    int itemViewType = holder.getItemViewType();
    if (isHeaderViewType(itemViewType)) {
      if (mHeaderAdapter != null) {
        mHeaderAdapter.onViewDetachedFromWindow(holder);
      }
    } else if (isFooterViewType(itemViewType)) {
      if (mFooterAdapter != null) {
        mFooterAdapter.onViewDetachedFromWindow(holder);
      }
    } else {
      mAdapter.onViewDetachedFromWindow(holder);
    }
  }

  private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
    ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
    if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
      return true;
    }
    return false;
  }

  @Override
  public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.unregisterAdapterDataObserver(observer);
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    if (mAdapter.hasObservers()) {
      mAdapter.unregisterAdapterDataObserver(mAdapterDataObserverProxy);
    }
    mAdapter.registerAdapterDataObserver(mAdapterDataObserverProxy);
    mAdapter.onAttachedToRecyclerView(recyclerView);

    if (mHeaderAdapter != null) {
      mHeaderAdapter.onAttachedToRecyclerView(recyclerView);
      mHeaderAdapter.unregisterAdapterDataObserver(mAdapterDataObserverProxy);
      mHeaderAdapter.registerAdapterDataObserver(mAdapterDataObserverProxy);
    }

    if (mFooterAdapter != null) {
      mFooterAdapter.onAttachedToRecyclerView(recyclerView);
      mFooterAdapter.unregisterAdapterDataObserver(mAdapterDataObserverProxy);
      mFooterAdapter.registerAdapterDataObserver(mAdapterDataObserverProxy);
    }
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    if (mAdapter.hasObservers()) {
      mAdapter.unregisterAdapterDataObserver(mAdapterDataObserverProxy);
    }
    mAdapter.onDetachedFromRecyclerView(recyclerView);

    if (mHeaderAdapter != null) {
      mHeaderAdapter.onDetachedFromRecyclerView(recyclerView);
      mHeaderAdapter.unregisterAdapterDataObserver(mAdapterDataObserverProxy);
    }

    if (mFooterAdapter != null) {
      mFooterAdapter.onDetachedFromRecyclerView(recyclerView);
      mFooterAdapter.unregisterAdapterDataObserver(mAdapterDataObserverProxy);
    }
  }

  public RecyclerView.Adapter getAdapter() {
    return mAdapter;
  }

  /**
   * Header/Footer view 的集合，用来维护 header/footer view type 和 view 的对应关系
   * view type 仍然以 position 做为 key，为了保持稳定，只会增加，不随着 remove view 而减少
   */
  static class HeaderFooterViewGroup {
    // key = view type in adapter, value = value
    private final SparseArray<View> mViews;
    // 曾经添加过的所有的 view count，只增不减，作为 mViews 的 key
    private int mIndex;

    HeaderFooterViewGroup() {
      this(null);
    }

    HeaderFooterViewGroup(List<View> viewList) {
      mViews = new SparseArray<>();
      mIndex = 0;
      if (viewList != null) {
        for (View view : viewList) {
          mViews.put(mIndex++, view);
        }
      }
    }

    boolean containsView(View view) {
      return mViews.indexOfValue(view) >= 0;
    }

    /**
     * 增加一个 header/footer
     * 
     * @param view header/footer view
     * @return 是否添加成功
     */
    boolean addView(View view) {
      if (containsView(view)) {
        return false;
      }
      mViews.put(mIndex++, view);
      return true;
    }

    /**
     * 删除一个 header/footer
     * 
     * @param view header/footer view
     * @return 是否删除成功
     */
    boolean removeView(View view) {
      int index = mViews.indexOfValue(view);
      if (index < 0) {
        return false;
      }
      mViews.removeAt(index);
      return true;
    }

    /**
     * 返回在 pos 位置的 view
     * 
     * @param pos
     * @return view
     */
    View getViewAtPos(int pos) {
      return (pos >= 0 && pos < mViews.size()) ? mViews.valueAt(pos) : null;
    }

    /**
     * 返回特定 view type 的 view
     * 
     * @param viewType
     * @return view
     */
    View getViewWithType(int viewType) {
      return mViews.get(viewType);
    }

    /**
     * 返回 pos 位置的 view type >= 0，如果 view type < 0 表示 invalid
     * 
     * @param pos
     * @return
     */
    int getViewTypeAtPos(int pos) {
      return (pos >= 0 && pos < mViews.size()) ? mViews.keyAt(pos) : -1;
    }

    int viewCount() {
      return mViews.size();
    }

    int maxIndex() {
      return mIndex;
    }
  }
}
