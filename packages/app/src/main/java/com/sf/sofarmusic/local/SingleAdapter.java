package com.sf.sofarmusic.local;
import android.view.View;
import android.widget.TextView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

/**
 * Created by sufan on 16/11/23.
 */
public class SingleAdapter extends RecyclerAdapter<Song> {


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
    return R.layout.adapter_single_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new SingleItemHolder(itemView);
  }


  class SingleItemHolder extends RecyclerViewHolder<Song> {

    TextView voiceTv, nameTv, playerTv, dotTv;

    public SingleItemHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      voiceTv = itemView.findViewById(R.id.voice_tv);
      nameTv = itemView.findViewById(R.id.name_tv);
      playerTv = itemView.findViewById(R.id.player_tv);
      dotTv = itemView.findViewById(R.id.dot_tv);
    }

    @Override
    protected void onBindData(Song item, RecyclerViewHolder holder) {
      nameTv.setText(item.name);
      playerTv.setText(item.author);
      if (item.play) {
        voiceTv.setVisibility(View.VISIBLE);
      } else {
        voiceTv.setVisibility(View.GONE);
      }

      itemView.setOnClickListener(v -> {
        selectSong(holder.viewAdapterPosition);
      });
    }
  }
}
