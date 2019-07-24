package com.sf.sofarmusic.online.artist.presenter;

import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.artist.model.MVDetailResponse;

public class MVInfoPresenter extends Presenter<MVDetailResponse> {

  TextView nameTv;
  TextView authorTv;
  TextView playCountTv;

  @Override
  protected void onCreate() {
    super.onCreate();
    nameTv = getView().findViewById(R.id.name_tv);
    authorTv = getView().findViewById(R.id.author_tv);
    playCountTv = getView().findViewById(R.id.play_count_tv);
  }

  @Override
  protected void onBind(MVDetailResponse model, Object callerContext) {
    super.onBind(model, callerContext);

    if (model.mv == null) {
      return;
    }

    nameTv.setText(model.mv.name);
    authorTv.setText(model.mv.artist);
    playCountTv.setText(model.mv.playCount + "次播放");

  }
}
