package com.sf.sofarmusic.online.artist;

import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.viewholder.ArtistClickViewHolder;
import com.sf.sofarmusic.online.artist.viewholder.ArtistResizeViewHolder;
import com.sf.sofarmusic.online.artist.viewholder.ArtistShowViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistGroupHeaderAdapter extends RecyclerAdapter<Artist> {

  private static final int MAX_COUNT = 8;

  @Override
  public int getItemCount() {
    return super.getItemCount() > MAX_COUNT ? MAX_COUNT : super.getItemCount();
  }

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.artist_group_header_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder viewHolder = new MultiRecyclerViewHolder(itemView);
    viewHolder.addViewHolder(new ArtistResizeViewHolder(itemView));
    viewHolder.addViewHolder(new ArtistShowViewHolder(itemView));
    viewHolder.addViewHolder(new ArtistClickViewHolder(itemView));
    return viewHolder;
  }
}
