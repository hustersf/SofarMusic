package com.sf.sofarmusic.online.rank.presenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.detail.RankDetailFragment;
import com.sf.sofarmusic.online.rank.model.RankDetailResponse;

public class RankDetailTitlePresenter extends Presenter<RankDetailResponse> {

  TextView backTv;
  TextView titleTv;
  TextView menuTv;
  TextView searchTv;

  LinearLayout titleLayout;
  ImageView headIv;

  AppBarLayout appBarLayout;

  private AppBarLayout.OnOffsetChangedListener listener =
      new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
          if (titleLayout.getHeight() == 0 || headIv.getHeight() == 0
              || getModel().billboard.mainColor == 0) {
            return;
          }

          int y = -verticalOffset;
          if (y > headIv.getHeight() - titleLayout.getHeight()) {
            y = headIv.getHeight() - titleLayout.getHeight();
          }

          int alpha = (int) ((y * 1.0f) / (headIv.getHeight() - titleLayout.getHeight()) * 255);
          if (alpha > 125) {
            titleTv.setVisibility(View.VISIBLE);
          } else {
            titleTv.setVisibility(View.GONE);
          }

          titleLayout.setBackgroundColor(getModel().billboard.mainColor);
          titleLayout.getBackground().setAlpha(alpha);
        }
      };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    appBarLayout.removeOnOffsetChangedListener(listener);
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    backTv = getView().findViewById(R.id.back_tv);
    titleTv = getView().findViewById(R.id.title_tv);
    menuTv = getView().findViewById(R.id.menu_tv);
    searchTv = getView().findViewById(R.id.search_tv);

    titleLayout = getView().findViewById(R.id.title_ll);
    headIv = getView().findViewById(R.id.rank_head_bg_iv);

    appBarLayout = getView().findViewById(R.id.app_bar);
    appBarLayout.addOnOffsetChangedListener(listener);

    backTv.setOnClickListener(v -> {
      if (getCallerContext() instanceof RankDetailFragment) {
        ((RankDetailFragment) getCallerContext()).activity.finish();
      }
    });

  }

  @Override
  protected void onBind(RankDetailResponse model, Object callerContext) {
    super.onBind(model, callerContext);
    titleTv.setText(model.billboard.name);
  }
}
