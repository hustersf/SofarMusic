package com.sf.sofarmusic.online;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.libnet.callback.StringCallback;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.PlayerBaseActivity;
import com.sf.sofarmusic.data.LocalData;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.MenuItem;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.LogUtil;
import com.sf.sofarmusic.util.PopUtil;

import java.util.ArrayList;
import java.util.List;

import static com.sf.sofarmusic.base.Constant.Ip;

/**
 * Created by sufan on 17/4/9.
 */

public class SongListActivity extends PlayerBaseActivity implements SongListAdapter.OnItemClickListener {

    private Toolbar toolbar;
    private TextView back_tv, title_tv, serach_tv, menu_tv;

    private TextView tv_error;

    private RecyclerView song_rv;
    private SongListAdapter mSongAdapter;
    private List<PlayItem> mPlayList;

    private List<MenuItem> mMenuList;

    private String mImgUrl;
    private String mUrl;  //请求地址，拼接
    private String mType;
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
        mType = bundle.getString("type");
        if ("26".equals(mType)) {
            //26是歌手列表
            mImgUrl = bundle.getString("imgUrl");
            title_tv.setText(bundle.getString("name"));
            String tinguid = bundle.getString("tinguid");
            int count = 100;
            mUrl = Ip + "method=baidu.ting.artist.getSongList&tinguid=" + tinguid + "&limits=" + count + "&use_cluster=1&order=2";
            mHeadType = 2;
        } else {
            int count = bundle.getInt("count");
            if (count == 0) {
                count = 20;
            }
            mImgUrl = bundle.getString("imgUrl");
            title_tv.setText(bundle.getString("name"));

            mUrl = Ip + Constant.SongList + "type=" + mType + "&size=" + count + "&offset=0";
            mHeadType = 1;
        }

        mMenuList = LocalData.getRankListMenuData();
        getSongList();
    }

    private void getSongList(){
        mPlayList = new ArrayList<>();
        if ("26".equals(mType)) {
            getArtistSong();
        } else {
            getAllSong();
        }
    }

    private void initNetData() {
        mSongAdapter = new SongListAdapter(baseAt, mPlayList, mImgUrl, mHeadType);
        song_rv.setAdapter(mSongAdapter);

        mSongAdapter.setOnItemClickListener(this);
        mSongAdapter.setOnColorCallback(new SongListAdapter.ColorCallback() {
            @Override
            public void OnColor(int color) {
                mColor = color;
            }
        });
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back_tv = (TextView) findViewById(R.id.back_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        serach_tv = (TextView) findViewById(R.id.search_tv);
        menu_tv = (TextView) findViewById(R.id.menu_tv);
        Typeface iconfont = FontUtil.setFont(baseAt);
        back_tv.setTypeface(iconfont);
        serach_tv.setTypeface(iconfont);
        menu_tv.setTypeface(iconfont);

        song_rv = (RecyclerView) findViewById(R.id.song_rv);
        song_rv.setLayoutManager(new LinearLayoutManager(baseAt));

        tv_error = (TextView) findViewById(R.id.tv_error);
        dynamicAddView(tv_error, "textColor", R.color.main_text_color);

    }

    private void initEvent() {
        tv_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_error.setVisibility(View.GONE);
                getSongList();
            }
        });

        back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        menu_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUtil.showMenuPop(baseAt, mMenuList);
            }
        });

        song_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstH = recyclerView.getChildAt(0).getHeight();
                int titleH = toolbar.getHeight();
                int totalH = firstH - titleH;

                int y = getScollYDistance();
                if (y >= totalH) {
                    y = totalH;
                }

                int alpha = (int) ((y * 1.0f) / totalH * 255);
                //改变标题栏
                if (alpha > 125) {
                    title_tv.setVisibility(View.VISIBLE);
                } else {
                    title_tv.setVisibility(View.GONE);
                }
                toolbar.setBackgroundColor(mColor);
                toolbar.getBackground().setAlpha(alpha);
            }
        });

    }


    private int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) song_rv.getLayoutManager();
        int postion = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(postion);
        int itemHeight = firstVisibleChildView.getHeight();
        return (postion) * itemHeight - firstVisibleChildView.getTop();
    }


    private void getArtistSong() {
        baseAt.show();
        baseAt.requestGet(mUrl, null, new StringCallback() {
            @Override
            public void OnSuccess(String s) {
                baseAt.dismiss();
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONArray songList = jsonObject.getJSONArray("songlist");
                for (int i = 0; i < songList.size(); i++) {
                    PlayItem pItem = new PlayItem();
                    JSONObject info = songList.getJSONObject(i);
                    pItem.songId = info.getString("song_id");
                    pItem.name = info.getString("title");
                    pItem.artist = info.getString("author");
                    pItem.smallUrl = info.getString("pic_small");
                    pItem.bigUrl = info.getString("pic_big");
                    pItem.lrcLinkUrl = info.getString("lrclink");
                    if (i < 3) {
                        pItem.isImport = (true);
                    }
                    mPlayList.add(pItem);
                }
                initNetData();
            }

            @Override
            public void OnError(Object o) {
                tv_error.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getAllSong() {
        baseAt.show();
        baseAt.requestGet(mUrl, null, new StringCallback() {
            @Override
            public void OnSuccess(String s) {
                baseAt.dismiss();
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONArray songList = jsonObject.getJSONArray("song_list");
                for (int i = 0; i < songList.size(); i++) {
                    PlayItem pItem = new PlayItem();
                    JSONObject info = songList.getJSONObject(i);
                    pItem.songId = info.getString("song_id");
                    pItem.name = info.getString("title");
                    pItem.artist = info.getString("author");
                    pItem.smallUrl = info.getString("pic_small");
                    pItem.bigUrl = info.getString("pic_big");
                    pItem.lrcLinkUrl = info.getString("lrclink");
                    if (i < 3) {
                        pItem.isImport = (true);
                    }
                    mPlayList.add(pItem);
                }

                initNetData();
            }

            @Override
            public void OnError(Object o) {
                tv_error.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void OnRankListItem(int position) {

        //更新当前页面列表
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


        //更新底部播放栏
        if (Constant.sPlayList != null) {
            Constant.sPlayList = null;
        }
        Constant.sPlayList = new ArrayList<>();
        Constant.sPlayList.addAll(mPlayList);

        saveData(position > 1 ? position - 2 : 0);
        updateBottom();
    }


    private void saveData(int position) {
        //存进数据库
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
        //刷新本页面的歌曲列表状态
        if (mSongAdapter != null)
            for (int i = 0; i < mPlayList.size(); i++) {
                if (mPlayList.get(i).isSelected) {
                    mSongAdapter.refreshList(i);
                    song_rv.scrollToPosition(i + 5);
                } else {
                    mPlayList.get(i).isFirstClick = true;
                }
            }
    }
}
