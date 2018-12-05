package com.sf.sofarmusic.online;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.libnet.callback.StringCallback;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.base.Constant;
import com.sf.base.LazyLoadBaseFragment;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.enity.RankItem;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.model.response.RankSongsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by sufan on 16/11/9.
 * 音乐榜单，待扩展
 */

public class RankListFragment extends LazyLoadBaseFragment
    implements
      RankListAdapter.OnItemClickListener {

  private View view;

  private List<RankItem> mRankList;
  private RankListAdapter mRankAdapter;
  private RecyclerView rank_rv;
  private TextView tv_error;


  private int[] orders = {1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 13};
  private String[] types = {"1", "2", "200", "20", "21", "24", "23", "25", "22", "11", "8"};

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_rank_list, container, false);
    return view;
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
    for (int i = 0; i < types.length; i++) {
      observables.add(ApiProvider.getMusicApiService().getRankSongs(types[i], 3, 0));
    }

    activity.show();
    Observable.zip(observables, new Function<Object[], Boolean>() {
      @Override
      public Boolean apply(Object[] objects) throws Exception {
        for (int i = 0; i < objects.length; i++) {
          RankSongsResponse response = (RankSongsResponse) objects[i];
          RankItem rankItem = new RankItem();
          rankItem.order = orders[i];
          rankItem.type = types[i];

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
          rankItem.imgUrl = response.mBillBoard.mCoverUrl;
          rankItem.bigImgUrl = response.mBillBoard.mBigCoverUrl;
          mRankList.add(rankItem);
        }
        if (objects.length == types.length) {
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
      tv_error.setVisibility(View.VISIBLE);
      Toast.makeText(activity, "网络连接不可用，请稍后再试", Toast.LENGTH_SHORT).show();
    } else {
      tv_error.setVisibility(View.GONE);
    }
  }

  private void initRank() {
    mRankAdapter = new RankListAdapter(activity, mRankList);
    mRankAdapter.setOnItemClickListener(this);
    rank_rv.setAdapter(mRankAdapter);
  }

  @Override
  protected void initView() {
    rank_rv = (RecyclerView) view.findViewById(R.id.rank_rv);
    rank_rv.setLayoutManager(new LinearLayoutManager(activity));

    tv_error = (TextView) view.findViewById(R.id.tv_error);
    dynamicAddView(tv_error, "textColor", R.color.main_text_color);
  }

  @Override
  protected void initEvent() {
    // 测试是否滑动到了底部
    rank_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!recyclerView.canScrollVertically(1)) {

        }
      }
    });

    tv_error.setOnClickListener(new View.OnClickListener() {
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
    bundle.putString("type", item.type);
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
