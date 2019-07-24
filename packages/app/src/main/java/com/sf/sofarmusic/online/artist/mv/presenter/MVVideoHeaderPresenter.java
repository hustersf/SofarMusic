package com.sf.sofarmusic.online.artist.mv.presenter;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.widget.TextView;
import com.sf.base.BaseActivity;
import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.artist.model.MVDetailResponse;

public class MVVideoHeaderPresenter extends Presenter<MVDetailResponse> {
  TextView headBack;
  TextView headTitle;

  @Override
  protected void onCreate() {
    super.onCreate();
    headBack = getView().findViewById(R.id.head_back);
    headTitle = getView().findViewById(R.id.head_title);

    headBack.setOnClickListener(v -> {
      if (getActivity() != null) {
        Configuration configuration = getActivity().getResources().getConfiguration();
        int ori = configuration.orientation;
        if (ori == configuration.ORIENTATION_LANDSCAPE) {
          getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
          getActivity().finish();
        }
      }
    });
  }

  @Override
  protected void onBind(MVDetailResponse model, Object callerContext) {
    super.onBind(model, callerContext);
    headTitle.setVisibility(View.GONE);
    headTitle.setText(model.mv.name);

    if (getActivity() instanceof BaseActivity) {
      ((BaseActivity) getActivity()).addOnConfigurationChangedListener(newConfig -> {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
          headTitle.setVisibility(View.VISIBLE);
        } else {
          headTitle.setVisibility(View.GONE);
        }
      });
    }
  }
}
