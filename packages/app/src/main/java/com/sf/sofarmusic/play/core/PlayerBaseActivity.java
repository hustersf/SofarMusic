package com.sf.sofarmusic.play.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.sf.base.BaseActivity;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.db.song.SongRecordManager;
import com.sf.sofarmusic.main.MainActivity;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.play.presenter.PlayFloatViewPresenter;
import com.sf.utility.CollectionUtil;
import com.sf.utility.LogUtil;
import com.sf.utility.ViewUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

/**
 * Created by sufan on 17/4/9.
 * 封装底部播放栏和播放服务逻辑
 */
public class PlayerBaseActivity extends BaseActivity {

  private static final String TAG = "PlayerBaseActivity";

  // 悬浮的音乐界面
  protected View mFloatView;
  // 底部填充
  private LinearLayout bottomLayout;

  private PlayFloatViewPresenter presenter;
  private List<Song> songs;
  private Song curSong; // 当前选中播放的歌曲

  public MusicPlayer playerHelper;
  private MusicPlayCallbackAdapter callback = new MusicPlayCallbackAdapter() {
    @Override
    public void onCompletion(MediaPlayer mp) {
      super.onCompletion(mp);
      autoPlayNext();
    }
  };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFloatView = ViewUtil.inflate(getBaseContext(), R.layout.layout_bottom_music_player);
    presenter = new PlayFloatViewPresenter();
    presenter.create(mFloatView);

    initData();
  }

  private void initData() {
    // 初始化播放列表
    songs = PlayDataHolder.getInstance().getSongs();
    presenter.bind(songs, this);
    if (CollectionUtil.isEmpty(songs) && baseAt instanceof MainActivity) {
      fetchSongList();
    } else {
      initCurSong(songs);
    }
    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
  }

  private void fetchSongList() {
    SongRecordManager.getInstance().asyncFetchSongList().subscribe(songs -> {
      if (!CollectionUtil.isEmpty(songs)) {
        initCurSong(songs);
        PlayDataHolder.getInstance().setSongs(songs, false);
        presenter.bind(songs, this);
        showFloatMusicView();
      }
    });
  }

  private void initCurSong(List<Song> songs) {
    for (Song song : songs) {
      if (song.play) {
        curSong = song;
        break;
      }
    }
  }

  private void showFloatMusicView() {
    mFloatView.setVisibility(View.VISIBLE);
    if (bottomLayout != null) {
      bottomLayout.setVisibility(View.VISIBLE);
    }
  }


  private void hideFloatMusicView() {
    mFloatView.setVisibility(View.GONE);
    if (bottomLayout != null) {
      bottomLayout.setVisibility(View.GONE);
    }
  }


  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    // 主播放界面，不添加悬浮mFloatView，但又想要用iBinder
    if (baseAt instanceof PlayActivity) {
      return;
    }

    if (baseAt instanceof MainActivity) {
      RelativeLayout main_rl = findViewById(R.id.rl_main);
      RelativeLayout.LayoutParams lp =
          new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
              RelativeLayout.LayoutParams.WRAP_CONTENT);
      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      main_rl.addView(mFloatView, lp);
    } else {
      FrameLayout.LayoutParams layoutParams =
          new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
      layoutParams.gravity = Gravity.BOTTOM;
      mContentContainer.addView(mFloatView, layoutParams);
    }

    // 底部填充布局
    bottomLayout = findViewById(R.id.bottom_ll);

    if (CollectionUtil.isEmpty(songs)) {
      hideFloatMusicView();
    }
  }


  @Override
  protected void onResume() {
    super.onResume();
    if (presenter != null) {
      presenter.resume();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onStart() {
    super.onStart();
    // 绑定服务
    Intent intent = new Intent(this, MusicPlayService.class);
    bindService(intent, conn, Context.BIND_AUTO_CREATE);

    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    unbindService(conn);
    if (playerHelper != null) {
      playerHelper.removeMusicPlayCallback(callback);
    }

    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (presenter != null) {
      presenter.destroy();
    }

    if (baseAt instanceof MainActivity) {
      // 更新被选中的歌曲
      SongRecordManager.getInstance().asyncReplaceSongList(songs);
    }
  }

  /**
   * 选中某首歌曲
   */
  @Subscribe
  public void onSelectEvent(PlayEvent.SelectSongEvent event) {
    if (CollectionUtil.isEmpty(songs)) {
      return;
    }
    showFloatMusicView();
    presenter.selectSong(event.song);
    curSong = event.song;
    play();
  }

  /**
   * 播放歌曲
   */
  @Subscribe
  public void onPlayEvent(PlayEvent.PlaySongEvent event) {
    if (CollectionUtil.isEmpty(songs)) {
      return;
    }

    play();
  }

  /**
   * 暂停歌曲
   */
  @Subscribe
  public void onPauseEvent(PlayEvent.PauseSongEvent event) {
    if (CollectionUtil.isEmpty(songs)) {
      return;
    }

    if (curSong != null && playerHelper != null) {
      PlayControlHolder.getInstance().setStatus(PlayControlHolder.PlayStatus.PAUSE);
      playerHelper.pause();
    }
  }

  @Subscribe
  public void onClearSongEvent(PlayEvent.ClearSongEvent event) {
    if (CollectionUtil.isEmpty(songs)) {
      hideFloatMusicView();
    }
  }

  private void play() {
    if (curSong == null || playerHelper == null) {
      return;
    }

    PlayControlHolder.getInstance().setStatus(PlayControlHolder.PlayStatus.PLAY);
    // 播放的是本地歌曲
    if (!TextUtils.isEmpty(curSong.songUri)) {
      playerHelper.play(curSong.songUri);
      return;
    }

    // 播放在线歌曲
    if (curSong.songLink == null) {
      ApiProvider.getMusicApiService().getSongInfo(curSong.songId).subscribe(song -> {
        curSong.songLink = song.songLink;
        if (curSong.songLink != null) {
          playerHelper.play(curSong.songLink.showLink);
        }
      });
    } else {
      playerHelper.play(curSong.songLink.showLink);
    }
  }

  /**
   * 自动播放下一首
   */
  private void autoPlayNext() {
    int mode = PlayControlHolder.getInstance().getMode();

    int position = songs.indexOf(curSong);

    if (mode == PlayControlHolder.PlayMode.LIST_CYCLE) {
      if (position == songs.size() - 1) {
        position = 0;
      } else {
        position = position + 1;
      }
    } else if (mode == PlayControlHolder.PlayMode.SINGLE_CYCLE) {
      // position不变
    } else if (mode == PlayControlHolder.PlayMode.RANDOM_CYCLE) {
      int temp = position;
      do {
        position = new Random().nextInt(songs.size());
      } while (position == temp);
    }
    curSong = songs.get(position);
    EventBus.getDefault().post(new PlayEvent.SelectSongEvent(curSong));
  }


  // 启动activity无动画效果，使得底部的播放view无缝出现
  public void startActivity(Intent intent) {
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    super.startActivity(intent);
  }

  /**
   * 返回activity无动画效果
   * 手机的虚拟back键，也是调用finish，故重写finish
   */
  public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }


  private ServiceConnection conn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      LogUtil.d(TAG, "onServiceConnected");
      playerHelper = ((MusicPlayService.PlayBinder) service).getMusicPlayerHelper();
      playerHelper.removeMusicPlayCallback(callback);
      playerHelper.addMusicPlayCallback(callback);
      EventBus.getDefault().post(new PlayEvent.PlayServiceConnected(playerHelper));
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      LogUtil.d(TAG, "onServiceDisconnected");
      playerHelper = null;
    }
  };

}
