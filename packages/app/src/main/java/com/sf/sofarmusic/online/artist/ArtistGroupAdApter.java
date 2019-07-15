package com.sf.sofarmusic.online.artist;

import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.artist.model.ArtistGroup;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistGroupAdApter extends RecyclerAdapter<ArtistGroup> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.artist_group_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new ArtistGroupViewHolder(itemView);
  }

  class ArtistGroupViewHolder extends RecyclerViewHolder<ArtistGroup> {

    TextView artistGroupTv;

    public ArtistGroupViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      artistGroupTv = itemView.findViewById(R.id.artist_group_tv);
    }

    @Override
    protected void onBindData(ArtistGroup item) {
      artistGroupTv.setText(item.name);
      itemView.setOnClickListener(v -> {
        ArtistListActivity.launch(getContext(), item);
      });
    }
  }
}
