package com.sf.sofarmusic.search;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.search.model.SearchInfo;
import com.sf.sofarmusic.search.viewholder.SearchResultItemViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchResultAdapter extends RecyclerAdapter<SearchInfo> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.search_result_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new SearchResultItemViewHolder(itemView));
    return holder;
  }
}
