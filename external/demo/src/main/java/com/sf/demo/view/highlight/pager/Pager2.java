package com.sf.demo.view.highlight.pager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;

import com.sf.demo.R;
import com.sf.demo.list.page.DividePartBean;
import com.sf.demo.list.page.DividePartSubBean;
import com.sf.demo.list.page.PageGridView;
import com.sf.demo.list.page.PageIndicator;
import com.sf.demo.view.highlight.GridAdapter;

/**
 * Created by sufan on 17/7/28.
 */

public class Pager2 extends BasePager {

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


    public Pager2(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_tab2, null);
        pageGridView = (PageGridView)view. findViewById(R.id.page_gv);
        pageIndicator = (PageIndicator)view. findViewById(R.id.indicator);
        return view;
    }

    public void initData(){
        //初始化数据
        if(!isPagerDataLoaded()) {
            setPagerDataLoaded(true);
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
            mAdapter = new GridAdapter(mActivity, partBean, 1);
            pageGridView.initConfig(partBean.row, partBean.column, null);
            pageGridView.setAdapter(mAdapter);

            int pageCount = partBean.column * partBean.row;
            int pages = mSubBeanList.size() / pageCount;
            int maxPages = mSubBeanList.size() % pageCount == 0 ? pages : pages + 1;
            if (maxPages > 1) {
                pageIndicator.setVisibility(View.VISIBLE);
                pageGridView.setPageIndicator(pageIndicator);
            } else {
                pageIndicator.setVisibility(View.GONE);
            }
        }
    }
}
