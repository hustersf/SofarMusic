package com.sf.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

  public Context context;
  public int viewAdapterPosition;

  public RecyclerViewHolder(View itemView) {
    super(itemView);
    context = itemView.getContext();
    onCreateView(itemView);
  }

  protected abstract void onCreateView(View itemView);

  protected abstract void onBindData(T item, RecyclerViewHolder holder);

  public Context getContext(){
    return context;
  }

  public int getViewAdapterPosition() {
    return viewAdapterPosition;
  }
}
