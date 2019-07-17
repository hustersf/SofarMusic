package com.sf.sofarmusic.online.artist.viewholder;

import android.view.View;
import android.widget.TextView;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayTimeUtil;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistSongViewHolder extends RecyclerViewHolder<Song> {

  SofarImageView cover;
  TextView nameTv;
  TextView albumTv;
  TextView durationTv;

  public ArtistSongViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    cover = itemView.findViewById(R.id.cover);
    nameTv = itemView.findViewById(R.id.name_tv);
    albumTv = itemView.findViewById(R.id.album_tv);
    durationTv = itemView.findViewById(R.id.duration_tv);
  }

  @Override
  protected void onBindData(Song item) {
    cover.bindUrl(item.smallThumbUrl);
    nameTv.setText(item.name);
    albumTv.setText(item.albumTitle);
    durationTv.setText(PlayTimeUtil.getFormatTimeStr(item.length * 1000));

    itemView.setOnClickListener(v -> {

    });
  }
}
