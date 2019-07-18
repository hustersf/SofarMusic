package com.sf.sofarmusic.online.artist.presenter;

import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Album;
import com.sf.utility.CollectionUtil;

public class AlbumHeadPresenter extends Presenter<Album> {

  SofarImageView cover;
  TextView albumTv;
  TextView timeTv;

  TextView authorTv;
  SofarImageView authorCover;

  @Override
  protected void onCreate() {
    super.onCreate();
    albumTv = getView().findViewById(R.id.album_tv);
    cover = getView().findViewById(R.id.album_cover);
    timeTv = getView().findViewById(R.id.time_tv);
    authorTv = getView().findViewById(R.id.author_tv);
    authorCover = getView().findViewById(R.id.author_iv);
  }

  @Override
  protected void onBind(Album model, Object callerContext) {
    super.onBind(model, callerContext);
    cover.bindUrl(model.albumBackground);
    albumTv.setText(model.name);
    timeTv.setText(model.publishTime);
    authorTv.setText(model.author);
    if (!CollectionUtil.isEmpty(model.artists)) {
      authorCover.bindUrl(model.artists.get(0).avatarSmallUrl);
    }
  }
}
