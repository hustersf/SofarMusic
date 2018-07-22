package com.sf.sofarmusic.demo.list.refresh;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sf.sofarmusic.R;
import com.sf.base.UIRootActivity;
import com.sf.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 2017/11/6.
 */

public class LoadMoreActivity extends UIRootActivity {

    private RecyclerView rv_loadMore;
    private List<String> mDatas;
    private LoadMoreAdapterWrapper mWrapper;

    private Handler mHandler;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_load_more;
    }

    @Override
    protected void initTitle() {
        head_title.setText("上拉加载更多");
    }

    @Override
    public void initView() {
        rv_loadMore = (RecyclerView) findViewById(R.id.rv_loadMore);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rv_loadMore.setLayoutManager(manager);

    }

    @Override
    public void initData() {
        mHandler = new Handler(Looper.getMainLooper());
        mDatas = new ArrayList<>();
        getDatas();

        LoadMoreAdapter loadMoreAdapter = new LoadMoreAdapter(this, mDatas);
        mWrapper = new LoadMoreAdapterWrapper(loadMoreAdapter);
        rv_loadMore.setAdapter(mWrapper);
    }

    @Override
    public void initEvent() {
        rv_loadMore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动到底部
                if (!recyclerView.canScrollVertically(1)) {
                    LogUtil.d("TAG","if");
                    mWrapper.updateLoadStatus(LoadMoreAdapterWrapper.LOADING);
                    if (mDatas.size() < 52) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getDatas();
                                mWrapper.updateLoadStatus(LoadMoreAdapterWrapper.LOADING_FINISH);
                            }
                        }, 2000);
                    } else {
                        mWrapper.updateLoadStatus(LoadMoreAdapterWrapper.LOADING_NONE);
                    }
                }else {
                    LogUtil.d("TAG","else");
                }
            }
        });

    }

    private void getDatas() {
        char letter = 'A';
        for (int i = 0; i < 26; i++) {
            mDatas.add(String.valueOf(letter));
            letter++;
        }
    }
}
