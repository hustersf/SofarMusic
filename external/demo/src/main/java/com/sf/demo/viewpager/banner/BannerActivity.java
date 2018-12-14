package com.sf.demo.viewpager.banner;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.banner.BannerIndicator;
import com.sf.widget.banner.BannerInfo;
import com.sf.widget.banner.ImageAdapter;

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
    final String[] title = {"最新最酷魔法表情，表达不一样的你", "看精彩视频，发现生活无限可能", "随时随地发现身边好友", "最活跃、自由的视频创作社区"};
    Integer[] imgId = {R.drawable.login_bg_a, R.drawable.login_bg_b, R.drawable.login_bg_c,
        R.drawable.login_bg_d};


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
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
