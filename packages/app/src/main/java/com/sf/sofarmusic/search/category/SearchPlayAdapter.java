package com.sf.sofarmusic.search.category;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.PlayInfo;
import com.sf.sofarmusic.search.viewholder.SearchPlayItemViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchPlayAdapter extends RecyclerAdapter<PlayInfo> {
  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.search_play_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new SearchPlayItemViewHolder(itemView));
    return holder;
  }
}
