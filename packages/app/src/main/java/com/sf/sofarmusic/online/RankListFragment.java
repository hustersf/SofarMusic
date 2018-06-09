package com.sf.sofarmusic.online;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.libnet.callback.StringCallback;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.LazyLoadBaseFragment;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.enity.RankItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sufan on 16/11/9.
 * 音乐榜单，待扩展
 */

public class RankListFragment extends LazyLoadBaseFragment implements RankListAdapter.OnItemClickListener {

    private View view;

    private List<RankItem> mRankList;
    private RankListAdapter mRankAdapter;
    private RecyclerView rank_rv;
    private TextView tv_error;

    private Observe mObserve;

    private int[] orders = {1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 13};
    private String[] types = {"1", "2", "200", "20", "21", "24", "23", "25", "22", "11", "8"};
    private String[] urls = {Constant.NewSong, Constant.HotSong, Constant.SelfSong, Constant.ChineseSong,
            Constant.EASong,Constant.FilmSong,Constant.DoubleSong,Constant.NetSong,Constant.OldSong,
    Constant.RockSong,Constant.BillboardSong};

    private boolean hasShowError=false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rank_list, container, false);
        return view;
    }


    @Override
    protected void initData() {
        //先加榜单标题
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


        for(int i=0;i<urls.length;i++){
            getSongList(urls[i],orders[i],types[i]);
        }

        //观察交易的是否全部完成
        activity.show();
        setOnObserve(new Observe() {
            @Override
            public void update(int count) {
                if (count == 14) {
                    activity.dismiss();
                    Collections.sort(mRankList);
                    initRank();
                }
            }
        });

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

        tv_error=(TextView)view.findViewById(R.id.tv_error);
        dynamicAddView(tv_error, "textColor", R.color.main_text_color);
    }

    @Override
    protected void initEvent() {
        //测试是否滑动到了底部
        rank_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    //     ToastUtil.startShort(activity,"已经滑动到底部");
                }
            }
        });

        tv_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_error.setVisibility(View.GONE);
                hasShowError=false;
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
            //没效果
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "rank_head").toBundle());
        } else {
            startActivity(intent);
        }
    }

    //5.0以下 计算View相关属性，用来为共享元素做准备
    private void captureValues(Bundle bundle,View view){
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        bundle.putInt("letf", screenLocation[0]);
        bundle.putInt("top", screenLocation[1]);
        bundle.putInt("width", view.getWidth());
        bundle.putInt("height", view.getHeight());
    }

    //利用观察者模式，当所有数据都请求到了才去更新adapter
    private void setOnObserve(Observe observe) {
        mObserve = observe;
    }

    private interface Observe {

        void update(int count);

    }

    private void getSongList(String url, final int order, final String type) {
        activity.requestGetNoError(Constant.Ip + url, null, new StringCallback() {
            @Override
            public void OnSuccess(String s) {
                RankItem rItem = new RankItem();
                rItem.order = order;
                rItem.type = type;
                List<PlayItem> playList = new ArrayList<PlayItem>();
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONArray songList = jsonObject.getJSONArray("song_list");
                if (songList == null) {
                    songList = new JSONArray();
                }
                JSONObject billboard = jsonObject.getJSONObject("billboard");
                for (int i = 0; i < songList.size(); i++) {
                    PlayItem pItem = new PlayItem();
                    JSONObject info = songList.getJSONObject(i);
                    pItem.songId = info.getString("song_id");
                    pItem.name = info.getString("title");
                    pItem.artist = info.getString("author");
                    pItem.smallUrl = info.getString("pic_small");
                    pItem.bigUrl = info.getString("pic_big");
                    pItem.lrcLinkUrl = info.getString("lrclink");
                    playList.add(pItem);
                }
                rItem.playList = playList;
                rItem.name = billboard.getString("name");
                String count = billboard.getString("billboard_songnum");
                if (count != null)
                    rItem.count = Integer.valueOf(count);
                rItem.imgUrl = billboard.getString("pic_s260");
                rItem.bigImgUrl = billboard.getString("pic_s444");

                mRankList.add(rItem);
                mObserve.update(mRankList.size());
            }

            @Override
            public void OnError(Object o) {
                tv_error.setVisibility(View.VISIBLE);
                if(!hasShowError) {
                    hasShowError=true;
                    Toast.makeText(activity, "网络连接不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
