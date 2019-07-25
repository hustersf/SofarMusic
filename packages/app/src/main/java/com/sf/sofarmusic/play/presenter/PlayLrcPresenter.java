package com.sf.sofarmusic.play.presenter;

import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.LrcItem;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.core.MusicPlayer;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.sofarmusic.play.lrc.LrcCacheUtil;
import com.sf.sofarmusic.play.lrc.LrcUtil;
import com.sf.sofarmusic.play.lrc.LrcView;
import com.sf.utility.LogUtil;
import com.sf.utility.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 歌词逻辑
 */
@BindEventBus
public class PlayLrcPresenter extends Presenter<List<Song>> {

  private static final String TAG = "PlayLrcPresenter";

  TextView lrcTv;
  LrcView lrcView;
  List<LrcItem> lrcList;

  private Timer timer;
  Song curSong; // 当前正在播放的歌曲
  private MusicPlayer playerHelper;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopTimer();
  }


  @Subscribe
  public void onPlayServiceConnectedEvent(PlayEvent.PlayServiceConnected event) {
    if (event.playerHelper == null) {
      return;
    }

    playerHelper = event.playerHelper;
    startTimer();
  }

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    if (event.song == null) {
      return;
    }
    curSong = event.song;
    getLrc(event.song);
  }

  @Subscribe
  public void onPlayProgressDragEvent(PlayEvent.PlayProgressDragEvent event) {
    int current = playerHelper.getCurrentPosition(); // 获取歌曲当前位置
    lrcView.onDrag(current);
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    lrcTv = getView().findViewById(R.id.lrc_tv);
    lrcView = getView().findViewById(R.id.lrc);
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);

    for (Song song : model) {
      if (song.play) {
        curSong = song;
        break;
      }
    }
    getLrc(curSong);
  }

  private void getLrc(Song item) {
    lrcView.clearLrc();
    lrcTv.setText("");
    LrcCacheUtil util = new LrcCacheUtil(getActivity(), item, false);
    util.getLrc(new LrcCacheUtil.LrcCallback() {
      @Override
      public void onSuccess(String lrc) {
        LogUtil.d(TAG, "lrc:" + lrc);
        lrcView.loadLrc(lrc);
        lrcList = LrcUtil.getLrcList(lrc);
      }

      @Override
      public void onError(String error) {
        ToastUtil.startShort(getActivity(), error);
      }
    });
  }

  private void startTimer() {
    stopTimer();
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        getActivity().runOnUiThread(() -> {
          updateLrc();
        });
      }
    }, 1000, 1000);
  }

  private void stopTimer() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }

  /**
   * 更新歌词的位置
   */
  private void updateLrc() {
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    int current = playerHelper.getCurrentPosition(); // 获取歌曲当前位置
    lrcView.updateTime(current);
    // 显示歌词
    if (lrcList != null) {
      for (int i = 0; i < lrcList.size(); i++) {
        LrcItem lrcItem = lrcList.get(i);
        int time = lrcItem.getTime();
        String timeStr = sdf.format(new Date(time));
        if (timeStr.equals(sdf.format(new Date(current)))) {
          lrcTv.setText(lrcItem.getContent());
        }
      }
    }
  }

}
