package com.sf.sofarmusic.play;

import android.view.View;
import android.widget.TextView;

import com.sf.libskin.config.SkinConfig;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

import java.util.List;

public class PlayListAdapter extends RecyclerAdapter<Song> {

  private OnItemClickListener mOnItemClickListener;

  private int selectedPosition = -1; // 选中

  @Override
  public void setList(List<Song> datas) {
    super.setList(datas);
    initSelectedPos();
  }

  @Override
  public void setListWithRelated(List<Song> datas) {
    super.setListWithRelated(datas);
    initSelectedPos();
  }

  public int getSelectedPosition() {
    return selectedPosition;
  }

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
    return R.layout.adapter_sheet_play_list;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new ItemViewHolder(itemView);
  }

  class ItemViewHolder extends RecyclerViewHolder<Song> {

    TextView imgTv;
    TextView xxTv;
    TextView contentTv;

    public ItemViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      imgTv = itemView.findViewById(R.id.img_tv);
      xxTv = itemView.findViewById(R.id.xx_tv);
      contentTv = itemView.findViewById(R.id.content_tv);
    }

    @Override
    protected void onBindData(Song item, RecyclerViewHolder holder) {
      contentTv.setText(item.name + "-" + item.author);
      if (item.play) {
        imgTv.setVisibility(View.VISIBLE);
        contentTv.setTextColor(SkinConfig.skinColor);
      } else {
        imgTv.setVisibility(View.GONE);
        contentTv.setTextColor(getContext().getResources().getColor(R.color.text_gray));
      }

      holder.itemView.setOnClickListener(v -> {
        selectSong(holder.getAdapterPosition());
        if (mOnItemClickListener != null) {
          mOnItemClickListener.onItemClick(holder.getAdapterPosition());
        }
      });

      xxTv.setOnClickListener(v -> {
        // 删除某一条item
        int position = holder.getAdapterPosition();
        Song deleteSong = getList().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getList().size());

        // 每删除一个，必须要重新确定selectedPosition的值
        initSelectedPos();
        if (getList().size() == 0) {

        } else if (selectedPosition == getList().size()) {
          selectedPosition = 0;
          getList().get(selectedPosition).play = true;
          notifyItemChanged(selectedPosition);
        } else if (selectedPosition == position) {
          // 删除之后，自动选中下一首
          selectedPosition = position;
          getList().get(selectedPosition).play = true;
          notifyItemChanged(selectedPosition);
        }

        if (mOnItemClickListener != null) {
          mOnItemClickListener.onDeleteSong(deleteSong);
        }
      });
    }

  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;

  }

  public interface OnItemClickListener {

    /**
     * 
     * @param position 点击的歌曲的位置
     */
    void onItemClick(int position);

    /**
     * 
     * @param song 被删除的歌曲
     */
    void onDeleteSong(Song song);
  }

  private void initSelectedPos() {
    for (int i = 0; i < getList().size(); i++) {
      if (getList().get(i).play) {
        selectedPosition = i;
      }
    }
  }

}
