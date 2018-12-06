package com.sf.sofarmusic.local;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.LazyLoadBaseFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.enity.AlbumItem;

/**
 * Created by sufan on 16/12/1.
 * 专辑
 */

public class AlbumFragment extends LazyLoadBaseFragment implements AlbumAdapter.OnItemClickListener {

    private View view;


    private RecyclerView album_rv;
    private List<AlbumItem> mAlbumList;
    private AlbumAdapter mAdapter;

    private static final int REQUEST_CODE=101;  //>0的整数即可

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_album, container, false);
        return view;
    }


    @Override
    protected void initData() {
        mAlbumList = MusicLoader.getInstance().getLocalAlbumList(Constant.sLocalList);
        mAdapter = new AlbumAdapter(activity, mAlbumList);
        album_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    protected void initView() {
        album_rv = (RecyclerView) view.findViewById(R.id.album_rv);
        album_rv.setLayoutManager(new LinearLayoutManager(activity));

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void OnAlbumItem(int position) {

        AlbumItem item = mAlbumList.get(position);


        Intent intent = new Intent(activity, ShowDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle bundle = new Bundle();
        bundle.putString("title", item.albumName);
        //   bundle.putSerializable("list", playList);
        Constant.sPreList = item.albumList;   //
        intent.putExtras(bundle);

        startActivityForResult(intent,REQUEST_CODE);

    }

    public void refreshData() {
        if (isInit) {
            initData();
        }
    }
}
