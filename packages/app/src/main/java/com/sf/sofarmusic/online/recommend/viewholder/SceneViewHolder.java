package com.sf.sofarmusic.online.recommend.viewholder;

import android.graphics.Color;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sf.demo.viewpager.transformer.ZoomOutPageTransformer;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.SceneAdapter;
import com.sf.sofarmusic.online.recommend.model.Feed;
import com.sf.sofarmusic.online.recommend.model.FeedGroup;
import com.sf.utility.CollectionUtil;
import com.sf.widget.banner.BannerIndicator;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SceneViewHolder extends RecyclerViewHolder<FeedGroup> {

  LinearLayout sceneLayout;
  TextView titleTv;
  TextView titleMoreTv;

  ViewPager sceneVp;
  SceneAdapter adapter;
  BannerIndicator sceneIndicator;

  public SceneViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    sceneLayout = itemView.findViewById(R.id.scene_layout);
    titleTv = itemView.findViewById(R.id.title_tv);
    titleMoreTv = itemView.findViewById(R.id.title_more_tv);
    sceneVp = itemView.findViewById(R.id.scene_vp);
    sceneIndicator = itemView.findViewById(R.id.scene_indicator);

    sceneVp.setPageTransformer(true, new ZoomOutPageTransformer());
    sceneVp.setOffscreenPageLimit(2);
    // sceneVp.setPageMargin(DensityUtil.dp2px(getContext(),10));
  }

  @Override
  protected void onBindData(FeedGroup item) {
    if (CollectionUtil.isEmpty(item.feeds)) {
      sceneLayout.setVisibility(View.GONE);
      return;
    }


    setFeedColor(item.feeds.get(0));
    titleTv.setText(item.title);
    titleMoreTv.setText(item.titleMore);

    if (item.feeds.size() > 1) {
      sceneIndicator.setVisibility(View.VISIBLE);
    } else {
      sceneIndicator.setVisibility(View.GONE);
    }
    adapter = new SceneAdapter(getContext(), item.feeds);
    adapter.bindHost(sceneVp, sceneIndicator);
    adapter.setOnPageSelectListener(position -> {
      setFeedColor(item.feeds.get(position));
    });
  }

  private void setFeedColor(Feed feed) {
    if (feed == null) {
      return;
    }
    String bgColor = "FFFFFF";
    if (!TextUtils.isEmpty(feed.bgColor)) {
      bgColor = feed.bgColor;
    }
    sceneLayout.setBackgroundColor(Color.parseColor("#" + bgColor));
  }
}
