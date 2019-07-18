package com.sf.sofarmusic.online.artist.viewholder;

import org.greenrobot.eventbus.EventBus;

import android.view.View;
import android.widget.TextView;

import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.album.AlbumDetailAdapter;
import com.sf.sofarmusic.play.PlayTimeUtil;
import com.sf.sofarmusic.play.core.PlayDataHolder;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.utility.ToastUtil;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class AlbumDetailViewHolder extends RecyclerViewHolder<Song> {

  SofarImageView cover;
  TextView nameTv;
  TextView artistTv;
  TextView durationTv;
  TextView voiceTv;

  AlbumDetailAdapter adapter;

  public AlbumDetailViewHolder(View itemView, AlbumDetailAdapter adapter) {
    super(itemView);
    this.adapter = adapter;
  }

  @Override
  protected void onCreateView(View itemView) {
    cover = itemView.findViewById(R.id.cover);
    nameTv = itemView.findViewById(R.id.name_tv);
    artistTv = itemView.findViewById(R.id.artist_tv);
    durationTv = itemView.findViewById(R.id.duration_tv);
    voiceTv = itemView.findViewById(R.id.voice_tv);
  }

  @Override
  protected void onBindData(Song item) {
    nameTv.setText(item.name);
    artistTv.setText(item.author);
    durationTv.setText(PlayTimeUtil.getFormatTimeStr(item.length * 1000));

    if (item.play) {
      voiceTv.setVisibility(View.VISIBLE);
    } else {
      voiceTv.setVisibility(View.GONE);
    }

    if (item.delStatus == 1) {
      nameTv.setTextColor(getContext().getResources().getColor(R.color.light_gray));
      artistTv.setTextColor(getContext().getResources().getColor(R.color.light_gray));
    } else {
      nameTv.setTextColor(getContext().getResources().getColor(R.color.text_black));
      artistTv.setTextColor(getContext().getResources().getColor(R.color.text_gray));
    }

    itemView.setOnClickListener(v -> {
      if (item.delStatus == 1) {
        ToastUtil.startShort(getContext(), "版权问题，无法播放");
        return;
      }
      adapter.selectSong(getViewAdapterPosition());
      PlayDataHolder.getInstance().setSongs(adapter.getList());
      EventBus.getDefault().post(new PlayEvent.SelectSongEvent(item));
    });
  }
}
