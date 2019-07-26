package com.sf.sofarmusic.play.presenter;

import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.base.BaseActivity;
import com.sf.base.mvp.Presenter;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.libskin.base.SkinBaseActivity;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.play.core.MusicPlayCallbackAdapter;
import com.sf.sofarmusic.play.core.MusicPlayer;
import com.sf.sofarmusic.play.core.PlayControlHolder;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.widget.progress.MusicProgress;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 底部播放栏
 */
@BindEventBus
public class PlayFloatViewPresenter extends Presenter<List<Song>> {

  private RelativeLayout musicLayout;
  private ImageView musicIv;
  private TextView musicNameTv, musicArtistTv;
  private TextView musicMoreTv, musicPlayTv, musicNextTv;
  private MusicProgress musicProgress;

  BaseActivity activity;
  int position;

  private Timer timer;
  private MusicPlayer playerHelper;
  private MusicPlayCallbackAdapter callback = new MusicPlayCallbackAdapter() {
    @Override
    public void onPlayStart(MediaPlayer mp) {
      super.onPlayStart(mp);
      musicPlayTv.setText(activity.getResources().getString(R.string.icon_play));
      startTimer();
    }
  };

  public PlayFloatViewPresenter() {
    add(R.id.music_more_tv, new PlayListPresenter());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopTimer();
  }

  @Override
  protected void onResume() {
    super.onResume();
    for (int i = 0; i < getModel().size(); i++) {
      if (getModel().get(i).play) {
        selectSong(getModel().get(i));
        break;
      }
    }
    if (PlayControlHolder.getInstance().isPlaying()) {
      musicPlayTv.setText(activity.getResources().getString(R.string.icon_play));
    } else {
      musicPlayTv.setText(activity.getResources().getString(R.string.icon_stop));
    }
  }

  @Override
  protected void onCreate() {
    super.onCreate();
    musicLayout = getView().findViewById(R.id.music_rl);
    musicIv = getView().findViewById(R.id.music_iv);
    musicNameTv = getView().findViewById(R.id.music_name_tv);
    musicArtistTv = getView().findViewById(R.id.music_artist_tv);
    musicProgress = getView().findViewById(R.id.music_pg);
    musicMoreTv = getView().findViewById(R.id.music_more_tv);
    musicPlayTv = getView().findViewById(R.id.music_play_tv);
    musicNextTv = getView().findViewById(R.id.music_next_tv);

  }

  @Override
  protected void onBind(List<Song> model, Object callerContext) {
    super.onBind(model, callerContext);

    if (getActivity() instanceof SkinBaseActivity) {
      activity = (BaseActivity) getActivity();
    }

    if (activity == null) {
      return;
    }

    activity.dynamicAddView(musicMoreTv, "textColor", R.color.main_text_color);
    activity.dynamicAddView(musicPlayTv, "textColor", R.color.main_text_color);
    activity.dynamicAddView(musicNextTv, "textColor", R.color.main_text_color);
    activity.dynamicAddView(musicNameTv, "textColor", R.color.main_text_color);
    activity.dynamicAddView(musicLayout, "background", R.color.custom_rectangle_bg);
    activity.dynamicAddView(musicProgress, "reachColor", R.color.themeColor);

    musicPlayTv.setOnClickListener(v -> {
      changeStatus();
    });

    musicNextTv.setOnClickListener(v -> {
      next();
    });

    musicLayout.setOnClickListener(v -> {
      PlayActivity.launch(getActivity());
    });

    for (int i = 0; i < getModel().size(); i++) {
      if (getModel().get(i).play) {
        selectSong(getModel().get(i));
        break;
      }
    }
  }

  public void selectSong(Song song) {
    if (activity == null) {
      return;
    }
    musicNameTv.setText(song.name);
    musicArtistTv.setText(song.author);
    Glide.with(activity).load(song.smallThumbUrl).into(musicIv);
  }

  private void next() {
    int mode = PlayControlHolder.getInstance().getMode();
    if (mode == PlayControlHolder.PlayMode.LIST_CYCLE) {
      if (position == getModel().size() - 1) {
        position = 0;
      } else {
        position = position + 1;
      }
    } else if (mode == PlayControlHolder.PlayMode.SINGLE_CYCLE) {
      // position不变
    } else if (mode == PlayControlHolder.PlayMode.RANDOM_CYCLE) {
      int temp = position;
      do {
        position = new Random().nextInt(getModel().size());
      } while (position == temp);
    }

    selectSong(getModel().get(position));
    EventBus.getDefault().post(new PlayEvent.SelectSongEvent(getModel().get(position)));
  }

  private void changeStatus() {
    if (PlayControlHolder.getInstance().isPlaying()) {
      musicPlayTv.setText(activity.getResources().getString(R.string.icon_stop));
      // 暂停播放
      EventBus.getDefault().post(new PlayEvent.PauseSongEvent());
    } else {
      musicPlayTv.setText(activity.getResources().getString(R.string.icon_play));
      // 播放音乐
      EventBus.getDefault().post(new PlayEvent.PlaySongEvent());
    }
  }

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    Song song = event.song;
    if (song == null) {
      return;
    }
    for (int i = 0; i < getModel().size(); i++) {
      if (getModel().get(i).songId.equals(event.song.songId)) {
        position = i;
        break;
      }
    }
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
            updateProgress();
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

  private void updateProgress() {
    long curPosition = playerHelper.getCurrentPosition();
    long totalDuration = playerHelper.getTotalDuration();

    if (totalDuration == 0) {
      return;
    }

    int progress = (int) (1.0f * curPosition / totalDuration * 100);
    musicProgress.setProgress(progress);
  }
}
