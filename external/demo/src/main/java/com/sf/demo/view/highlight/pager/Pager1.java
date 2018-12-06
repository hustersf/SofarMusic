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

public class Pager1 extends BasePager {

    private PageGridView pageGridView;
    private PageIndicator pageIndicator;

    private final int ROW = 2;
    private final int COLUMN = 4;

    private List<DividePartSubBean> mSubBeanList;
    private GridAdapter mAdapter;

    private String[] titles = {"账户管理", "交易明细", "交易日志", "综合账单",
            "转账汇款", "资金归集", "他行收款", "面对面付款",
    };
    private String[] imgNames = {"zhgl", "jymx", "jyrz", "zhzd",
            "zzhk", "zjgj", "thsk", "mdmfk",
    };


    public Pager1(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_tab1, null);
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
            mAdapter = new GridAdapter(mActivity, partBean, 0);
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
