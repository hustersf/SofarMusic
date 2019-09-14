package com.sf.sofarmusic.search.category;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.search.viewholder.SearchSongItemViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchSongAdapter extends RecyclerAdapter<Song> {
  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.search_song_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new SearchSongItemViewHolder(itemView));
    return holder;
  }
}
