package com.sf.sofarmusic.online.artist.mv.presenter;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.widget.ImageView;
import com.sf.base.BaseActivity;
import com.sf.base.mvp.Presenter;
import com.sf.base.video.ActivityOrientationControl;
import com.sf.base.video.VideoPlayer;
import com.sf.sofarmusic.R;

public class MVVideoOrientationPresenter extends Presenter<VideoPlayer> {

  ImageView videoLandIv;

  ActivityOrientationControl orientationControl;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (orientationControl != null) {
      orientationControl.disable();
    }
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    videoLandIv = getView().findViewById(R.id.video_land_iv);
    videoLandIv.setOnClickListener(v -> {
      switchOrientation();
    });
  }

  @Override
  protected void onBind(VideoPlayer model, Object callerContext) {
    super.onBind(model, callerContext);

    // 监听屏幕转化
    if (getActivity() instanceof BaseActivity) {
      ((BaseActivity) getActivity()).addOnConfigurationChangedListener(newConfig -> {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
          videoLandIv.setImageResource(R.drawable.video_switch_port);
        } else {
          videoLandIv.setImageResource(R.drawable.video_switch_land);
        }
      });

      orientationControl = new ActivityOrientationControl(getActivity());
      orientationControl.enable();
    }


  }

  private void switchOrientation() {
    if (getActivity() == null) {
      return;
    }

    Configuration configuration = getActivity().getResources().getConfiguration();
    int ori = configuration.orientation;

    if (ori == configuration.ORIENTATION_PORTRAIT) {
      getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    } else {
      getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }
}
