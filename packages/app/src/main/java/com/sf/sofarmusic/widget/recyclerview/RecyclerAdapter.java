package com.sf.sofarmusic.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @param <T> 列表数据的实体类
 *          封装一个通用的RecyclerView的适配器
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

  private List<T> mDatas;
  private Context mContext;
  private LayoutInflater mInflater;

  public RecyclerAdapter(Context context, List<T> datas) {
    mContext = context;
    mDatas = datas;
    mInflater = LayoutInflater.from(mContext);
  }

  @Override
  public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = onCreateView(parent, viewType);
    return new RecyclerViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerViewHolder holder, int position) {
    onBindData(mDatas.get(position), holder);
  }

  @Override
  public int getItemCount() {
    return mDatas.size();
  }

  protected abstract View onCreateView(ViewGroup parent, int viewType);

  protected abstract void onBindData(T data, RecyclerViewHolder holder);

}
