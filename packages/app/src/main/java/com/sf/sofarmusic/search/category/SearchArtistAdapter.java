package com.sf.sofarmusic.search.category;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.search.viewholder.SearchArtistItemViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchArtistAdapter extends RecyclerAdapter<Artist> {
  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.search_artist_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new SearchArtistItemViewHolder(itemView));
    return holder;
  }
}
