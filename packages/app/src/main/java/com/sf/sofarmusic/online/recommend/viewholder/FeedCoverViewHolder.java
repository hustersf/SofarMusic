package com.sf.sofarmusic.online.recommend.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.model.Feed;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class FeedCoverViewHolder extends RecyclerViewHolder<Feed> {

  ImageView feedIv;
  TextView titleTv;
  TextView infoTv;
  ImageView authorIv;
  TextView authorTv;

  public FeedCoverViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    feedIv = itemView.findViewById(R.id.feed_iv);
    titleTv = itemView.findViewById(R.id.title_tv);
    infoTv = itemView.findViewById(R.id.info_tv);
    authorIv = itemView.findViewById(R.id.author_iv);
    authorTv = itemView.findViewById(R.id.author_tv);
  }

  @Override
  protected void onBindData(Feed item) {
    Glide.with(getContext()).load(item.thumbUrl).into(feedIv);
    titleTv.setText(item.title);
    infoTv.setText(item.songNum + "首单曲|" + item.genre);
    Glide.with(getContext()).load(item.authorThumbUrl).into(authorIv);
    authorTv.setText(item.author);
  }
}
