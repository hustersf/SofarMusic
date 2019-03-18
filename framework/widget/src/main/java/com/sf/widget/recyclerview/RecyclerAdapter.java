package com.sf.widget.recyclerview;

import java.util.ArrayList;
import java.util.List;

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

  protected List<T> mDatas;

  public RecyclerAdapter() {
    this(new ArrayList<>());
  }

  public RecyclerAdapter(List<T> datas) {
    mDatas = datas;
  }

  public void setList(List<T> datas) {
    mDatas.clear();
    mDatas.addAll(datas);
  }

  public List<T> getList() {
    return mDatas;
  }

  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = ViewUtil.inflate(parent, getItemLayoutId(viewType), false);
    RecyclerViewHolder holder = onCreateViewHolder(viewType, view);
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerViewHolder holder, int position) {
    holder.onBindData(mDatas.get(holder.getAdapterPosition()), holder);
  }

  @Override
  public void onViewRecycled(RecyclerViewHolder holder) {
    super.onViewRecycled(holder);
  }

  @Override
  public int getItemCount() {
    return mDatas.size();
  }

  public boolean isEmpty() {
    return CollectionUtil.isEmpty(mDatas);
  }

  /**
   * 子类提供布局id
   */
  protected abstract int getItemLayoutId(int viewType);

  /**
   * 子类创建具体的ViewHolder
   */
  protected abstract RecyclerViewHolder onCreateViewHolder(int viewType, View itemView);

}
