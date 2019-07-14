package com.sf.sofarmusic.online.recommend;

import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.model.Feed;
import com.sf.sofarmusic.online.recommend.viewholder.FeedClickViewHolder;
import com.sf.sofarmusic.online.recommend.viewholder.FeedCoverViewHolder;
import com.sf.sofarmusic.online.recommend.viewholder.FeedImageResizeViewHolder;
import com.sf.widget.recyclerview.MultiRecyclerViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class FeedItemAdapter extends RecyclerAdapter<Feed> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.feed_item_image;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    MultiRecyclerViewHolder holder = new MultiRecyclerViewHolder(itemView);
    holder.addViewHolder(new FeedCoverViewHolder(itemView));
    holder.addViewHolder(new FeedClickViewHolder(itemView));
    holder.addViewHolder(new FeedImageResizeViewHolder(itemView));
    return holder;
  }
}
