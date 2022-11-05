package com.sf.widget.recyclerview;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

  public Context context;
  public int viewAdapterPosition;

  public RecyclerViewHolder(View itemView) {
    super(itemView);
    context = itemView.getContext();
  }

  protected abstract void onCreateView(View itemView);

  protected abstract void onBindData(T item);

  public Context getContext() {
    return context;
  }

  public int getViewAdapterPosition() {
    return viewAdapterPosition;
  }
}
