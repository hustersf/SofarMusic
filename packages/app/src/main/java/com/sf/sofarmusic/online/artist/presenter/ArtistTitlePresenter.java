package com.sf.sofarmusic.online.artist.presenter;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;

public class ArtistTitlePresenter extends Presenter<Artist> {

  TextView headBack;
  TextView headTitle;
  AppBarLayout appBarLayout;
  View headLayout;

  int headHeight = 0;

  @Override
  protected void onCreate() {
    super.onCreate();
    headBack = getView().findViewById(R.id.head_back);
    headTitle = getView().findViewById(R.id.head_title);
    appBarLayout = getView().findViewById(R.id.app_bar);
    headLayout = getView().findViewById(R.id.head_layout);
  }

  @Override
  protected void onBind(Artist model, Object callerContext) {
    super.onBind(model, callerContext);
    headBack.setOnClickListener(v -> {
      getActivity().finish();
    });

    headLayout.post(() -> {
      headHeight = headLayout.getHeight();
    });


    headTitle.setText(model.name);
    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (headHeight == 0) {
          return;
        }

        int y = -verticalOffset;
        if (y > headHeight) {
          headTitle.setVisibility(View.VISIBLE);
        } else {
          headTitle.setVisibility(View.GONE);
        }
      }
    });
  }
}
