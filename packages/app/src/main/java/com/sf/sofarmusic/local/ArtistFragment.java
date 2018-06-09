package com.sf.sofarmusic.local;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sf.libnet.callback.StringCallback;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.LazyLoadBaseFragment;
import com.sf.sofarmusic.enity.ArtistItem;

import java.util.List;

/**
 * Created by sufan on 16/12/1.
 * 歌手
 */

public class ArtistFragment extends LazyLoadBaseFragment implements ArtistAdapter.OnItemClickListener {


    private View view;

    private RecyclerView artist_rv;
    private List<ArtistItem> mArtistList;
    private ArtistAdapter mAdapter;

    private int mIndex = 0;   //获取歌手url的次数

    private static final int REQUEST_CODE = 100;  //>0的整数即可

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_artist, container, false);
        return view;
    }


    @Override
    protected void initData() {
        mArtistList = MusicLoader.getInstance().getLocalArtistList(Constant.sLocalList);
        mAdapter = new ArtistAdapter(activity, mArtistList);
        artist_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        getArtistUrl();
    }


    //通过酷狗接口获取歌手图片的url
    private void getArtistUrl() {
        for (int i = 0; i < mArtistList.size(); i++) {
            final ArtistItem item = mArtistList.get(i);
            String baseUrl = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&api_key=fdb3a51437d4281d4d64964d333531d4&format=json";
            activity.requestGet(baseUrl + "&artist=" + item.name, null, new StringCallback() {
                @Override
                public void OnSuccess(String s) {
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    JSONObject artistJson = jsonObject.getJSONObject("artist");
                    if (artistJson != null) {
                        JSONArray imageArray = artistJson.getJSONArray("image");
                        String mediumUrl = "";
                        String extraLargeUrl = "";
                        if (imageArray != null) {
                            mediumUrl = imageArray.getJSONObject(1).getString("#text");
                            extraLargeUrl = imageArray.getJSONObject(3).getString("#text");
                        }
                        item.mediumUrl = (mediumUrl);
                        item.extraLargeUrl = (extraLargeUrl);
                    }
                    mIndex++;
                    if (mIndex == mArtistList.size()) {
                        mAdapter = new ArtistAdapter(activity, mArtistList);
                        artist_rv.setAdapter(mAdapter);
                    }

                }

                @Override
                public void OnError(Object o) {

                }
            });
        }

    }


    @Override
    protected void initView() {
        artist_rv = (RecyclerView) view.findViewById(R.id.artist_rv);
        artist_rv.setLayoutManager(new LinearLayoutManager(activity));

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void OnArtistItem(int position) {
        ArtistItem item = mArtistList.get(position);

        Intent intent = new Intent(activity, ShowDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle bundle = new Bundle();
        bundle.putString("title", item.name);
        //    bundle.putSerializable("list",playList);
        Constant.sPreList = item.artistList;   //
        intent.putExtras(bundle);

        startActivityForResult(intent, REQUEST_CODE);

    }

    public void refreshData() {
        if (isInit) {
            initData();
        }
    }
}
