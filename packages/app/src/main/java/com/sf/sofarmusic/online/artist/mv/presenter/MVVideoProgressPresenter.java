package com.sf.sofarmusic.online.artist.mv.presenter;

import android.widget.TextView;
import com.sf.base.mvp.Presenter;
import com.sf.base.video.VideoPlayer;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.play.PlayTimeUtil;
import com.sf.widget.progress.DoubleMusicProgress;
import java.util.Timer;
import java.util.TimerTask;

public class MVVideoProgressPresenter extends Presenter<VideoPlayer> {

  TextView currentTv;
  TextView durationTv;
  DoubleMusicProgress videoProgress;

  Timer timer;
  VideoPlayer player;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopTimer();
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    currentTv = getView().findViewById(R.id.current_tv);
    durationTv = getView().findViewById(R.id.duration_tv);
    videoProgress = getView().findViewById(R.id.video_pb);
    videoProgress.setOnProgressListener(new DoubleMusicProgress.OnProgressListener() {
      @Override
      public void onProgress(int progress) {
        if (player != null) {
          player.seekTo(progress);
        }
      }

      @Override
      public void onDragFinish(int progress) {

      }
    });
  }

  @Override
  protected void onBind(VideoPlayer model, Object callerContext) {
    super.onBind(model, callerContext);
    if (player == null) {
      player = model;
    }
    startTimer();
  }

  private void updateUI() {
    long curPosition = player.getCurrentPosition();
    long totalDuration = player.getTotalDuration();
    if (totalDuration < 0) {
      return;
    }

    currentTv.setText(PlayTimeUtil.getFormatTimeStr(curPosition));
    durationTv.setText(PlayTimeUtil.getFormatTimeStr(totalDuration));

    int progress = (int) (1.0f * curPosition / totalDuration * 100);
    videoProgress.setProgress(progress);
    videoProgress.setSecondaryProgress(player.getBufferPercentage());
  }

  private void startTimer() {
    stopTimer();
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (getActivity() != null) {
          getActivity().runOnUiThread(() -> {
            updateUI();
          });
        }
      }
    }, 1000, 1000);
  }

  private void stopTimer() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }
}
