package com.sf.sofarmusic.demo.viewpager.banner;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.util.DeviceUtil;
import com.sf.sofarmusic.util.FontUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/24.
 */

public class BannerActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private ViewPager banner_vp;
    private BannerIndicator banner_indicator;
    private RelativeLayout banner_rl;

    private ImageAdapter mAdapter;
    private List<BannerInfo> mBannerList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_banner);
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

        head_title.setText("无线循环广告");

        head_right.setVisibility(View.GONE);

    }

    @Override
    public void initView() {
        banner_vp = (ViewPager) findViewById(R.id.banner_vp);
        banner_indicator = (BannerIndicator) findViewById(R.id.banner_indicator);

        banner_rl = (RelativeLayout) findViewById(R.id.banner_rl);

    }

    @Override
    public void initData() {
        final String[] title = {"广告1", "广告2","广告3"};
        Integer[] imgId = {R.drawable.demo_banner1, R.drawable.demo_banner2,R.drawable.demo_banner3};


        //动态设置广告的宽高
        int width = DeviceUtil.getMetricsWidth(this);
        int height = width / 2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        banner_rl.setLayoutParams(lp);

        //添加小点
        if (title.length > 1) {
            banner_indicator.InitIndicatorItems(title.length);
            banner_indicator.setVisibility(View.VISIBLE);
        } else {
            banner_indicator.setVisibility(View.GONE);
        }

        mBannerList = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            BannerInfo item = new BannerInfo();
            item.imgId = imgId[i];
            item.name = title[i];
            mBannerList.add(item);
        }
        mAdapter = new ImageAdapter(this, mBannerList, banner_vp, banner_indicator);

        mAdapter.startAutoRun();

    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.stopAutoRun();
    }
}
