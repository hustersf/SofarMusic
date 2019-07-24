package com.sf.sofarmusic.online.artist.mv.presenter;

import android.view.View;
import android.widget.ImageView;
import com.sf.base.mvp.Presenter;
import com.sf.base.video.VideoControlEvent;
import com.sf.base.video.VideoPlayer;
import com.sf.sofarmusic.R;
import org.greenrobot.eventbus.EventBus;
import java.util.Timer;
import java.util.TimerTask;

public class MVVideoControlPresenter extends Presenter<VideoPlayer> {

  View videoRootView;
  View playControlLayout;

  ImageView playStopIv;
  boolean play = true;

  Timer timer;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopTimer();
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    videoRootView = getView().findViewById(R.id.video_root_layout);
    playControlLayout = getView().findViewById(R.id.play_control_panel);
    playStopIv = getView().findViewById(R.id.play_stop_iv);

    videoRootView.setOnClickListener(v -> {
      if (playControlLayout.getVisibility() == View.GONE) {
        playControlLayout.setVisibility(View.VISIBLE);
        if (play) {
          startTimer();
        }
      } else {
        playControlLayout.setVisibility(View.GONE);
      }
    });

    playStopIv.setOnClickListener(v -> {
      if (play) {
        stopTimer();
        playStopIv.setImageResource(R.drawable.video_icon_play);
        EventBus.getDefault().post(new VideoControlEvent.PauseEvent());
      } else {
        startTimer();
        playStopIv.setImageResource(R.drawable.video_icon_stop);
        EventBus.getDefault().post(new VideoControlEvent.PlayEvent());
      }
      play = !play;
    });
  }

  @Override
  protected void onBind(VideoPlayer model, Object callerContext) {
    super.onBind(model, callerContext);
  }


  private void startTimer() {
    stopTimer();
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (getActivity() != null) {
          getActivity().runOnUiThread(() -> {
            playControlLayout.setVisibility(View.GONE);
          });
        }
      }
    }, 3000);
  }

  private void stopTimer() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}
