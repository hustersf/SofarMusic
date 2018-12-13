package com.sf.sofarmusic.play;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sf.base.view.MyBottomSheetDialog;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.PlayerBaseActivity;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.LrcItem;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.play.cache.LrcCacheUtil;
import com.sf.sofarmusic.util.LrcUtil;
import com.sf.sofarmusic.view.CircleImageView;
import com.sf.widget.progress.DoubleMusicProgress;
import com.sf.sofarmusic.view.LrcView;
import com.sf.utility.LogUtil;
import com.sf.utility.ToastUtil;


/**
 * Created by sufan on 16/11/21.
 */

public class PlayActivity extends PlayerBaseActivity
    implements
      View.OnClickListener,
      PlayListAdapter.OnItemClickListener {

  private ImageView play_bg_iv;
  private Typeface mIconfont;

  // 头部
  private TextView head_back, head_share;
  private TextView music_name_tv, music_sing_tv;

  // 中间
  private CircleImageView head_iv;
  private TextView lrc_tv;
  private ImageView needle_iv;

  // 底部
  private TextView play_tv, left_tv, right_tv, type_tv, more_tv;
  private TextView like_tv, download_tv, comment_tv, dot_tv;
  private TextView finish_tv, total_tv;
  private DoubleMusicProgress dmp;

  // 歌词
  private List<LrcItem> mLrcList;
  private RelativeLayout center_rl;
  private LrcView lrcView;


  private Bitmap mSourceBt;
  private Bitmap mBlurBt;
  private ObjectAnimator mOa;

  private int mType; // 本地还是在线歌曲
  private int mMode; // 播放模式
  private int mPosition;
  private int mStatus;

  // 播放列表
  private MyBottomSheetDialog mSheetDialog;
  private View mSheetView;
  private RecyclerView play_rv;
  private TextView list_tv, clear_tv;
  private PlayListAdapter mPlayAdapter;
  private int mHeight;
  private BroadcastReceiver receiver;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play);
    mIconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
    initHead();
    initFoot();
    initView();
    initData();
    initEvent();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    recycleBitmap();
    unregisterReceiver(receiver);
  }


  private void initData() {
    mType = PlayStatus.getInstance(this).getType();
    mMode = PlayStatus.getInstance(this).getMode();
    mPosition = PlayStatus.getInstance(this).getPosition();
    mStatus = PlayStatus.getInstance(this).getStatus();

    // 初始化动画
    headAnim();
    needle_iv.setPivotX(0);
    needle_iv.setPivotY(0);
    if (mStatus == PlayStatus.PAUSE) {
      needleAnimOut();
      mOa.pause();
    } else {
      mOa.resume();
    }


    // 初始化背景
    mSourceBt = BitmapFactory.decodeResource(getResources(), R.drawable.play_default_bg);
    mBlurBt = RenderBlur.gaussianBlur(this, mSourceBt, 25);
    play_bg_iv.setImageBitmap(mBlurBt);
    changeImgAndLrc();


    // 初始化页面元素
    music_name_tv.setText(Constant.sPlayList.get(mPosition).name);
    music_sing_tv.setText(Constant.sPlayList.get(mPosition).artist);

    dmp.setSecondaryProgress(0); // 下载进度
    dmp.setProgress(0); // 播放进度

    finish_tv.setText("00:00");
    total_tv.setText("00:00");

    if (mMode == PlayStatus.List_Cycle) {
      type_tv.setText(getResources().getString(R.string.icon_list_cycle));
    } else if (mMode == PlayStatus.Single_Cycle) {
      type_tv.setText(getResources().getString(R.string.icon_single_cycle));
    } else if (mMode == PlayStatus.Random_Cycle) {
      type_tv.setText(getResources().getString(R.string.icon_random_cycle));
    }

    if (mStatus == PlayStatus.PAUSE) {
      play_tv.setText(getResources().getString(R.string.icon_stop));
    } else {
      play_tv.setText(getResources().getString(R.string.icon_play));
    }


    // 初始化列表菜单
    initSheetDialog();


    // 注册广播
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Constant.RefreshProgress);
    intentFilter.addAction(Constant.RefreshPosition);
    intentFilter.addAction(Constant.NOTIFY_SERVICE_PAUSE);
    intentFilter.addAction(Constant.NOTIFY_SERVICE_PLAY);
    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constant.RefreshProgress)) {
          Bundle bundle = intent.getExtras();
          int progress = bundle.getInt("progress");
          int secondProgress = bundle.getInt("secondProgress");
          dmp.setProgress(progress); // 播放进度
          dmp.setSecondaryProgress(secondProgress); // 下载进度

          SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
          int current = bundle.getInt("current");
          int total = bundle.getInt("total");
          finish_tv.setText(sdf.format(new Date(current)));
          total_tv.setText(sdf.format(new Date(total)));

          lrcView.updateTime(current);
          // 显示歌词
          if (mLrcList != null) {
            for (int i = 0; i < mLrcList.size(); i++) {
              LrcItem lrcItem = mLrcList.get(i);
              int time = lrcItem.getTime();

              String timeStr = sdf.format(new Date(time));
              if (timeStr.equals(sdf.format(new Date(current)))) {
                lrc_tv.setText(lrcItem.getContent());
              }
            }
          }
        } else if (intent.getAction().equals(Constant.RefreshPosition)) {
          Bundle bundle = intent.getExtras();
          mPosition = bundle.getInt("position");
          music_name_tv.setText(Constant.sPlayList.get(mPosition).name);
          music_sing_tv.setText(Constant.sPlayList.get(mPosition).artist);
          mPlayAdapter.refreshList(mPosition);
          changeImgAndLrc();
        } else if (intent.getAction().equals(Constant.NOTIFY_SERVICE_PLAY)) {
          play_tv.setText(getResources().getString(R.string.icon_play));

        } else if (intent.getAction().equals(Constant.NOTIFY_SERVICE_PAUSE)) {
          play_tv.setText(getResources().getString(R.string.icon_stop));

        }
      }
    };
    registerReceiver(receiver, intentFilter);

  }

  private void recycleBitmap() {
    if (mSourceBt != null)
      mSourceBt.recycle();
    mSourceBt = null;

    if (mBlurBt != null)
      mBlurBt.recycle();
    mBlurBt = null;
  }

  private void initView() {
    play_bg_iv = (ImageView) findViewById(R.id.play_bg_iv);
    head_iv = (CircleImageView) findViewById(R.id.head_iv);
    lrc_tv = (TextView) findViewById(R.id.lrc_tv);
    needle_iv = (ImageView) findViewById(R.id.needle_iv);

    center_rl = (RelativeLayout) findViewById(R.id.center_rl);
    lrcView = (LrcView) findViewById(R.id.lrc);
    dynamicAddView(lrcView, "currentColor", R.color.themeColor);
  }

  private void initEvent() {
    play_tv.setOnClickListener(this);
    type_tv.setOnClickListener(this);
    left_tv.setOnClickListener(this);
    right_tv.setOnClickListener(this);
    more_tv.setOnClickListener(this);
    download_tv.setOnClickListener(this);

    center_rl.setOnClickListener(this);
    lrcView.setOnClickListener(this);

    dmp.setOnProgressListener(new DoubleMusicProgress.OnProgressListener() {
      @Override
      public void onProgress(int progress) {
        try {
          iBinder.seekTo(progress);
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
    });

  }

  private void initHead() {
    head_back = (TextView) findViewById(R.id.head_back);
    head_share = (TextView) findViewById(R.id.head_share);
    music_name_tv = (TextView) findViewById(R.id.music_name_tv);
    music_sing_tv = (TextView) findViewById(R.id.music_sing_tv);

    head_back.setTypeface(mIconfont);
    head_share.setTypeface(mIconfont);

    head_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

  }

  private void initFoot() {
    play_tv = (TextView) findViewById(R.id.play_tv);
    left_tv = (TextView) findViewById(R.id.left_tv);
    right_tv = (TextView) findViewById(R.id.right_tv);
    type_tv = (TextView) findViewById(R.id.type_tv);
    more_tv = (TextView) findViewById(R.id.more_tv);

    like_tv = (TextView) findViewById(R.id.like_tv);
    download_tv = (TextView) findViewById(R.id.download_tv);
    comment_tv = (TextView) findViewById(R.id.comment_tv);
    dot_tv = (TextView) findViewById(R.id.dot_tv);

    play_tv.setTypeface(mIconfont);
    left_tv.setTypeface(mIconfont);
    right_tv.setTypeface(mIconfont);
    type_tv.setTypeface(mIconfont);
    more_tv.setTypeface(mIconfont);

    like_tv.setTypeface(mIconfont);
    download_tv.setTypeface(mIconfont);
    comment_tv.setTypeface(mIconfont);
    dot_tv.setTypeface(mIconfont);

    // 计时器,音乐播放相关
    finish_tv = (TextView) findViewById(R.id.finish_tv);
    total_tv = (TextView) findViewById(R.id.total_tv);
    dmp = (DoubleMusicProgress) findViewById(R.id.dmp);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.play_tv:
        changeStatus();
        break;
      case R.id.type_tv:
        changeMode();
        break;
      case R.id.left_tv:
        pre();
        break;
      case R.id.right_tv:
        next();
        break;
      case R.id.more_tv:
        openBottomSheet();
        break;
      case R.id.center_rl:
        center_rl.setVisibility(View.GONE);
        lrcView.setVisibility(View.VISIBLE);
        break;
      case R.id.lrc:
        center_rl.setVisibility(View.VISIBLE);
        lrcView.setVisibility(View.GONE);
        break;
      case R.id.download_tv:

        break;
    }
  }


  private void headAnim() {
    mOa = ObjectAnimator.ofFloat(head_iv, "rotation", 0F, 360F);
    mOa.setDuration(10000);
    mOa.setRepeatMode(ObjectAnimator.RESTART);
    mOa.setRepeatCount(-1);
    mOa.setInterpolator(new LinearInterpolator());
    mOa.start();
  }

  private void needleAnimOut() {
    ObjectAnimator oa1 = ObjectAnimator.ofFloat(needle_iv, "rotation", 0F, -20F);
    oa1.setDuration(200);
    oa1.start();
  }

  private void needleAnimIn() {
    ObjectAnimator oa2 = ObjectAnimator.ofFloat(needle_iv, "rotation", -20F, 0F);
    oa2.setDuration(500);
    oa2.start();
  }

  // 改变背景图片和转盘图片
  private void changeImgAndLrc() {
    PlayItem item = Constant.sPlayList.get(mPosition);
    if (mType == PlayStatus.LOCAL) {
      Glide.with(baseAt).load(item.imgUri).into(head_iv);
    } else {
      Glide.with(baseAt).load(item.bigUrl).into(head_iv);
    }

    getLrc(item);
  }


  // 改变播放状态
  private void changeStatus() {
    mStatus = PlayStatus.getInstance(this).getStatus();
    if (mStatus == PlayStatus.PAUSE) {
      play_tv.setText(getResources().getString(R.string.icon_play));
      try {
        iBinder.play();
      } catch (RemoteException e) {
        e.printStackTrace();
      }

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        mOa.resume();
      }
      needleAnimIn();
    } else {
      play_tv.setText(getResources().getString(R.string.icon_stop));
      try {
        iBinder.pause();
      } catch (RemoteException e) {
        e.printStackTrace();
      }

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        mOa.pause();
      }
      needleAnimOut();
    }
  }

  // 改变模式
  private void changeMode() {
    if (mMode == PlayStatus.List_Cycle) {
      type_tv.setText(getResources().getString(R.string.icon_single_cycle));
      mMode = PlayStatus.Single_Cycle;
      PlayStatus.getInstance(this).setMode(mMode);
      ToastUtil.startShort(baseAt, "单曲循环");

    } else if (mMode == PlayStatus.Single_Cycle) {
      type_tv.setText(getResources().getString(R.string.icon_random_cycle));
      mMode = PlayStatus.Random_Cycle;
      PlayStatus.getInstance(this).setMode(mMode);
      ToastUtil.startShort(baseAt, "随机循环");

    } else if (mMode == PlayStatus.Random_Cycle) {
      type_tv.setText(getResources().getString(R.string.icon_list_cycle));
      mMode = PlayStatus.List_Cycle;
      PlayStatus.getInstance(this).setMode(mMode);
      ToastUtil.startShort(baseAt, "顺序循环");
    }
  }


  // 上一曲
  private void pre() {
    try {
      play_tv.setText(getResources().getString(R.string.icon_play));
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        mOa.resume();
      }
      needleAnimIn();
      iBinder.playPre();
    } catch (RemoteException e) {
      e.printStackTrace();
    }

  }


  // 下一曲
  private void next() {
    try {
      play_tv.setText(getResources().getString(R.string.icon_play));
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        mOa.resume();
      }
      needleAnimIn();
      iBinder.playNext();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }


  private void openBottomSheet() {
    mSheetDialog.show();
  }

  private void initSheetDialog() {
    mSheetDialog = new MyBottomSheetDialog(baseAt);
    mSheetView = LayoutInflater.from(baseAt).inflate(R.layout.sheet_play_list, null);
    mSheetDialog.setContentView(mSheetView);
    final BottomSheetBehavior bottomSheetBehavior =
        BottomSheetBehavior.from((View) mSheetView.getParent());
    mSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialog) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      }
    });


    list_tv = (TextView) mSheetView.findViewById(R.id.list_tv);
    clear_tv = (TextView) mSheetView.findViewById(R.id.clear_tv);
    clear_tv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Constant.sPlayList = null;
        try {
          iBinder.pause();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
        mSheetDialog.dismiss();
        finish();
      }
    });
    play_rv = (RecyclerView) mSheetView.findViewById(R.id.play_list_rv);
    play_rv.setLayoutManager(new LinearLayoutManager(baseAt));
    list_tv.setText("播放列表（" + Constant.sPlayList.size() + "）");
    mPlayAdapter = new PlayListAdapter(baseAt, Constant.sPlayList);
    mPlayAdapter.setOnItemClickListener(this);

    // 滚动到指定位置
    if (mPosition > 1) {
      play_rv.scrollToPosition(mPosition - 1);
    }
    play_rv.setAdapter(mPlayAdapter);
  }


  @Override
  public void onItemClick(int position) {
    // 刷新SheetDialog列表
    for (int i = 0; i < Constant.sPlayList.size(); i++) {
      if (Constant.sPlayList.get(i).isSelected) {
        mPosition = i;
      }
    }
    list_tv.setText("播放列表（" + Constant.sPlayList.size() + "）");

    if (Constant.sPlayList.size() == 0) {
      mSheetDialog.dismiss();
    }

    // 刷新当前页面
    music_name_tv.setText(Constant.sPlayList.get(mPosition).name);
    music_sing_tv.setText(Constant.sPlayList.get(mPosition).artist);
    changeImgAndLrc();

    // 存进数据库
    PlayList.getInstance(this).savePlayList(Constant.sPlayList);
    PlayStatus.getInstance(this).setPosition(mPosition);
    try {
      play_tv.setText(getResources().getString(R.string.icon_play));
      iBinder.play();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }


  private void getLrc(PlayItem item) {
    lrcView.clearLrc();
    lrc_tv.setText("");
    LrcCacheUtil util = null;
    if (mType == PlayStatus.LOCAL) {
      util = new LrcCacheUtil(this, item, true);
    } else {
      util = new LrcCacheUtil(this, item, false);
    }

    util.getLrc(new LrcCacheUtil.LrcCallback() {
      @Override
      public void onSuccess(String lrc) {
        LogUtil.d("TAG", "lrc:" + lrc);
        lrcView.loadLrc(lrc);
        mLrcList = LrcUtil.getLrcList(lrc);
      }

      @Override
      public void onError(String error) {
        ToastUtil.startShort(baseAt, error);
      }
    });
  }
}
