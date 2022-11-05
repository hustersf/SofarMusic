package com.sf.demo.md;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.sf.base.util.FontUtil;
import com.sf.demo.DemoActivity;
import com.sf.demo.R;

/**
 * Created by sufan on 17/7/4.
 */

public class MD1Activity extends DemoActivity {
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private TabLayout md_tl;
    private ViewPager md_vp;
    private List<Fragment> mFragments;
    private List<String> mTitles;

    private OnlineFmAdapter fmAdapter;

    private FloatingActionButton fab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_md1);
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

        head_title.setText("MD效果页面");

        head_right.setVisibility(View.GONE);
    }


    @Override
    public void initView() {
        md_tl=(TabLayout)findViewById(R.id.md_tl);
        md_vp=(ViewPager)findViewById(R.id.md_vp);

        dynamicAddView(md_tl,"background",R.color.head_title_bg_color);

        fab=(FloatingActionButton)findViewById(R.id.fab);
      //  fab.setRippleColor(Color.RED); 点击按钮时的颜色
      //  fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));  //设置fab的颜色
        dynamicAddView(fab,"fabColor",R.color.themeColor);
    }

    @Override
    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(TypeFragment.newInstance("热点"));
        mFragments.add(TypeFragment.newInstance("军事"));
        mFragments.add(TypeFragment.newInstance("科技"));
        mFragments.add(TypeFragment.newInstance("娱乐"));

        mTitles = new ArrayList<>();
        mTitles.add("热点");
        mTitles.add("军事");
        mTitles.add("科技");
        mTitles.add("娱乐");

        FragmentManager fm = getSupportFragmentManager();
        fmAdapter = new OnlineFmAdapter(fm, mFragments,mTitles);
        md_vp.setAdapter(fmAdapter);
        md_vp.setOffscreenPageLimit(mTitles.size());

        md_tl.addTab(md_tl.newTab().setText(mTitles.get(0)));
        md_tl.addTab(md_tl.newTab().setText(mTitles.get(1)));
        md_tl.addTab(md_tl.newTab().setText(mTitles.get(2)));
        md_tl.addTab(md_tl.newTab().setText(mTitles.get(3)));
        md_tl.setupWithViewPager(md_vp);  //是的tab随着viewpager动

    }

    @Override
    public void initEvent() {
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Snackbar.make(fab, "点我分享哦！", Snackbar.LENGTH_SHORT).show();
           }
       });

    }
}
