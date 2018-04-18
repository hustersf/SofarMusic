package com.sf.sofarmusic.demo.viewpager.banner;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.UIRootActivity;
import com.sf.sofarmusic.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/24.
 */

public class BannerActivity extends UIRootActivity {

    private ViewPager banner_vp;
    private BannerIndicator banner_indicator;
    private RelativeLayout banner_rl;

    private ImageAdapter mAdapter;
    private List<BannerInfo> mBannerList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_banner;
    }

    @Override
    protected void initTitle() {
        head_title.setText("无线循环广告");
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
