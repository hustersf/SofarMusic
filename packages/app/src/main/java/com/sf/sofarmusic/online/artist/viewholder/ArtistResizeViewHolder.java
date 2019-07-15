package com.sf.sofarmusic.online.artist.viewholder;

import android.view.View;
import android.view.ViewGroup;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.widget.bitmap.round.RoundImageView;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistResizeViewHolder extends RecyclerViewHolder<Artist> {

  RoundImageView artistIv;

  public ArtistResizeViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    artistIv = itemView.findViewById(R.id.artist_iv);
  }

  @Override
  protected void onBindData(Artist item) {
    ViewGroup.LayoutParams lp = artistIv.getLayoutParams();
    int spanCount = 4;
    int itemWidth = (DeviceUtil.getMetricsWidth(getContext())
        - DensityUtil.dp2px(getContext(), 2 * 16 + (spanCount - 1) * 12)) / spanCount;
    lp.width = itemWidth;
    lp.height = itemWidth;
    artistIv.setLayoutParams(lp);
  }
}
