package com.sf.sofarmusic.online.artist.album;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.viewholder.AlbumDetailViewHolder;
import com.sf.sofarmusic.online.artist.viewholder.ArtistSongMoreViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class AlbumDetailAdapter extends RecyclerAdapter<Song> {

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
    return R.layout.album_song_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new AlbumDetailViewHolder(itemView, this));
    holder.addViewHolder(new ArtistSongMoreViewHolder(itemView));
    return holder;
  }
}
