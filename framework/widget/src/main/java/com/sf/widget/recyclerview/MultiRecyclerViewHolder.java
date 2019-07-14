package com.sf.widget.recyclerview;

import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MultiRecyclerViewHolder<T> extends RecyclerViewHolder<T> {

  private List<RecyclerViewHolder> holders = new ArrayList<>();

  public MultiRecyclerViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    for (RecyclerViewHolder holder : holders) {
      holder.onCreateView(itemView);
    }
  }

  @Override
  protected void onBindData(T item) {
    for (RecyclerViewHolder holder : holders) {
      holder.onBindData(item);
    }
  }

  public void addViewHolder(RecyclerViewHolder viewHolder) {
    holders.add(viewHolder);
  }

}
