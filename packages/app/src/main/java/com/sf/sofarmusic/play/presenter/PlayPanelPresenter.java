package com.sf.sofarmusic.play.presenter;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.base.mvp.Presenter;
import com.sf.libskin.base.SkinBaseActivity;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.LrcItem;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayEvent;
import com.sf.sofarmusic.play.cache.LrcCacheUtil;
import com.sf.sofarmusic.util.LrcUtil;
import com.sf.sofarmusic.view.LrcView;
import com.sf.utility.LogUtil;
import com.sf.utility.ToastUtil;
import com.sf.widget.bitmap.round.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlayPanelPresenter extends Presenter<List<Song>> {

  private static final String TAG = "PlayPanelPresenter";

  RelativeLayout centerRl;
  RoundImageView headIv;
  TextView lrcTv;

  LrcView lrcView;
  List<LrcItem> lrcList;

  int position;
  ObjectAnimator headAnim;

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopHeadAnim();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    centerRl = getView().findViewById(R.id.center_rl);
    headIv = getView().findViewById(R.id.head_iv);
    lrcTv = getView().findViewById(R.id.lrc_tv);
    lrcView = getView().findViewById(R.id.lrc);

    centerRl.setOnClickListener(v -> {
      centerRl.setVisibility(View.GONE);
      lrcView.setVisibility(View.VISIBLE);
    });

    lrcView.setOnClickListener(v -> {
      centerRl.setVisibility(View.VISIBLE);
      lrcView.setVisibility(View.GONE);
    });
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);
    if (getActivity() == null) {
      return;
    }

    ((SkinBaseActivity) getActivity()).dynamicAddView(lrcView, "currentColor", R.color.themeColor);

    position = PlayStatus.getInstance(getActivity()).getPosition();
    if (position == -1) {
      position = 0;
    }

    changeHeadAndLrc(position);
    startHeadAnim();
  }

  /**
   * 更新头像和歌词
   */
  private void changeHeadAndLrc(int position) {
    Song item = getModel().get(position);
    Glide.with(getActivity()).load(item.bigThumbUrl).into(headIv);
    getLrc(item);
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

  /**
   * 更新歌词的位置
   */
  private void updateLrc() {
    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    int current = 0; // 获取歌曲当前位置

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

  @Subscribe
  public void onChangeSongEvent(PlayEvent.ChangeSongEvent event) {
    changeHeadAndLrc(event.position);
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
