package com.sf.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

  public RecyclerViewHolder(View itemView) {
    super(itemView);
    onCreateView(itemView);
  }

  protected abstract void onCreateView(View itemView);

  protected abstract void onBindData(T data,RecyclerViewHolder holder);
}
