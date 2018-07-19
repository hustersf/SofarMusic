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
    View view = mInflater.inflate(getItemLayoutId(), parent, false);
    RecyclerViewHolder holder = new RecyclerViewHolder(view);
    onCreateView(holder);
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerViewHolder holder, int position) {
    onBindData(mDatas.get(position), holder);
  }

  @Override
  public int getItemCount() {
    return mDatas.size();
  }

  /**
   * 子类提供布局id
   */
  protected abstract int getItemLayoutId();


  /**
   * 可以在这个方法中获取布局中控件
   */
  protected abstract void onCreateView(RecyclerViewHolder holder);

  /**
   * 可以在这个方法处理数据绑定
   */
  protected abstract void onBindData(T data, RecyclerViewHolder holder);

}
