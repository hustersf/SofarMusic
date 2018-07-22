package com.sf.sofarmusic.demo.viewpager;

import android.content.Intent;

import com.sf.sofarmusic.R;
import com.sf.base.UIRootActivity;
import com.sf.sofarmusic.demo.viewpager.banner.BannerActivity;
import com.sf.sofarmusic.demo.viewpager.gallery.GalleryActivity;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/6/27.
 */

public class VPShowActivity extends UIRootActivity {

    private FlowTagList tag_fl;

    private String[] mTags = {"无线循环广告","画廊"};


    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo_show;
    }

    @Override
    protected void initTitle() {
        head_title.setText("ViewPager效果集合");
    }

    @Override
    public void initView() {
        tag_fl = (FlowTagList) findViewById(R.id.tag_fl);
        dynamicAddView(tag_fl, "tagColor", R.color.themeColor);
    }

    @Override
    public void initData() {
        tag_fl.setTags(mTags);
    }

    @Override
    public void initEvent() {
        tag_fl.setOnTagClickListener(new FlowTagList.OnTagClickListener() {
            @Override
            public void OnTagClick(String text, int position) {
                for (int i = 0; i < mTags.length; i++) {
                    if (i == position) {
                        tag_fl.setChecked(true, position);
                    } else {
                        tag_fl.setChecked(false, i);
                    }
                }
                tag_fl.notifyAllTag();

                doTag(text, position);
            }
        });
    }

    private void doTag(String text, int position){
        if("无线循环广告".equals(text)){
            Intent banner=new Intent(this, BannerActivity.class);
            startActivity(banner);
        }else if("画廊".equals(text)){
            Intent gallery=new Intent(this, GalleryActivity.class);
            startActivity(gallery);
        }
    }
}
