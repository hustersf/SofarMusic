package com.sf.sofarmusic.search.category;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.viewholder.SearchAllItemViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchAllAdapter extends RecyclerAdapter<SearchAllInfo> {
  @Override
  protected int getItemLayoutId(int viewType) {
    if (viewType == SearchAllInfo.TYPE_PLAY) {
      return R.layout.search_play_item;
    } else if (viewType == SearchAllInfo.TYPE_SONG) {
      return R.layout.search_song_item;
    } else if (viewType == SearchAllInfo.TYPE_ALBUM) {
      return R.layout.search_album_item;
    } else if (viewType == SearchAllInfo.TYPE_ARTIST) {
      return R.layout.search_artist_item;
    }
    return R.layout.feed_group_item_unsupport;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new SearchAllItemViewHolder(itemView));
    return holder;
  }

  @Override
  public int getItemViewType(int position) {
    SearchAllInfo item = getItem(position);
    return item.searchContentType;
  }
}
