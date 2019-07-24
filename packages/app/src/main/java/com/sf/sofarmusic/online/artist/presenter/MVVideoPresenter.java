package com.sf.sofarmusic.online.artist.presenter;

import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.sf.base.mvp.Presenter;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.base.video.VideoControlEvent;
import com.sf.base.video.VideoPlayer;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.VideoFile;
import com.sf.sofarmusic.online.artist.model.MVDetailResponse;
import com.sf.sofarmusic.online.artist.mv.presenter.MVVideoControlPresenter;
import com.sf.sofarmusic.online.artist.mv.presenter.MVVideoHeaderPresenter;
import com.sf.sofarmusic.online.artist.mv.presenter.MVVideoOrientationPresenter;
import com.sf.sofarmusic.online.artist.mv.presenter.MVVideoProgressPresenter;
import com.sf.sofarmusic.online.artist.mv.presenter.MVVideoReplayPanelPresenter;
import org.greenrobot.eventbus.Subscribe;
import java.util.Map;

@BindEventBus
public class MVVideoPresenter extends Presenter<MVDetailResponse> {

  SurfaceView surfaceView;
  VideoPlayer player;

  MVDetailResponse response;

  Presenter presenter;

  SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      player.setSurface(holder.getSurface());
      play();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
  };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (player != null) {
      player.stop();
    }
    surfaceView.getHolder().removeCallback(callback);
  }

  public MVVideoPresenter() {
    add(new MVVideoHeaderPresenter());

    presenter = new Presenter();
    presenter.add(new MVVideoControlPresenter());
    presenter.add(new MVVideoProgressPresenter());
    presenter.add(new MVVideoOrientationPresenter());
    presenter.add(new MVVideoReplayPanelPresenter());
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    surfaceView = getView().findViewById(R.id.surface_view);
    surfaceView.getHolder().addCallback(callback);
    player = new VideoPlayer();
    presenter.create(getView());
  }

  @Override
  protected void onBind(MVDetailResponse model, Object callerContext) {
    super.onBind(model, callerContext);
    response = model;
    if (player.surfaceAvailable()) {
      play();
    }
    presenter.bind(player, getCallerContext());
  }

  private void play() {
    if (response == null) {
      return;
    }

    Map<String, VideoFile> videoFileMap = response.videoFileMap;
    String key = "41";
    if (!TextUtils.isEmpty(response.maxDefinition)) {
      key = response.maxDefinition;
    }
    VideoFile videoFile = videoFileMap.get(key);
    player.play(videoFile.fileLink);
  }

  @Subscribe
  public void onVideoPlayEvent(VideoControlEvent.PlayEvent event) {
    play();
  }

  @Subscribe
  public void onVideoStopEvent(VideoControlEvent.PauseEvent event) {
    player.pause();
  }


}
