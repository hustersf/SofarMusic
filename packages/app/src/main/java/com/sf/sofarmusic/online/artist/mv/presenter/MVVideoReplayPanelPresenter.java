package com.sf.sofarmusic.online.artist.mv.presenter;

import android.media.MediaPlayer;
import android.view.View;
import com.sf.base.mvp.Presenter;
import com.sf.base.video.VideoControlEvent;
import com.sf.base.video.VideoPlayCallbackAdapter;
import com.sf.base.video.VideoPlayer;
import com.sf.sofarmusic.R;
import org.greenrobot.eventbus.EventBus;

public class MVVideoReplayPanelPresenter extends Presenter<VideoPlayer> {

  View videoRootView;
  View replayPanel;
  View replayLayout;

  VideoPlayer player;

  VideoPlayCallbackAdapter callback = new VideoPlayCallbackAdapter() {
    @Override
    public void onCompletion(MediaPlayer mp) {
      super.onCompletion(mp);
      videoRootView.setEnabled(false);
      replayPanel.setVisibility(View.VISIBLE);
    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (player != null) {
      player.removeVideoPlayCallback(callback);
    }

  }

  @Override
  protected void onCreate() {
    super.onCreate();
    videoRootView = getView().findViewById(R.id.video_root_layout);
    replayPanel = getView().findViewById(R.id.replay_panel);
    replayLayout = getView().findViewById(R.id.replay_layout);

    replayLayout.setOnClickListener(v -> {
      videoRootView.setEnabled(true);
      replayPanel.setVisibility(View.GONE);
      EventBus.getDefault().post(new VideoControlEvent.PlayEvent());
    });
  }

  @Override
  protected void onBind(VideoPlayer model, Object callerContext) {
    super.onBind(model, callerContext);
    if (player == null) {
      player = model;
      player.addVideoPlayCallback(callback);
    }
  }

}
