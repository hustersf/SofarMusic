package com.sf.sofarmusic.play.presenter;

import android.app.Activity;
import android.widget.TextView;

import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayEvent;
import com.sf.utility.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

public class PlayControlPresenter extends Presenter<List<Song>> {

  TextView playTv;
  TextView preTv;
  TextView nextTv;
  TextView typeTv;

  Activity activity;

  int mode;
  int status;
  int position;

  public PlayControlPresenter() {
    add(R.id.more_tv, new PlayListPresenter());
    add(new PlayProgressPresenter());
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    playTv = getView().findViewById(R.id.play_tv);
    preTv = getView().findViewById(R.id.pre_tv);
    nextTv = getView().findViewById(R.id.next_tv);
    typeTv = getView().findViewById(R.id.type_tv);

    typeTv.setOnClickListener(v -> {
      changeMode();
    });

    playTv.setOnClickListener(v -> {
      changeStatus();
    });

    preTv.setOnClickListener(v -> {
      pre();
    });

    nextTv.setOnClickListener(v -> {
      next();
    });
  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);
    if (!(callerContext instanceof Activity)) {
      return;
    }

    activity = (Activity) callerContext;

    // 播放模式
    mode = PlayStatus.getInstance(activity).getMode();
    if (mode == PlayStatus.LIST_CYCLE) {
      typeTv.setText(activity.getResources().getString(R.string.icon_list_cycle));
    } else if (mode == PlayStatus.SINGLE_CYCLE) {
      typeTv.setText(activity.getResources().getString(R.string.icon_single_cycle));
    } else if (mode == PlayStatus.RANDOW_CYCLE) {
      typeTv.setText(activity.getResources().getString(R.string.icon_random_cycle));
    }

    // 播放暂停状态
    status = PlayStatus.getInstance(activity).getStatus();
    if (status == PlayStatus.PAUSE) {
      playTv.setText(activity.getResources().getString(R.string.icon_stop));
    } else {
      playTv.setText(activity.getResources().getString(R.string.icon_play));
    }

  }

  /**
   * 改变模式
   */
  private void changeMode() {
    if (mode == PlayStatus.LIST_CYCLE) {
      typeTv.setText(activity.getResources().getString(R.string.icon_single_cycle));
      mode = PlayStatus.SINGLE_CYCLE;
      PlayStatus.getInstance(activity).setMode(mode);
      ToastUtil.startShort(activity, "单曲循环");
    } else if (mode == PlayStatus.SINGLE_CYCLE) {
      typeTv.setText(activity.getResources().getString(R.string.icon_random_cycle));
      mode = PlayStatus.RANDOW_CYCLE;
      PlayStatus.getInstance(activity).setMode(mode);
      ToastUtil.startShort(activity, "随机循环");
    } else if (mode == PlayStatus.RANDOW_CYCLE) {
      typeTv.setText(activity.getResources().getString(R.string.icon_list_cycle));
      mode = PlayStatus.LIST_CYCLE;
      PlayStatus.getInstance(activity).setMode(mode);
      ToastUtil.startShort(activity, "顺序循环");
    }
  }

  /**
   * 改变播放状态
   */
  private void changeStatus() {
    if (status == PlayStatus.PAUSE) {
      status = PlayStatus.PLAY;
      playTv.setText(activity.getResources().getString(R.string.icon_play));
      // 播放音乐
      EventBus.getDefault().post(new PlayEvent.PlaySongEvent());
    } else {
      status = PlayStatus.PAUSE;
      playTv.setText(activity.getResources().getString(R.string.icon_stop));
      // 暂停播放
      EventBus.getDefault().post(new PlayEvent.PauseSongEvent());
    }
    PlayStatus.getInstance(activity).setStatus(status);
  }

  /**
   * 上一首
   */
  private void pre() {
    status = PlayStatus.PLAY;
    playTv.setText(activity.getResources().getString(R.string.icon_play));
    PlayStatus.getInstance(activity).setStatus(status);

    if (mode == PlayStatus.LIST_CYCLE) {
      if (position == 0) {
        position = getModel().size() - 1;
      } else {
        position = position - 1;
      }
    } else if (mode == PlayStatus.SINGLE_CYCLE) {
      // position不变
    } else if (mode == PlayStatus.RANDOW_CYCLE) {
      int temp = position;
      do {
        position = new Random().nextInt(getModel().size());
      } while (position == temp);
    }

    EventBus.getDefault().post(new PlayEvent.ChangeSongEvent(position));
    EventBus.getDefault().post(new PlayEvent.PlaySongEvent());
  }

  /**
   * 下一首
   */
  private void next() {
    status = PlayStatus.PLAY;
    playTv.setText(activity.getResources().getString(R.string.icon_play));
    PlayStatus.getInstance(activity).setStatus(status);

    if (mode == PlayStatus.LIST_CYCLE) {
      if (position == getModel().size() - 1) {
        position = 0;
      } else {
        position = position + 1;
      }
    } else if (mode == PlayStatus.SINGLE_CYCLE) {
      // position不变
    } else if (mode == PlayStatus.RANDOW_CYCLE) {
      int temp = position;
      do {
        position = new Random().nextInt(getModel().size());
      } while (position == temp);
    }

    EventBus.getDefault().post(new PlayEvent.ChangeSongEvent(position));
    EventBus.getDefault().post(new PlayEvent.PlaySongEvent());
  }
}
