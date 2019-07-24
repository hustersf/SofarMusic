package com.sf.sofarmusic.online.artist.presenter;

import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.sf.base.mvp.Presenter;
import com.sf.base.video.VideoPlayer;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.VideoFile;
import com.sf.sofarmusic.online.artist.model.MVDetailResponse;
import java.util.Map;

public class MVVideoPresenter extends Presenter<MVDetailResponse> {

  SurfaceView surfaceView;

  VideoPlayer player;

  MVDetailResponse response;

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

  @Override
  protected void onCreate() {
    super.onCreate();
    surfaceView = getView().findViewById(R.id.surface_view);
    surfaceView.getHolder().addCallback(callback);
    player = new VideoPlayer();
  }

  @Override
  protected void onBind(MVDetailResponse model, Object callerContext) {
    super.onBind(model, callerContext);
    response = model;
    if (player.surfaceAvailable()) {
      play();
    }
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
}
