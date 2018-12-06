package com.sf.sofarmusic.online;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sf.base.LazyLoadBaseFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.enity.RankItem;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.model.response.RankSongsResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by sufan on 16/11/9.
 * 音乐榜单，待扩展
 */

public class RankListFragment extends LazyLoadBaseFragment
    implements
      RankListAdapter.OnItemClickListener {


  private List<RankItem> mRankList;
  private RankListAdapter mRankAdapter;
  private RecyclerView mRankRecyclerView;
  private TextView mErrorView;

  private int[] rankOrders = {1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 13};
  private int[] rankTypes = {RankType.NEW_SONG, RankType.HOT_SONG, RankType.SELF_SONG,
      RankType.CHINA_SONG, RankType.EA_SONG, RankType.FILM_SONG, RankType.DOUBLE_SONG,
      RankType.NET_SONG, RankType.OLD_SONG, RankType.ROCK_SONG, RankType.BILLBOARD_SONG};

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_rank_list, container, false);
  }


  @Override
  protected void initData() {
    // 先加榜单标题
    mRankList = new ArrayList<>();
    RankItem mainItem = new RankItem();
    mainItem.order = 0;
    mainItem.isTitle = true;
    mainItem.name = "主打榜单";
    mRankList.add(mainItem);

    RankItem typeItem = new RankItem();
    typeItem.order = 4;
    typeItem.isTitle = true;
    typeItem.name = "分类榜单";
    mRankList.add(typeItem);

    RankItem boardItem = new RankItem();
    boardItem.order = 12;
    boardItem.isTitle = true;
    boardItem.name = "媒体榜单";
    mRankList.add(boardItem);

    // 创建请求
    List<Observable<RankSongsResponse>> observables = new ArrayList<>();
    for (int i = 0; i < rankTypes.length; i++) {
      observables.add(ApiProvider.getMusicApiService().getRankSongs(rankTypes[i], 3, 0));
    }

    activity.show();
    Observable.zip(observables, new Function<Object[], Boolean>() {
      @Override
      public Boolean apply(Object[] objects) throws Exception {
        for (int i = 0; i < objects.length; i++) {
          RankSongsResponse response = (RankSongsResponse) objects[i];
          RankItem rankItem = new RankItem();
          rankItem.order = rankOrders[i];
          rankItem.type = rankTypes[i];

          List<PlayItem> playList = new ArrayList<>();
          for (Song song : response.mSongList) {
            PlayItem playItem = new PlayItem();
            playItem.songId = song.mId;
            playItem.name = song.mName;
            playItem.artist = song.mAuthor;
            playItem.smallUrl = song.mCoverUrl;
            playItem.bigUrl = song.mBigCoverUrl;
            playItem.lrcLinkUrl = song.mLrcLink;
            playList.add(playItem);
          }
          rankItem.playList = playList;

          rankItem.name = response.mBillBoard.mName;
          rankItem.count = response.mBillBoard.mCount;
          rankItem.imgUrl = response.mBillBoard.mCoverUrl;
          rankItem.bigImgUrl = response.mBillBoard.mBigCoverUrl;
          mRankList.add(rankItem);
        }
        if (objects.length == rankTypes.length) {
          return true;
        }
        return false;
      }
    }).observeOn(AndroidSchedulers.mainThread())
        .compose(this.bindToLifecycle())
        .subscribe(aBoolean -> {
          activity.dismiss();
          if (aBoolean) {
            Collections.sort(mRankList);
            initRank();
          } else {
            showOrHideError(true);
          }
        }, throwable -> {
          activity.dismiss();
          showOrHideError(true);
        });
  }

  private void showOrHideError(boolean show) {
    if (show) {
      mErrorView.setVisibility(View.VISIBLE);
      Toast.makeText(activity, "网络连接不可用，请稍后再试", Toast.LENGTH_SHORT).show();
    } else {
      mErrorView.setVisibility(View.GONE);
    }
  }

  private void initRank() {
    mRankAdapter = new RankListAdapter(activity, mRankList);
    mRankAdapter.setOnItemClickListener(this);
    mRankRecyclerView.setAdapter(mRankAdapter);
  }

  @Override
  protected void initView() {
    mRankRecyclerView = (RecyclerView) mView.findViewById(R.id.rank_rv);
    mRankRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

    mErrorView = (TextView) mView.findViewById(R.id.tv_error);
    dynamicAddView(mErrorView, "textColor", R.color.main_text_color);
  }

  @Override
  protected void initEvent() {
    // 测试是否滑动到了底部
    mRankRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!recyclerView.canScrollVertically(1)) {

        }
      }
    });

    mErrorView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showOrHideError(false);
        initData();
      }
    });

  }


  @Override
  public void onRankItem(int position, View view) {
    RankItem item = mRankList.get(position);
    Intent intent = new Intent(getActivity(), SongListActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("name", item.name);
    bundle.putInt("type", item.type);
    bundle.putInt("count", item.count);
    bundle.putString("imgUrl", item.bigImgUrl);
    intent.putExtras(bundle);
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    if (Build.VERSION.SDK_INT >= 21) {
      // 没效果
      startActivity(intent, ActivityOptions
          .makeSceneTransitionAnimation(getActivity(), view, "rank_head").toBundle());
    } else {
      startActivity(intent);
    }
  }

  // 5.0以下 计算View相关属性，用来为共享元素做准备
  private void captureValues(Bundle bundle, View view) {
    int[] screenLocation = new int[2];
    view.getLocationOnScreen(screenLocation);
    bundle.putInt("letf", screenLocation[0]);
    bundle.putInt("top", screenLocation[1]);
    bundle.putInt("width", view.getWidth());
    bundle.putInt("height", view.getHeight());
  }
}
