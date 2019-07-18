package com.sf.sofarmusic.play.presenter;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.sf.base.mvp.Presenter;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.libskin.base.SkinBaseActivity;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.sofarmusic.play.lrc.LrcView;
import com.sf.widget.bitmap.round.RoundImageView;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;

@BindEventBus
public class PlayPanelPresenter extends Presenter<List<Song>> {

  private static final String TAG = "PlayPanelPresenter";

  RelativeLayout centerRl;
  RoundImageView headIv;
  LrcView lrcView;

  ObjectAnimator headAnim;
  Song curSong; // 当前正在播放的歌曲

  public PlayPanelPresenter() {
    add(new PlayLrcPresenter());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopHeadAnim();
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    centerRl = getView().findViewById(R.id.center_rl);
    headIv = getView().findViewById(R.id.head_iv);
    lrcView = getView().findViewById(R.id.lrc);

    centerRl.setOnClickListener(v -> {
      centerRl.setVisibility(View.GONE);
      lrcView.setVisibility(View.VISIBLE);
    });

    lrcView.setOnClickListener(v -> {
      centerRl.setVisibility(View.VISIBLE);
      lrcView.setVisibility(View.GONE);
    });
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);
    if (getActivity() == null) {
      return;
    }

    ((SkinBaseActivity) getActivity()).dynamicAddView(lrcView, "currentColor", R.color.themeColor);

    for (Song song : model) {
      if (song.play) {
        curSong = song;
        break;
      }
    }
    changeHeadAndLrc(curSong);
    startHeadAnim();
  }

  /**
   * 更新头像和歌词
   */
  private void changeHeadAndLrc(Song item) {
    Glide.with(getActivity()).load(item.bigThumbUrl).into(headIv);
  }

  private void startHeadAnim() {
    headAnim = ObjectAnimator.ofFloat(headIv, "rotation", 0F, 360F);
    headAnim.setDuration(10000);
    headAnim.setRepeatMode(ObjectAnimator.RESTART);
    headAnim.setRepeatCount(ObjectAnimator.INFINITE);
    headAnim.setInterpolator(new LinearInterpolator());
    headAnim.start();
  }

  private void stopHeadAnim() {
    if (headAnim != null) {
      headAnim.cancel();
    }
  }

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    if (event.song == null) {
      return;
    }
    curSong = event.song;
    changeHeadAndLrc(event.song);
  }

  @Subscribe
  public void onPlaySongEvent(PlayEvent.PlaySongEvent event) {
    if (headAnim != null) {
      headAnim.resume();
    }
  }

  @Subscribe
  public void onPauseSongEvent(PlayEvent.PauseSongEvent event) {
    if (headAnim != null) {
      headAnim.pause();
    }
  }
}
