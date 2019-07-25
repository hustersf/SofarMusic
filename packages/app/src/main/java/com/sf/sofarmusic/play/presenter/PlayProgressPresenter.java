package com.sf.sofarmusic.play.presenter;

import android.media.MediaPlayer;
import android.widget.TextView;
import com.sf.base.mvp.Presenter;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayTimeUtil;
import com.sf.sofarmusic.play.core.MusicPlayCallbackAdapter;
import com.sf.sofarmusic.play.core.MusicPlayer;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.widget.progress.DoubleMusicProgress;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放进度相关
 */
@BindEventBus
public class PlayProgressPresenter extends Presenter<List<Song>> {

  DoubleMusicProgress progressView;
  TextView finishTv;
  TextView totalTv;

  private Timer timer;

  private MusicPlayer playerHelper;
  private MusicPlayCallbackAdapter callback = new MusicPlayCallbackAdapter() {
    @Override
    public void onPlayStart(MediaPlayer mp) {
      super.onPlayStart(mp);
      startTimer();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
      super.onBufferingUpdate(mp, percent);
      progressView.setSecondaryProgress(percent);
    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (playerHelper != null) {
      playerHelper.removeMusicPlayCallback(callback);
    }
    stopTimer();
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    progressView = getView().findViewById(R.id.music_progress);
    finishTv = getView().findViewById(R.id.finish_tv);
    totalTv = getView().findViewById(R.id.total_tv);

    progressView.setOnProgressListener(new DoubleMusicProgress.OnProgressListener() {
      @Override
      public void onProgress(int progress) {
        // 指定播放进度
        if (playerHelper != null) {
          playerHelper.seekTo(progress);
        }
      }

      @Override
      public void onDragFinish(int progress) {
        EventBus.getDefault().post(new PlayEvent.PlayProgressDragEvent());
      }
    });
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);

    // 初始化
    progressView.setSecondaryProgress(0); // 下载进度
    progressView.setProgress(0); // 播放进度
    finishTv.setText("00:00");
    totalTv.setText("00:00");
  }

  @Subscribe
  public void onPlayServiceConnectedEvent(PlayEvent.PlayServiceConnected event) {
    if (event.playerHelper == null) {
      return;
    }

    playerHelper = event.playerHelper;
    playerHelper.removeMusicPlayCallback(callback);
    playerHelper.addMusicPlayCallback(callback);
    if (playerHelper.isPrepared()) {
      startTimer();
    }
  }

  private void startTimer() {
    stopTimer();
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (getActivity() != null) {
          getActivity().runOnUiThread(() -> {
            formatTime();
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

  private void formatTime() {
    long curPosition = playerHelper.getCurrentPosition();
    long totalDuration = playerHelper.getTotalDuration();

    if (totalDuration == 0) {
      return;
    }

    finishTv.setText(PlayTimeUtil.getFormatTimeStr(curPosition));
    totalTv.setText(PlayTimeUtil.getFormatTimeStr(totalDuration));

    int progress = (int) (1.0f * curPosition / totalDuration * 100);
    progressView.setProgress(progress);
  }
}
