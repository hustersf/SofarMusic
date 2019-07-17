package com.sf.sofarmusic.online.artist;

import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.online.artist.viewholder.ArtistAlbumViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistAlbumAdapter extends RecyclerAdapter<Album> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.artist_album_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new ArtistAlbumViewHolder(itemView));
    return holder;
  }
}
