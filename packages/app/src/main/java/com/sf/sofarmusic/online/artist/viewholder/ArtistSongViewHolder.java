package com.sf.sofarmusic.online.artist.viewholder;

import android.view.View;
import android.widget.TextView;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.ArtistSongAdapter;
import com.sf.sofarmusic.play.PlayTimeUtil;
import com.sf.sofarmusic.play.core.PlayDataHolder;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.widget.recyclerview.RecyclerViewHolder;

import org.greenrobot.eventbus.EventBus;

public class ArtistSongViewHolder extends RecyclerViewHolder<Song> {

  SofarImageView cover;
  TextView nameTv;
  TextView albumTv;
  TextView durationTv;
  TextView voiceTv;

  ArtistSongAdapter adapter;

  public ArtistSongViewHolder(View itemView, ArtistSongAdapter adapter) {
    super(itemView);
    this.adapter = adapter;
  }

  @Override
  protected void onCreateView(View itemView) {
    cover = itemView.findViewById(R.id.cover);
    nameTv = itemView.findViewById(R.id.name_tv);
    albumTv = itemView.findViewById(R.id.album_tv);
    durationTv = itemView.findViewById(R.id.duration_tv);
    voiceTv = itemView.findViewById(R.id.voice_tv);
  }

  @Override
  protected void onBindData(Song item) {
    cover.bindUrl(item.smallThumbUrl);
    nameTv.setText(item.name);
    albumTv.setText(item.albumTitle);
    durationTv.setText(PlayTimeUtil.getFormatTimeStr(item.length * 1000));

    if (item.play) {
      voiceTv.setVisibility(View.VISIBLE);
    } else {
      voiceTv.setVisibility(View.GONE);
    }

    itemView.setOnClickListener(v -> {
      adapter.selectSong(getViewAdapterPosition());
      PlayDataHolder.getInstance().setSongs(adapter.getList());
      EventBus.getDefault().post(new PlayEvent.SelectSongEvent(item));
    });
  }
}
