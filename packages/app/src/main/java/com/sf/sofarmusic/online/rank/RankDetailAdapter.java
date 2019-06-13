package com.sf.sofarmusic.online.rank;

import android.view.View;
import android.widget.TextView;

import com.sf.libskin.config.SkinConfig;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayDataHolder;
import com.sf.sofarmusic.play.PlayEvent;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

import org.greenrobot.eventbus.EventBus;

public class RankDetailAdapter extends RecyclerAdapter<Song> {

  private int selectedPosition = -1; // 选中

  /**
   * position 选中歌曲的位置
   */
  public void selectSong(int position) {
    if (position == -1 || selectedPosition == position) {
      return;
    }

    if (selectedPosition != -1) {
      getList().get(selectedPosition).play = false;
      notifyItemChanged(selectedPosition);
    }

    selectedPosition = position;
    getList().get(selectedPosition).play = true;
    notifyItemChanged(selectedPosition);
  }

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.rank_detail_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new RankDetailViewHolder(itemView);
  }

  class RankDetailViewHolder extends RecyclerViewHolder<Song> {

    TextView orderTv;
    TextView voiceTv;
    TextView nameTv;
    TextView artistTv;

    public RankDetailViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      orderTv = itemView.findViewById(R.id.order_tv);
      voiceTv = itemView.findViewById(R.id.voice_tv);
      nameTv = itemView.findViewById(R.id.name_tv);
      artistTv = itemView.findViewById(R.id.artist_tv);
    }

    @Override
    protected void onBindData(Song item, RecyclerViewHolder holder) {
      int order = holder.getAdapterPosition() + 1;
      if (order <= 3) {
        orderTv.setTextColor(SkinConfig.skinColor);
      } else {
        orderTv.setTextColor(context.getResources().getColor(R.color.text_gray));
      }
      orderTv.setText(String.valueOf(order));

      nameTv.setText(item.name);
      artistTv.setText(item.author);

      if (item.play) {
        orderTv.setVisibility(View.GONE);
        voiceTv.setVisibility(View.VISIBLE);
      } else {
        orderTv.setVisibility(View.VISIBLE);
        voiceTv.setVisibility(View.GONE);
      }

      itemView.setOnClickListener(v -> {
        selectSong(holder.getAdapterPosition());
        PlayDataHolder.getInstance().setSongs(getList());
        EventBus.getDefault().post(new PlayEvent.SelectSongEvent(item));
      });
    }
  }
}
