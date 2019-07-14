package com.sf.sofarmusic.online.recommend.presenter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.model.Feed;

public class SceneItemPresenter extends Presenter<Feed> {

  ImageView sceneIv;
  TextView titleTv;
  TextView enTitleTv;

  @Override
  protected void onCreate() {
    super.onCreate();
    sceneIv = getView().findViewById(R.id.scene_iv);
    titleTv = getView().findViewById(R.id.title_tv);
    enTitleTv = getView().findViewById(R.id.en_title_tv);
  }

  @Override
  protected void onBind(Feed model, Object callerContext) {
    super.onBind(model, callerContext);
    Glide.with(getContext()).load(model.thumbUrl).into(sceneIv);
    titleTv.setText(model.title);
    enTitleTv.setText(model.enTitle);
  }
}
