package com.sf.sofarmusic.online.artist.viewholder;

import android.view.View;
import android.widget.TextView;

import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayTimeUtil;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistAlbumViewHolder extends RecyclerViewHolder<Album> {

  SofarImageView cover;
  TextView nameTv;
  TextView timeTv;
  TextView songCountTv;

  public ArtistAlbumViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    cover = itemView.findViewById(R.id.cover);
    nameTv = itemView.findViewById(R.id.name_tv);
    timeTv = itemView.findViewById(R.id.time_tv);
    songCountTv = itemView.findViewById(R.id.song_count_tv);
  }

  @Override
  protected void onBindData(Album item) {
    cover.bindUrl(item.smallThumbUrl);
    nameTv.setText(item.name);
    timeTv.setText(item.publishTime);
    songCountTv.setText(item.songCount + "首歌曲");

    itemView.setOnClickListener(v -> {

    });
  }
}
