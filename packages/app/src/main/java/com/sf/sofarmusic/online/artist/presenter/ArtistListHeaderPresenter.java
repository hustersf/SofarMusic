package com.sf.sofarmusic.online.artist.presenter;

import android.widget.TextView;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.artist.model.ArtistGroup;

public class ArtistListHeaderPresenter extends Presenter<ArtistGroup> {

  TextView headBackTv;
  TextView headTitleTv;

  @Override
  protected void onCreate() {
    super.onCreate();
    headBackTv = getView().findViewById(R.id.head_back);
    headTitleTv = getView().findViewById(R.id.head_title);
  }

  @Override
  protected void onBind(ArtistGroup model, Object callerContext) {
    super.onBind(model, callerContext);
    headBackTv.setOnClickListener(v -> {
      getActivity().finish();
    });

    headTitleTv.setText(model.name);
  }
}
