package com.sf.sofarmusic.play.presenter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.base.mvp.Presenter;
import com.sf.libskin.base.SkinBaseActivity;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.widget.progress.MusicProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Random;

/**
 * 底部播放栏
 */
public class PlayFloatViewPresenter extends Presenter<List<Song>> {

  private RelativeLayout musicLayout;
  private ImageView musicIv;
  private TextView musicNameTv, musicArtistTv;
  private TextView musicMoreTv, musicPlayTv, musicNextTv;
  private MusicProgress musicProgress;

  SkinBaseActivity activity;
  int position;

  public PlayFloatViewPresenter() {
    add(R.id.music_more_tv, new PlayListPresenter());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onCreate() {
    super.onCreate();

    EventBus.getDefault().register(this);
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
      activity = (SkinBaseActivity) getActivity();
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

    musicNextTv.setOnClickListener(v -> {
      next();
    });

    musicLayout.setOnClickListener(v -> {
      PlayActivity.launch(getActivity());
    });
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
    int mode = PlayStatus.getInstance(activity).getMode();
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

    selectSong(getModel().get(position));
    EventBus.getDefault().post(new PlayEvent.SelectSongEvent(getModel().get(position)));
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
}
