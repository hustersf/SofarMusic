package com.sf.sofarmusic.play.presenter;

import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.widget.progress.DoubleMusicProgress;

import java.util.List;

public class PlayProgressPresenter extends Presenter<List<Song>> {

  DoubleMusicProgress progress;
  TextView finishTv;
  TextView totalTv;

  @Override
  protected void onCreate() {
    super.onCreate();
    progress = getView().findViewById(R.id.music_progress);
    finishTv = getView().findViewById(R.id.finish_tv);
    totalTv = getView().findViewById(R.id.total_tv);

    progress.setOnProgressListener(new DoubleMusicProgress.OnProgressListener() {
      @Override
      public void onProgress(int progress) {
        // 指定播放进度
      }
    });
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);

    // 初始化
    progress.setSecondaryProgress(0); // 下载进度
    progress.setProgress(0); // 播放进度
    finishTv.setText("00:00");
    totalTv.setText("00:00");
  }
}
