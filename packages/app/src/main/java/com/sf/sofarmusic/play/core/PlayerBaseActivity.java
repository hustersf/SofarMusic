package com.sf.sofarmusic.play.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.sf.base.BaseActivity;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.main.MainActivity;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.play.presenter.PlayFloatViewPresenter;
import com.sf.utility.CollectionUtil;
import com.sf.utility.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import java.util.Random;

/**
 * Created by sufan on 17/4/9.
 * 封装底部播放栏和播放服务逻辑
 */
@BindEventBus
public class PlayerBaseActivity extends BaseActivity {

  // 悬浮的音乐界面
  protected View mFloatView;
  // 底部填充
  private LinearLayout bottomLayout;

  private PlayFloatViewPresenter presenter;
  private List<Song> songs;
  private Song curSong; // 当前选中播放的歌曲

  public MusicPlayerHelper playerHelper;
  private MusicPlayCallbackAdapter callback = new MusicPlayCallbackAdapter() {
    @Override
    public void onCompletion(MediaPlayer mp) {
      super.onCompletion(mp);
      if (baseAt instanceof MainActivity) {
        autoPlayNext();
      }
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

    // 绑定服务
    Intent intent = new Intent(this, MusicPlayService.class);
    bindService(intent, conn, Context.BIND_AUTO_CREATE);

    this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
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

    hideFloatMusicView();
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
  protected void onDestroy() {
    super.onDestroy();
    unbindService(conn);
    if (presenter != null) {
      presenter.destroy();
    }

    if (playerHelper != null) {
      playerHelper.removeMusicPlayCallback(callback);
    }
  }



  /**
   * 刷新底部播放（由子类触发，即继承自PlayerBaseActivity类的子类）
   * 页面列表状态影响播放栏列表状态
   */
  protected void updateBottom() {

  }

  /**
   * 刷新子类所在页面的歌曲列表状态（由父类触发，即本类。需要刷新的子类需要重写该方法）
   * 播放栏列表状态影响页面列表状态
   */
  protected void updateSongList() {

  }


  private void openBottomSheet() {

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
      playerHelper.pause();
    }
  }

  private void play() {
    if (curSong == null || playerHelper == null) {
      return;
    }

    // 播放的是本地歌曲
    if (!TextUtils.isEmpty(curSong.songUri)) {
      playerHelper.play(curSong.songUri);
      return;
    }

    // 播放在线歌曲
    if (curSong.songLink == null) {
      ApiProvider.getMusicApiService().getSongInfo(curSong.songId).subscribe(song -> {
        curSong.songLink = song.songLink;
        playerHelper.play(curSong.songLink.showLink);
      });
    } else {
      playerHelper.play(curSong.songLink.showLink);
    }
  }

  /**
   * 自动播放下一首
   */
  private void autoPlayNext() {
    int mode = PlayStatus.getInstance(this).getMode();

    int position = songs.indexOf(curSong);

    if (mode == PlayStatus.LIST_CYCLE) {
      if (position == songs.size() - 1) {
        position = 0;
      } else {
        position = position + 1;
      }
    } else if (mode == PlayStatus.SINGLE_CYCLE) {
      // position不变
    } else if (mode == PlayStatus.RANDOW_CYCLE) {
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
      playerHelper = ((MusicPlayService.PlayBinder) service).getMusicPlayerHelper();
      playerHelper.removeMusicPlayCallback(callback);
      playerHelper.addMusicPlayCallback(callback);
      EventBus.getDefault().post(new PlayEvent.PlayServiceConnected(playerHelper));
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      playerHelper = null;
    }
  };


}
