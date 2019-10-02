package com.sf.sofarmusic.search.category;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.search.viewholder.SearchAlbumItemViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchAlbumAdapter extends RecyclerAdapter<Album> {
  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.search_album_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new SearchAlbumItemViewHolder(itemView));
    return holder;
  }
}
