package com.sf.sofarmusic.demo.list.refresh;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 2017/11/6.
 */

public class LoadMoreActivity extends DemoActivity {
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;


    private RecyclerView rv_loadMore;
    private List<String> mDatas;
    private LoadMoreAdapterWrapper mWrapper;

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_load_more);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initHead() {

        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("上拉加载更多");

        head_right.setVisibility(View.GONE);

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
