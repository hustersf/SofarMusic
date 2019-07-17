package com.sf.sofarmusic.online.artist;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.MV;
import com.sf.sofarmusic.online.artist.viewholder.ArtistMVViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistMVAdapter extends RecyclerAdapter<MV> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.artist_mv_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new ArtistMVViewHolder(itemView));
    return holder;
  }
}
