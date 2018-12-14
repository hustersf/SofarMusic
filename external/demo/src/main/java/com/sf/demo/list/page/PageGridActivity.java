package com.sf.demo.list.page;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.util.TypedValue;
import android.view.View;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/6/22.
 */

public class PageGridActivity extends UIRootActivity {

    private PageGridView pageGridView;
    private PageIndicator pageIndicator;

    private List<DividePartSubBean> mSubBeanList;
    private PageGridAdapter mAdapter;

    //3行4列
    private final int ROW = 3;
    private final int COLUMN = 4;

    private String[] titles = {"账户管理", "交易明细", "交易日志", "综合账单",
            "转账汇款", "资金归集", "他行收款", "面对面付款",
            "手机充值", "生活缴费", "交通罚款", "游戏点卡",
            "掌上商城", "ETC缴费", "智慧校园", "彩票站点缴费",
            "公园门票"};
    private String[] imgNames = {"zhgl", "jymx", "jyrz", "zhzd",
            "zzhk", "zjgj", "thsk", "mdmfk",
            "sjcz", "shjf", "jtfk", "yxdk",
            "zssc", "etc_jf", "zhxy", "cpzdjf",
            "gymp"};


    @Override
    protected int getLayoutId() {
        return R.layout.activity_page_grid;
    }

    @Override
    protected void initTitle() {
        head_title.setText("分页菜单");
    }

    @Override
    public void initView() {
        pageGridView = (PageGridView) findViewById(R.id.page_gv);
        pageIndicator = (PageIndicator) findViewById(R.id.indicator);

    }

    @Override
    public void initData() {

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


        //测试分割线
        GradientDrawable shapedrawable = new GradientDrawable();
        shapedrawable.setShape(GradientDrawable.RECTANGLE);
        shapedrawable.setColor(Color.parseColor("#e5e5e5"));
        int defaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2f, getResources().getDisplayMetrics());
        shapedrawable.setSize(defaultWidth, defaultWidth);

        DividerItemDecoration d1=new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
        d1.setDrawable(shapedrawable);
        DividerItemDecoration d2=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        d2.setDrawable(shapedrawable);
        pageGridView.addItemDecoration(d1);
        pageGridView.addItemDecoration(d2);

        //将数据加进PageGridView
        mAdapter = new PageGridAdapter(this, partBean);
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

    @Override
    public void initEvent() {
        pageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
            @Override
            public void onItemClick(PageGridView pageGridView, int position) {
                ToastUtil.startShort(baseAt,mSubBeanList.get(position).title);
            }
        });





    }
}
