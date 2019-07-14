package com.sf.widget.recyclerview;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sf.utility.CollectionUtil;
import com.sf.utility.ViewUtil;

/**
 * @param <T> 列表数据的实体类
 *          封装一个通用的RecyclerView的适配器
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

  protected List<T> items;
  private boolean wrapped;

  public RecyclerAdapter() {
    this(new ArrayList<>());
  }

  public RecyclerAdapter(List<T> datas) {
    items = datas;
  }

  public void setList(List<T> datas) {
    if (items == null) {
      items = new ArrayList<>();
    }
    items.clear();
    items.addAll(datas);
  }

  public void setListWithRelated(List<T> datas) {
    items = datas;
  }

  public List<T> getList() {
    return items;
  }

  public @Nullable T getItem(int position) {
    return (position < 0 || position >= items.size()) ? null : items.get(position);
  }

  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = ViewUtil.inflate(parent, getItemLayoutId(viewType), false);
    RecyclerViewHolder holder = onCreateViewHolder(viewType, view);
    holder.onCreateView(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerViewHolder holder, int position) {
    int realPosition;
    if (wrapped) {
      realPosition = position;
    } else {
      realPosition = holder.getAdapterPosition();
    }
    holder.viewAdapterPosition = realPosition;
    holder.onBindData(items.get(realPosition));
  }

  @Override
  public void onViewRecycled(RecyclerViewHolder holder) {
    super.onViewRecycled(holder);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public boolean isEmpty() {
    return CollectionUtil.isEmpty(items);
  }

  /**
   * 子类提供布局id
   */
  protected abstract int getItemLayoutId(int viewType);

  /**
   * 子类创建具体的ViewHolder
   */
  protected abstract RecyclerViewHolder onCreateViewHolder(int viewType, View itemView);

  /**
   * 
   * @param wrapped 是否被{@link RecyclerHeaderFooterAdapter2 装饰}
   */
  public void setWrappedByHeaderFooterAdapter(boolean wrapped) {
    this.wrapped = wrapped;
  }

}
