package com.sf.sofarmusic.online.artist.presenter;

import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;

public class ArtistHeadPresenter extends Presenter<Artist> {

  TextView nameTv;
  SofarImageView cover;

  @Override
  protected void onCreate() {
    super.onCreate();
    nameTv = getView().findViewById(R.id.name_tv);
    cover = getView().findViewById(R.id.cover);
  }

  @Override
  protected void onBind(Artist model, Object callerContext) {
    super.onBind(model, callerContext);
    cover.bindUrl(model.avatarBigUrl);
    nameTv.setText(model.name);
  }
}
