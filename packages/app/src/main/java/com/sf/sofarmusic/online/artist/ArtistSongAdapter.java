package com.sf.sofarmusic.online.artist;

import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.viewholder.ArtistSongViewHolder;
import com.sf.sofarmusic.online.artist.viewholder.ArtistSongMoreViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistSongAdapter extends RecyclerAdapter<Song> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.artist_song_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new ArtistSongViewHolder(itemView));
    holder.addViewHolder(new ArtistSongMoreViewHolder(itemView));
    return holder;
  }
}
