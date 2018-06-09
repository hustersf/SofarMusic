package com.sf.sofarmusic.demo.view.highlight.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.BaseFragment;
import com.sf.sofarmusic.demo.list.page.DividePartBean;
import com.sf.sofarmusic.demo.list.page.DividePartSubBean;
import com.sf.sofarmusic.demo.list.page.PageGridView;
import com.sf.sofarmusic.demo.list.page.PageIndicator;
import com.sf.sofarmusic.demo.view.highlight.GridAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/7/27.
 */

public class TabFragment2 extends BaseFragment {

    private View view;

    private PageGridView pageGridView;
    private PageIndicator pageIndicator;

    private final int ROW = 2;
    private final int COLUMN = 4;

    private List<DividePartSubBean> mSubBeanList;
    private GridAdapter mAdapter;

    private String[] titles = {
            "手机充值", "生活缴费", "交通罚款", "游戏点卡",
            "掌上商城", "ETC缴费", "智慧校园", "彩票站点缴费",
    };
    private String[] imgNames = {
            "sjcz", "shjf", "jtfk", "yxdk",
            "zssc", "etc_jf", "zhxy", "cpzdjf",
            };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab2, container, false);
        initView();
        return view;
    }

    public void initData(){
        //初始化数据
        mSubBeanList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            DividePartSubBean subBean = new DividePartSubBean();
            subBean.title = titles[i];
            subBean.imgName = imgNames[i];
            mSubBeanList.add(subBean);
        }

        DividePartBean partBean = new DividePartBean();
        partBean.column = COLUMN;
        partBean.row = ROW;
        partBean.list = mSubBeanList;

        //将数据加进PageGridView
        mAdapter = new GridAdapter(getActivity(), partBean,1);
        pageGridView.initConfig(partBean.row, partBean.column, null);
        pageGridView.setAdapter(mAdapter);

        int pageCount = partBean.column * partBean.row;
        int pages = mSubBeanList.size() / pageCount;
        int maxPages = mSubBeanList.size() % pageCount == 0 ? pages : pages + 1;
        if(maxPages>1){
            pageIndicator.setVisibility(View.VISIBLE);
            pageGridView.setPageIndicator(pageIndicator);
        }else {
            pageIndicator.setVisibility(View.GONE);
        }

    }

    private void initView(){
        pageGridView = (PageGridView)view. findViewById(R.id.page_gv);
        pageIndicator = (PageIndicator)view. findViewById(R.id.indicator);

    }

    private void initEvent(){

    }
}
