package com.sf.sofarmusic.online.artist;

import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.viewholder.ArtistClickViewHolder;
import com.sf.sofarmusic.online.artist.viewholder.ArtistShowViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistListAdapter extends RecyclerAdapter<Artist> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.artist_list_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder viewHolder = new MultiRecyclerViewHolder(itemView);
    viewHolder.addViewHolder(new ArtistShowViewHolder(itemView));
    viewHolder.addViewHolder(new ArtistClickViewHolder(itemView));
    return viewHolder;
  }
}
