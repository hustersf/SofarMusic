package com.sf.sofarmusic.online.artist.viewholder;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.widget.round.RoundImageView;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistShowViewHolder extends RecyclerViewHolder<Artist> {

  RoundImageView artistIv;
  TextView artistTv;

  public ArtistShowViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    artistIv = itemView.findViewById(R.id.artist_iv);
    artistTv = itemView.findViewById(R.id.artist_tv);
  }

  @Override
  protected void onBindData(Artist item) {
    Glide.with(getContext()).load(item.avatarBigUrl).into(artistIv);
    artistTv.setText(item.name);
  }
}
