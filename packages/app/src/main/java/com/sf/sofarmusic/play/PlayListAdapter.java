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
        getList().get(position).play = false; // 删除之前先将其置为false,很重要
        getList().remove(position);
        notifyItemRemoved(position); // 该方法不会使position及其之后位置的vitemiew重新onBindViewHolder
        notifyItemRangeChanged(0, getList().size()); // 很重要，否则当前position之后的item得不到正确的postion值

        // 每删除一个，必须要重新确定mSelectedPos的值
        initSelectedPos();
        // 如果是最后一首选中第一首
        // Log.i("TAG","mSelectedPos:"+mSelectedPos+" realPosition："+realPosition+"
        // size:"+mList.size());
        if (getList().size() == 0) {
          // mOnItemClickListener.onItemClick(realPosition, "three");
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
          mOnItemClickListener.onItemClick(position);
        }
      });
    }

  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;

  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  private void initSelectedPos() {
    for (int i = 0; i < getList().size(); i++) {
      if (getList().get(i).play) {
        selectedPosition = i;
      }
    }
  }

}
