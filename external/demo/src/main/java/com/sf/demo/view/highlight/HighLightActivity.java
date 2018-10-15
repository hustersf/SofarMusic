package com.sf.demo.view.highlight;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.data.DemoData;
import com.sf.demo.view.highlight.component.ComponentTab3;
import com.sf.demo.view.highlight.core.Component;
import com.sf.demo.view.highlight.core.Guide;
import com.sf.demo.view.highlight.core.GuideBuilder;
import com.sf.demo.view.highlight.pager.BasePager;
import com.sf.demo.view.highlight.pager.HomePagerAdapter;
import com.sf.demo.view.highlight.pager.Pager1;
import com.sf.demo.view.highlight.pager.Pager2;
import com.sf.demo.view.highlight.pager.Pager3;
import com.sf.demo.view.highlight.pager.Pager4;
import com.sf.utility.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/7/27.
 */

public class HighLightActivity extends UIRootActivity {

    private NoScrollViewPager home_vp;
    private LinearLayout tab_container_ll;

    private List<Tab> tabList;
    private ImageView[] iv_tab;
    private TextView[] tv_tab;
    private int[] colors = new int[]{Color.parseColor("#DE4A40"), Color.GRAY, Color.parseColor("#DE4A40"), Color.GRAY};
    private int[][] states = new int[][]{{android.R.attr.state_selected}, {-android.R.attr.state_selected}, {android.R.attr.state_pressed}, {-android.R.attr.state_pressed}};
    private ColorStateList colorStateList = new ColorStateList(states, colors);


    private Pager1 p1;
    private Pager2 p2;
    private Pager3 p3;
    private Pager4 p4;
    private List<BasePager> mPagerList;
    private HomePagerAdapter mPagerAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_high_light;
    }

    @Override
    protected void initTitle() {
        head_title.setText("高亮引导图");
    }

    @Override
    public void initView() {
        home_vp = (NoScrollViewPager) findViewById(R.id.home_vp);
        tab_container_ll = (LinearLayout) findViewById(R.id.tab_container_ll);
    }

    @Override
    public void initData() {
        //初始化底部tab
        initTabs();

        //初始化viewpager
        initViewPager();


    }

    @Override
    public void initEvent() {

    }

    private void initViewPager() {
        mPagerList = new ArrayList<>();
        p1 = new Pager1(this);
        p2 = new Pager2(this);
        p3 = new Pager3(this);
        p4 = new Pager4(this);
        mPagerList.add(p1);
        mPagerList.add(p2);
        mPagerList.add(p3);
        mPagerList.add(p4);

        mPagerAdapter = new HomePagerAdapter(mPagerList);
        home_vp.setAdapter(mPagerAdapter);
        home_vp.setOffscreenPageLimit(mPagerList.size());
        setCurrentPager(0);

    }


    private void initTabs() {
        tabList = DemoData.getTabs();
        LinearLayout[] ll_sub_container = new LinearLayout[tabList.size()];
        iv_tab = new ImageView[tabList.size()];
        tv_tab = new TextView[tabList.size()];
        // 得到屏幕的宽度
        int width = this.getResources().getDisplayMetrics().widthPixels;
        for (int i = 0; i < tabList.size(); i++) {
            Tab tab = tabList.get(i);

            ll_sub_container[i] = new LinearLayout(this);
            ll_sub_container[i].setOrientation(LinearLayout.VERTICAL);
            ll_sub_container[i].setGravity(Gravity.CENTER);
            ll_sub_container[i].setLayoutParams(new LinearLayout.LayoutParams(width / tabList.size(), DensityUtil.dp2px(this, 50), 1));


            tv_tab[i] = new TextView(this);
            tv_tab[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_tab[i].setText(tab.title);
            tv_tab[i].setGravity(Gravity.CENTER);
            tv_tab[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv_tab[i].setClickable(true);
            tv_tab[i].setOnClickListener(new TabOnClickListener(i));
            tv_tab[i].setTextColor(colorStateList);
            tv_tab[i].setOnClickListener(new TabOnClickListener(i));

            iv_tab[i] = new ImageView(this);
            iv_tab[i].setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dp2px(this, 30), DensityUtil.dp2px(this, 30)));
            iv_tab[i].setBackgroundColor(Color.TRANSPARENT);
            setImageViewTint(tab.imgId, iv_tab[i]);
            iv_tab[i].setOnClickListener(new TabOnClickListener(i));

            ll_sub_container[i].addView(iv_tab[i]);
            ll_sub_container[i].addView(tv_tab[i]);
            tab_container_ll.addView(ll_sub_container[i]);

        }

    }


    private void setImageViewTint(int imgId, ImageView imageView) {

        Drawable drawable = ContextCompat.getDrawable(this,imgId);
        Drawable wraperDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintMode(wraperDrawable, PorterDuff.Mode.SRC_IN);
        DrawableCompat.setTintList(wraperDrawable, colorStateList);
        imageView.setImageDrawable(wraperDrawable);

    }


    private class TabOnClickListener implements View.OnClickListener {
        private int position;

        public TabOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            setCurrentPager(position);
        }

    }


    private void setCurrentPager(int position) {

        for (int i = 0; i < tabList.size(); i++) {
            tv_tab[i].setSelected(false);
            iv_tab[i].setSelected(false);
        }
        iv_tab[position].setSelected(true);
        tv_tab[position].setSelected(true);

        if (home_vp.getCurrentItem() != position)
            home_vp.setCurrentItem(position, false);

        if (position == 0) {
            p1.initData();
        } else if (position == 1) {
            p2.initData();
        } else if (position == 2) {
            p3.initData();
            showGuideView();
        } else if (position == 3) {
            p4.initData();
        }

    }

    private void showGuideView() {

        head_back.post(new Runnable() {
            @Override
            public void run() {
                GuideBuilder builder = new GuideBuilder();
                builder.setTargetView(head_back)
                        .setAlpha(100)
                        .setHighTargetGraphStyle(Component.CIRCLE)
                        .setHighTargetPadding(10)
                        .setOverlayTarget(false)
                        .setOutsideTouchable(false);
                builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                    @Override
                    public void onShown() {
                    }

                    @Override
                    public void onDismiss() {
                    }
                });

                builder.addComponent(new ComponentTab3());
                Guide guide = builder.createGuide();
                guide.setShouldCheckLocInWindow(false);
                guide.show(HighLightActivity.this);
            }
        });
    }
}
