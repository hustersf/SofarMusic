package com.sf.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

  private SparseArray<View> mViews;

  public RecyclerViewHolder(View itemView) {
    super(itemView);
  }

  public <T extends View> T getView(int id) {
    View view = mViews.get(id);
    if (view == null) {
      view = itemView.findViewById(id);
      mViews.put(id, view);
    }
    return (T) view;
  }
}
