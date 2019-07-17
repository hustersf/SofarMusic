package com.sf.sofarmusic.online.artist.viewholder;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.ArtistActivity;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistClickViewHolder extends RecyclerViewHolder<Artist> {

  View artistRootView;

  public ArtistClickViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    artistRootView = itemView.findViewById(R.id.artist_root);
  }

  @Override
  protected void onBindData(Artist item) {
    artistRootView.setOnClickListener(v -> {
      ArtistActivity.launch(getContext(), item);
    });
  }
}
