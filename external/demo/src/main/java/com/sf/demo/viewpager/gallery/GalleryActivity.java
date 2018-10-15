package com.sf.demo.viewpager.gallery;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.viewpager.transformer.ZoomOutPageTransformer;
import com.sf.utility.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/27.
 */

public class GalleryActivity extends UIRootActivity {

    private TextView skin_tv;
    private RelativeLayout skin_rl;

    private ViewPager gallery_vp;
    private GalleryAdapter mAdapter;
    private List<GalleryInfo> mDatas;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initTitle() {
        head_title.setText("画廊");
    }

    @Override
    public void initView() {
        skin_tv = (TextView) findViewById(R.id.skin_tv);
        skin_rl = (RelativeLayout) findViewById(R.id.skin_rl);
        gallery_vp = (ViewPager) findViewById(R.id.gallery_vp);
    }

    @Override
    public void initData() {
        int[] imgId = {R.drawable.skin_huoyin, R.drawable.skin_yinyang_master,
                R.drawable.skin_blue_rectangle, R.drawable.skin_green_rectangle,
                R.drawable.skin_pink_rectangle, R.drawable.multi_color};
        String[] name = {"火影忍者", "阴阳师", "纯色主题-蓝", "纯色主题-绿", "纯色主题-粉", "自选颜色"};
        mDatas = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            GalleryInfo item = new GalleryInfo();
            item.name = name[i];
            item.imgId = imgId[i];
            mDatas.add(item);
        }

        mAdapter = new GalleryAdapter(this, mDatas);
        gallery_vp.setAdapter(mAdapter);
        gallery_vp.setOffscreenPageLimit(mAdapter.getCount());
        gallery_vp.setPageMargin(DensityUtil.dp2px(this, 20));
        gallery_vp.setPageTransformer(true, new ZoomOutPageTransformer());

        skin_tv.setText(name[0]);
    }

    @Override
    public void initEvent() {

        gallery_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                skin_tv.setText(mDatas.get(position).name);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //将事件传递给viewpager,否则点击边缘不滑动
        skin_rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gallery_vp.dispatchTouchEvent(event);
            }
        });
    }
}
