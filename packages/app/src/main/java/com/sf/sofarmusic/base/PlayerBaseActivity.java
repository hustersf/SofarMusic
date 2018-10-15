package com.sf.sofarmusic.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.sf.sofarmusic.PlayServiceAIDL;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.main.MainActivity;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.play.PlayListAdapter;
import com.sf.utility.LogUtil;
import com.sf.sofarmusic.view.MusicProgress;
import com.sf.base.view.MyBottomSheetDialog;

import com.sf.base.BaseActivity;

/**
 * Created by sufan on 17/4/9.
 * 有底部播放栏的Activity需要继承的父类
 */

public class PlayerBaseActivity extends BaseActivity implements PlayListAdapter.OnItemClickListener {

    //悬浮的音乐界面
    protected View mFloatView;
    public RelativeLayout music_rl;
    private RelativeLayout music_inner_rl;
    private ImageView music_iv;
    private TextView music_name_tv, music_artist_tv;
    private TextView music_more_tv, music_play_tv, music_next_tv;
    private MusicProgress music_pg;

    //底部填充
    private LinearLayout bottom_ll;

    private int mPosition;
    private int mType;
    private int mStatus;

    //播放列表
    private MyBottomSheetDialog mSheetDialog;
    private View mSheetView;
    private RecyclerView play_rv;
    private TextView list_tv, clear_tv;
    private PlayListAdapter mPlayAdapter;
    private int mHeight;

    private BroadcastReceiver receiver;

    public PlayServiceAIDL iBinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFloatView = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_bottom_music_player, null);

        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mFloatView.setVisibility(View.GONE);
        //初始化播放列表
        Constant.sPlayList = PlayList.getInstance(this).getPlayList();

        //绑定服务
        Intent intent = new Intent();
        String pkg = "com.sf.sofarmusic";
        String cls = "com.sf.sofarmusic.play.PlayService";
        intent.setComponent(new ComponentName(pkg, cls));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    private void setBroadcastReceiver() {
        //注册广播
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
                    music_pg.setProgress(progress);
                    music_pg.setSecondaryProgress(secondProgress);
                } else if (intent.getAction().equals(Constant.RefreshPosition)) {
                    Bundle bundle = intent.getExtras();
                    mPosition = bundle.getInt("position");
                    for (int i = 0; i < Constant.sPlayList.size(); i++) {
                        if (i == mPosition) {
                            Constant.sPlayList.get(mPosition).isSelected = true;
                        } else {
                            Constant.sPlayList.get(i).isSelected = false;
                        }
                    }
                    updateBottom();
                    updateSongList();
                    mPlayAdapter.refreshList(mPosition);
                } else if (intent.getAction().equals(Constant.NOTIFY_SERVICE_PLAY)) {
                    music_play_tv.setText(getResources().getString(R.string.icon_play));
                } else if (intent.getAction().equals(Constant.NOTIFY_SERVICE_PAUSE)) {
                    music_play_tv.setText(getResources().getString(R.string.icon_stop));
                }
            }
        };
        registerReceiver(receiver, intentFilter);

    }

    private void initView() {

        music_rl = (RelativeLayout) mFloatView.findViewById(R.id.music_rl);
        music_iv = (ImageView) mFloatView.findViewById(R.id.music_iv);
        music_name_tv = (TextView) mFloatView.findViewById(R.id.music_name_tv);
        music_artist_tv = (TextView) mFloatView.findViewById(R.id.music_artist_tv);
        music_pg = (MusicProgress) mFloatView.findViewById(R.id.music_pg);

        music_inner_rl = (RelativeLayout) mFloatView.findViewById(R.id.music_inner_rl);

        music_more_tv = (TextView) mFloatView.findViewById(R.id.music_more_tv);
        music_play_tv = (TextView) mFloatView.findViewById(R.id.music_play_tv);
        music_next_tv = (TextView) mFloatView.findViewById(R.id.music_next_tv);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        music_more_tv.setTypeface(iconfont);
        music_play_tv.setTypeface(iconfont);
        music_next_tv.setTypeface(iconfont);

        //在xml中无效 不知道原因
        dynamicAddView(music_more_tv, "textColor", R.color.main_text_color);
        dynamicAddView(music_play_tv, "textColor", R.color.main_text_color);
        dynamicAddView(music_next_tv, "textColor", R.color.main_text_color);
        dynamicAddView(music_name_tv, "textColor", R.color.main_text_color);
        dynamicAddView(music_rl, "background", R.color.custom_rectangle_bg);
        dynamicAddView(music_pg, "reachColor", R.color.themeColor);
    }

    private void initEvent() {
        music_inner_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent play = new Intent(baseAt, PlayActivity.class);
                startActivity(play);
            }
        });

        music_more_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });

        music_next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iBinder.playNext();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                music_play_tv.setText(getResources().getString(R.string.icon_play));
            }
        });

        music_play_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatus = PlayStatus.getInstance(baseAt).getStatus();
                if (mStatus == PlayStatus.PAUSE) {
                    music_play_tv.setText(getResources().getString(R.string.icon_play));
                    try {
                        iBinder.play();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    music_play_tv.setText(getResources().getString(R.string.icon_stop));
                    try {
                        iBinder.pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //主播放界面，不添加悬浮mFloatView，但又想要用iBinder
        if (baseAt instanceof PlayActivity) {
            return;
        }

        if (baseAt instanceof MainActivity) {
            RelativeLayout main_rl = findViewById(R.id.rl_main);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            main_rl.addView(mFloatView, lp);
        } else {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.BOTTOM;
            mContentContainer.addView(mFloatView, layoutParams);
        }

        //底部填充布局
        bottom_ll = (LinearLayout) findViewById(R.id.bottom_ll);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initPlayStatus();
        //初始化列表菜单
        initSheetDialog();
        updateBottom();
        updateSongList();
        setBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }


    private void initPlayStatus() {
        mType = PlayStatus.getInstance(this).getType();
        mPosition = PlayStatus.getInstance(this).getPosition();
        mStatus = PlayStatus.getInstance(this).getStatus();
        if (mPosition != -1 && Constant.sPlayList != null && Constant.sPlayList.size() > 0) {
            Constant.sPlayList.get(mPosition).isSelected = true;
            if (mPlayAdapter != null) {
                mPlayAdapter.refreshList(mPosition);
            }
        }
    }

    /**
     * 刷新底部播放（由子类触发，即继承自PlayerBaseActivity类的子类）
     * 页面列表状态影响播放栏列表状态
     */
    protected void updateBottom() {
        if (bottom_ll == null) {
            return;
        }

        mType = PlayStatus.getInstance(this).getType();
        mStatus = PlayStatus.getInstance(this).getStatus();
        if (Constant.sPlayList == null || Constant.sPlayList.size() == 0) {
            bottom_ll.setVisibility(View.GONE);
            mFloatView.setVisibility(View.GONE);
        } else {
            bottom_ll.setVisibility(View.VISIBLE);
            mFloatView.setVisibility(View.VISIBLE);
        }

        if (mStatus == PlayStatus.PAUSE) {
            music_play_tv.setText(getResources().getString(R.string.icon_stop));
        } else {
            music_play_tv.setText(getResources().getString(R.string.icon_play));
        }

        //初始化被选中的歌
        if (Constant.sPlayList != null) {
            for (int i = 0; i < Constant.sPlayList.size(); i++) {
                if (Constant.sPlayList.get(i).isSelected) {
                    music_name_tv.setText(Constant.sPlayList.get(i).name);
                    music_artist_tv.setText(Constant.sPlayList.get(i).artist);
                    LogUtil.d("歌名：" + Constant.sPlayList.get(i).name);
                    if (mType == PlayStatus.LOCAL) {
                        LogUtil.d("本地歌曲地址" + Constant.sPlayList.get(i).imgUri);
                        Glide.with(this).load(Constant.sPlayList.get(i).imgUri).error(R.drawable.placeholder_disk_210).into(music_iv);
                    } else {
                        LogUtil.d("在线图片地址：" + Constant.sPlayList.get(i).smallUrl);
                        Glide.with(this).load(Constant.sPlayList.get(i).smallUrl).into(music_iv);
                    }
                    mPosition = i;
                }
            }
        }

        if (Constant.sPlayList != null) {
            list_tv.setText("播放列表（" + Constant.sPlayList.size() + "）");
            mPlayAdapter.setNewData(Constant.sPlayList);
        }
    }

    /**
     * 刷新子类所在页面的歌曲列表状态（由父类触发，即本类。需要刷新的子类需要重写该方法）
     * 播放栏列表状态影响页面列表状态
     */
    protected void updateSongList() {

    }


    private void updateSheetDialog() {
        //刷新SheetDialog列表
        list_tv.setText("播放列表（" + Constant.sPlayList.size() + "）");

        if (Constant.sPlayList.size() == 0) {
            mSheetDialog.dismiss();
            mFloatView.setVisibility(View.GONE);
        }
    }

    private void saveData() {
        //存进数据库
        PlayList.getInstance(this).savePlayList(Constant.sPlayList);
        PlayStatus.getInstance(this).setPosition(mPosition);
        try {
            music_play_tv.setText(getResources().getString(R.string.icon_play));
            iBinder.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(int position) {
        updateBottom();
        updateSheetDialog();
        updateSongList();
        saveData();
    }


    private void openBottomSheet() {
        mSheetDialog.show();
    }

    private void initSheetDialog() {
        mSheetDialog = new MyBottomSheetDialog(baseAt);
        mSheetView = LayoutInflater.from(baseAt).inflate(R.layout.sheet_play_list, null);
        mSheetDialog.setContentView(mSheetView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) mSheetView.getParent());
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
                Constant.sPlayList.clear();
                updateBottom();
                mSheetDialog.dismiss();
                try {
                    iBinder.clear();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        play_rv = (RecyclerView) mSheetView.findViewById(R.id.play_list_rv);
        play_rv.setLayoutManager(new LinearLayoutManager(baseAt));
        list_tv.setText("播放列表（" + Constant.sPlayList.size() + "）");
        mPlayAdapter = new PlayListAdapter(baseAt, Constant.sPlayList);
        mPlayAdapter.setOnItemClickListener(this);

        //滚动到指定位置
        if (mPosition > 1) {
            play_rv.scrollToPosition(mPosition - 1);
        }
        play_rv.setAdapter(mPlayAdapter);
    }


    //启动activity无动画效果，使得底部的播放view无缝出现
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
            iBinder = PlayServiceAIDL.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBinder = null;
        }
    };


}
