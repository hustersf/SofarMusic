package com.sf.sofarmusic.online.recommend.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.model.Feed;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class FeedImageResizeViewHolder extends RecyclerViewHolder<Feed> {

  ImageView feedIv;

  public FeedImageResizeViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    feedIv = itemView.findViewById(R.id.feed_iv);
  }

  @Override
  protected void onBindData(Feed item) {
    ViewGroup.LayoutParams feedLp = feedIv.getLayoutParams();
    int showCount = 2;
    int itemWidth =
        (DeviceUtil.getMetricsWidth(getContext())
            - DensityUtil.dp2px(getContext(), 2 * 16 + showCount * 5))
            / showCount;
    feedLp.width = itemWidth;
    feedLp.height = itemWidth;
    feedIv.setLayoutParams(feedLp);
  }
}
