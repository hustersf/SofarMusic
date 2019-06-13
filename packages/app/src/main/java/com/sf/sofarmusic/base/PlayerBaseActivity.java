package com.sf.sofarmusic.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.base.BaseActivity;
import com.sf.base.view.SofarBottomSheetDialog;
import com.sf.sofarmusic.PlayServiceAIDL;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.main.MainActivity;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.play.PlayDataHolder;
import com.sf.sofarmusic.play.PlayEvent;
import com.sf.sofarmusic.play.PlayListAdapter;
import com.sf.sofarmusic.play.core.MusicPlayService;
import com.sf.sofarmusic.play.presenter.PlayFloatViewPresenter;
import com.sf.utility.CollectionUtil;
import com.sf.utility.ViewUtil;
import com.sf.widget.progress.MusicProgress;
import com.sf.utility.LogUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by sufan on 17/4/9.
 * 有底部播放栏的Activity需要继承的父类
 */
public class PlayerBaseActivity extends BaseActivity {

  // 悬浮的音乐界面
  protected View mFloatView;
  // 底部填充
  private LinearLayout bottomLayout;

  private PlayFloatViewPresenter presenter;
  private List<Song> songs;

  public MusicPlayService.PlayBinder playBinder;

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
   * 选中某个歌曲
   */
  @Subscribe
  public void onSelectEvent(PlayEvent.SelectSongEvent event) {
    if (CollectionUtil.isEmpty(songs)) {
      return;
    }

    showFloatMusicView();
    presenter.selectSong(event.song);
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
      playBinder = (MusicPlayService.PlayBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      playBinder = null;
    }
  };


}
