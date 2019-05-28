package com.sf.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

  public Context context;

  public RecyclerViewHolder(View itemView) {
    super(itemView);
    context = itemView.getContext();
    onCreateView(itemView);
  }

  protected abstract void onCreateView(View itemView);

  protected abstract void onBindData(T data, RecyclerViewHolder holder);
}
