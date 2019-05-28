package com.sf.sofarmusic.online.rank.presenter;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.RankDetailAdapter;
import com.sf.sofarmusic.online.rank.detail.RankDetailFragment;
import com.sf.sofarmusic.online.rank.model.RankDetailResponse;

public class RankDetailHeadPresenter extends Presenter<RankDetailResponse> {

  ImageView headBgIv;
  TextView countTv;
  RelativeLayout playRl;


  @Override
  protected void onCreate() {
    super.onCreate();
    headBgIv = getView().findViewById(R.id.rank_head_bg_iv);
    countTv = getView().findViewById(R.id.count_tv);
    playRl = getView().findViewById(R.id.play_rl);

    playRl.setOnClickListener(v -> {
      if (getCallerContext() instanceof RankDetailFragment
          && ((RankDetailFragment) getCallerContext())
              .getOriginAdapter() instanceof RankDetailAdapter) {

        ((RankDetailAdapter) ((RankDetailFragment) getCallerContext()).getOriginAdapter())
            .selectSong(0);
      }
    });
  }

  @Override
  protected void onBind(RankDetailResponse model, Object callerContext) {
    super.onBind(model, callerContext);
    countTv.setText("(共" + model.songs.size() + "首)");

    if (model.billboard == null) {
      return;
    }

    Glide.with(getContext()).load(model.billboard.bigCoverUrl).asBitmap()
        .placeholder(R.drawable.placeholder_disk_210).into(new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap bitmap,
              GlideAnimation<? super Bitmap> glideAnimation) {
            headBgIv.setImageBitmap(bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
              @Override
              public void onGenerated(Palette palette) {
                int defaultColor = getContext().getResources().getColor(R.color.transparent);
                int dominantColor = palette.getDominantColor(defaultColor);
                model.billboard.mainColor = dominantColor;

              }
            });
          }
        });
  }
}
