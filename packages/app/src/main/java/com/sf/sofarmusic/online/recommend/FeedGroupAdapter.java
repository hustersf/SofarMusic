package com.sf.sofarmusic.online.recommend;

import android.view.View;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.model.FeedGroup;
import com.sf.sofarmusic.online.recommend.viewholder.RecommendViewHolder;
import com.sf.sofarmusic.online.recommend.viewholder.SceneViewHolder;
import com.sf.sofarmusic.online.recommend.viewholder.UnsupportViewHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class FeedGroupAdapter extends RecyclerAdapter<FeedGroup> {

  @Override
  public int getItemViewType(int position) {
    FeedGroup group = getItem(position);
    if (group == null) {
      return 0;
    }

    if (group.style == FeedGroup.STYLE_RECOMMEND) {
      return FeedGroup.STYLE_RECOMMEND;
    } else if (group.style == FeedGroup.STYLE_SCENE) {
      return FeedGroup.STYLE_SCENE;
    }

    return FeedGroup.STYLE_UNSUPPORT;
  }

  @Override
  protected int getItemLayoutId(int viewType) {
    if (viewType == FeedGroup.STYLE_RECOMMEND) {
      return R.layout.feed_group_item_recommend;
    } else if (viewType == FeedGroup.STYLE_SCENE) {
      return R.layout.feed_group_item_scene;
    } else {
      return R.layout.feed_group_item_unsupport;
    }
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    if (viewType == FeedGroup.STYLE_RECOMMEND) {
      return new RecommendViewHolder(itemView);
    } else if (viewType == FeedGroup.STYLE_SCENE) {
      return new SceneViewHolder(itemView);
    } else {
      return new UnsupportViewHolder(itemView);
    }
  }
}
