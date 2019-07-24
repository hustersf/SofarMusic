package com.sf.sofarmusic.online.artist.presenter;

import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.artist.model.MVDetailResponse;
import com.sf.utility.ToastUtil;

public class MVSharePresenter extends Presenter<MVDetailResponse> {

  TextView shareTv;

  @Override
  protected void onCreate() {
    super.onCreate();
    shareTv = getView().findViewById(R.id.mv_share_tv);
  }

  @Override
  protected void onBind(MVDetailResponse model, Object callerContext) {
    super.onBind(model, callerContext);

    shareTv.setOnClickListener(v -> {
      ToastUtil.startShort(getContext(), "分享");
    });
  }
}
