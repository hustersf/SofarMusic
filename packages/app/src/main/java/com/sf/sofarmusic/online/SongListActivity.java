package com.sf.sofarmusic.online;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.PlayerBaseActivity;
import com.sf.sofarmusic.data.LocalData;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.MenuItem;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.util.PopUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
/**
 * Created by sufan on 17/4/9.
 */

public class SongListActivity extends PlayerBaseActivity
    implements
      SongListAdapter.OnItemClickListener {

  private Toolbar mToolbar;
  private TextView mBackTv, mTitleTv, mSerachTv, mMenuTv;

  private TextView mErrorView;
  private SwipeRefreshLayout mRefreshLayout;
  private RecyclerView mSongRecyclerView;
  private SongListAdapter mSongAdapter;
  private List<PlayItem> mPlayList;
  private List<MenuItem> mMenuList;

  private String mImgUrl;
  private int mType;
  private int mCount;
  private int mHeadType;
  private int mColor = 0x00000000;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_song_list);

    initView();
    initData();
    initEvent();
  }

  private void initData() {
    Bundle bundle = getIntent().getExtras();
    mHeadType = 1;
    mType = bundle.getInt("type");
    mCount = bundle.getInt("count");
    if (mCount == 0) {
      mCount = 20;
    }
    mImgUrl = bundle.getString("imgUrl");
    mTitleTv.setText(bundle.getString("name"));
    mMenuList = LocalData.getRankListMenuData();
    getSongList();
  }

  private void getSongList() {
    mPlayList = new ArrayList<>();
    getAllSong();
  }

  private void initNetData() {
    mSongAdapter = new SongListAdapter(baseAt, mPlayList, mImgUrl, mHeadType);
    mSongRecyclerView.setAdapter(mSongAdapter);

    mSongAdapter.setOnItemClickListener(this);
    mSongAdapter.setOnColorCallback(new SongListAdapter.ColorCallback() {
      @Override
      public void OnColor(int color) {
        mColor = color;
      }
    });
  }


  private void initView() {
    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    mBackTv = (TextView) findViewById(R.id.back_tv);
    mTitleTv = (TextView) findViewById(R.id.title_tv);
    mSerachTv = (TextView) findViewById(R.id.search_tv);
    mMenuTv = (TextView) findViewById(R.id.menu_tv);
    Typeface iconfont = FontUtil.setFont(baseAt);
    mBackTv.setTypeface(iconfont);
    mSerachTv.setTypeface(iconfont);
    mMenuTv.setTypeface(iconfont);

    mSongRecyclerView = (RecyclerView) findViewById(R.id.song_rv);
    mSongRecyclerView.setLayoutManager(new LinearLayoutManager(baseAt));
    mRefreshLayout = findViewById(R.id.refresh_song);

    mErrorView = (TextView) findViewById(R.id.tv_error);
    dynamicAddView(mErrorView, "textColor", R.color.main_text_color);

  }

  private void initEvent() {
    mErrorView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mErrorView.setVisibility(View.GONE);
        getSongList();
      }
    });

    mBackTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          finishAfterTransition();
        } else {
          finish();
        }
      }
    });

    mMenuTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PopUtil.showMenuPop(baseAt, mMenuList);
      }
    });

    mSongRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstH = recyclerView.getChildAt(0).getHeight();
        int titleH = mToolbar.getHeight();
        int totalH = firstH - titleH;

        int y = getScollYDistance();
        if (y >= totalH) {
          y = totalH;
        }

        int alpha = (int) ((y * 1.0f) / totalH * 255);
        // 改变标题栏
        if (alpha > 125) {
          mTitleTv.setVisibility(View.VISIBLE);
        } else {
          mTitleTv.setVisibility(View.GONE);
        }
        mToolbar.setBackgroundColor(mColor);
        mToolbar.getBackground().setAlpha(alpha);
      }
    });

    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        testRefresh();
      }
    });

  }

  private void testRefresh() {
    mRefreshLayout.postDelayed(new Runnable() {
      @Override
      public void run() {
        mRefreshLayout.setRefreshing(false);
      }
    }, 2000);
  }


  private int getScollYDistance() {
    LinearLayoutManager layoutManager = (LinearLayoutManager) mSongRecyclerView.getLayoutManager();
    int postion = layoutManager.findFirstVisibleItemPosition();
    View firstVisibleChildView = layoutManager.findViewByPosition(postion);
    int itemHeight = firstVisibleChildView.getHeight();
    return (postion) * itemHeight - firstVisibleChildView.getTop();
  }


  private void getAllSong() {
    baseAt.show();
    ApiProvider.getMusicApiService().getRankSongs(mType, mCount, 0)
        .compose(this.bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(rankSongsResponse -> {
          baseAt.dismiss();
          if (rankSongsResponse.mSongList.isEmpty()) {
            return;
          }
          for (int i = 0; i < rankSongsResponse.mSongList.size(); i++) {
            PlayItem pItem = new PlayItem();
            Song song = rankSongsResponse.mSongList.get(i);
            pItem.songId = song.mId;
            pItem.name = song.mName;
            pItem.artist = song.mAuthor;
            pItem.smallUrl = song.mCoverUrl;
            pItem.bigUrl = song.mBigCoverUrl;
            pItem.lrcLinkUrl = song.mLrcLink;
            if (i < 3) {
              pItem.isImport = (true);
            }
            mPlayList.add(pItem);
          }
          initNetData();
        }, throwable -> {
          baseAt.dismiss();
          mErrorView.setVisibility(View.VISIBLE);
        });
  }

  @Override
  public void OnRankListItem(int position) {

    // 更新当前页面列表
    if (position == 1) {
      Intent intent = new Intent(this, PlayActivity.class);
      startActivity(intent);
    } else {
      int p = position - 2;
      PlayItem item = mPlayList.get(p);
      if (item.isFirstClick) {
        for (int i = 0; i < mPlayList.size(); i++) {
          if (p == i) {
            mPlayList.get(i).isFirstClick = false;
          } else {
            mPlayList.get(i).isFirstClick = true;
          }
        }
      } else {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
      }
    }


    // 更新底部播放栏
    if (Constant.sPlayList != null) {
      Constant.sPlayList = null;
    }
    Constant.sPlayList = new ArrayList<>();
    Constant.sPlayList.addAll(mPlayList);

    saveData(position > 1 ? position - 2 : 0);
    updateBottom();
  }


  private void saveData(int position) {
    // 存进数据库
    PlayStatus.getInstance(this).setType(PlayStatus.ONLINE);
    PlayStatus.getInstance(this).setPosition(position);
    PlayList.getInstance(this).savePlayList(Constant.sPlayList);
    try {
      iBinder.play();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void updateSongList() {
    super.updateSongList();
    // 刷新本页面的歌曲列表状态
    if (mSongAdapter != null)
      for (int i = 0; i < mPlayList.size(); i++) {
      if (mPlayList.get(i).isSelected) {
      mSongAdapter.refreshList(i);
      mSongRecyclerView.scrollToPosition(i + 5);
      } else {
      mPlayList.get(i).isFirstClick = true;
      }
      }
  }
}
