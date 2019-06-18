package com.sf.sofarmusic.local;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.BaseFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.PlayItem;

/**
 * Created by sufan on 16/12/1.
 * 单曲
 */

public class SingleFragment extends BaseFragment implements SingleAdapter.OnItemClickListener {

    private View view;

    private RecyclerView single_rv;
    private List<PlayItem> mPlayList;
    private SingleAdapter mAdapter;

    private OnUpdateListener mOnUpdateListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_single, container, false);
        initView();
        initData();
        initEvent();
        return view;
    }


    protected void initData() {
        mPlayList = Constant.sLocalList;
        mAdapter = new SingleAdapter(activity, mPlayList);
        single_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

    }


    protected void initView() {
        single_rv = (RecyclerView) view.findViewById(R.id.single_rv);
        single_rv.setLayoutManager(new LinearLayoutManager(activity));

    }


    protected void initEvent() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void OnSingleItem(int position) {

        //更新底部播放栏
        if (Constant.sPlayList != null) {
            Constant.sPlayList = null;
        }
        Constant.sPlayList = new ArrayList<>();
        Constant.sPlayList.addAll(mPlayList);

        //存进数据库
        PlayStatus.getInstance(getActivity()).setType(PlayStatus.LOCAL);
        PlayStatus.getInstance(getActivity()).setPosition(position > 0 ? position - 1 : 0);
        PlayList.getInstance(getActivity()).savePlayList(Constant.sPlayList);

        if (mOnUpdateListener != null) {
            mOnUpdateListener.onUpdate();
        }

    }


    public void refreshData() {
        if (mAdapter != null)
            for (int i = 0; i < mPlayList.size(); i++) {
                if (mPlayList.get(i).isSelected) {
                    mAdapter.refreshList(i);
                    single_rv.scrollToPosition(i + 5);
                }
            }
    }


    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        mOnUpdateListener = onUpdateListener;
    }

    public interface OnUpdateListener {
        void onUpdate();
    }
}
