package com.sf.sofarmusic.menu.profile;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.BaseActivity;
import com.sf.sofarmusic.util.DensityUtil;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends BaseActivity {
  private Toolbar toolbar;
  private TextView back_tv, title_tv, serach_tv, menu_tv;
  private RecyclerView rv_profile;

  private AppBarLayout appBarLayout;
  private TabLayout tl_profile, tl_profile_top;

  private LinearLayout ll_head;
  private ImageView iv_background;

  private LinearLayout layout_tab;
  private LinearLayout layout_tab_top;
  private int mCurrentTabPosition;
  // private

  // 滑动事件相关参数
  private int mTitleHeight;
  private int mHeadHeight;
  private int mZoomViewHeight;
  private int mColor = 0x00000000;
  private PullToZoomCoordinatorLayout ptzc_profile;

  private RelativeLayout rl_expect;
  private ImageView iv_expect;


  private ProfileAdapter mAdapter;

  String str;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);


    initView();
    initData();
    initEvent();
  }

  private void initView() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    back_tv = (TextView) findViewById(R.id.back_tv);
    title_tv = (TextView) findViewById(R.id.title_tv);
    serach_tv = (TextView) findViewById(R.id.search_tv);
    menu_tv = (TextView) findViewById(R.id.menu_tv);
    Typeface iconfont = FontUtil.setFont(baseAt);
    back_tv.setTypeface(iconfont);
    serach_tv.setTypeface(iconfont);
    menu_tv.setTypeface(iconfont);

    ptzc_profile = findViewById(R.id.ptzc_profile);

    ll_head = findViewById(R.id.ll_head);
    appBarLayout = findViewById(R.id.app_bar);
    iv_background = findViewById(R.id.iv_background);

    layout_tab = findViewById(R.id.layout_tab);
    tl_profile = layout_tab.findViewById(R.id.tl_profile);
    tl_profile.addTab(tl_profile.newTab().setText("作品"));
    tl_profile.addTab(tl_profile.newTab().setText("喜欢"));
    tl_profile.addTab(tl_profile.newTab().setText(("关注")));
    layout_tab_top = findViewById(R.id.layout_tab_top);
    tl_profile_top = layout_tab_top.findViewById(R.id.tl_profile);
    tl_profile_top.addTab(tl_profile_top.newTab().setText("作品"));
    tl_profile_top.addTab(tl_profile_top.newTab().setText("喜欢"));
    tl_profile_top.addTab(tl_profile_top.newTab().setText(("关注")));

    rv_profile = findViewById(R.id.rv_profile);
    rv_profile.setLayoutManager((new GridLayoutManager(this, 3,
        GridLayoutManager.VERTICAL, false)));

    rl_expect = findViewById(R.id.rl_expect);
    iv_expect = findViewById(R.id.iv_expect);

  }

  private void initData() {
    List<Photo> photos = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      Photo photo = new Photo();
      photo.photoId = String.valueOf(i);
      photos.add(photo);
    }
    int dividerWidth = DensityUtil.dp2px(this, 2);
    mAdapter = new ProfileAdapter(this, photos);
    rv_profile.setAdapter(mAdapter);
    // 增加分割线
    rv_profile.addItemDecoration(new PhotoItemDecoration(this, dividerWidth));

  }

  private void initEvent() {

    rl_expect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rl_expect.setSelected(!rl_expect.isSelected());
      }
    });


    PubTabSelectedListener listener = new PubTabSelectedListener();
    tl_profile.addOnTabSelectedListener(listener);
    tl_profile_top.addOnTabSelectedListener(listener);

    // 获取相关控件高度，来处理后续滑动事件
    toolbar.post(new Runnable() {
      @Override
      public void run() {
        mTitleHeight = toolbar.getHeight();
      }
    });



    ll_head.post(new Runnable() {
      @Override
      public void run() {
        mHeadHeight = ll_head.getHeight();
      }
    });

    iv_background.post(new Runnable() {
      @Override
      public void run() {
        mZoomViewHeight = iv_background.getHeight();
      }
    });

    // 获取背景图主色
    final Bitmap bitmap = ((BitmapDrawable) iv_background.getBackground()).getBitmap();
    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
      @Override
      public void onGenerated(@NonNull Palette palette) {
        int defaultColor = getResources().getColor(R.color.transparent);
        int dominantColor = palette.getDominantColor(defaultColor);
        mColor = dominantColor;
        // bitmap.recycle();
      }
    });


    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d("appbar", verticalOffset + "");
        if (mHeadHeight == 0 || mTitleHeight == 0 || mZoomViewHeight == 0) {
          return;
        }

        // 标题栏渐变
        int y = -verticalOffset;
        if (y > mZoomViewHeight - mTitleHeight) {
          y = mZoomViewHeight - mTitleHeight;
        }
        int alpha = (int) ((y * 1.0f) / (mZoomViewHeight - mTitleHeight) * 255);
        // 改变标题栏
        if (alpha > 125) {
          title_tv.setVisibility(View.VISIBLE);
        } else {
          title_tv.setVisibility(View.GONE);
        }
        toolbar.setBackgroundColor(mColor);
        toolbar.getBackground().setAlpha(alpha);


        if (-verticalOffset > mHeadHeight - mTitleHeight) {
          showTopTab();
        } else {
          hideTopTab();
        }
      }
    });

  }

  private void showTopTab() {
    layout_tab_top.setVisibility(View.VISIBLE);

  }

  private void hideTopTab() {
    layout_tab_top.setVisibility(View.GONE);
  }

  private class PubTabSelectedListener implements TabLayout.OnTabSelectedListener {

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
      mCurrentTabPosition = tab.getPosition();
      tl_profile_top.getTabAt(mCurrentTabPosition).select();
      tl_profile.getTabAt(mCurrentTabPosition).select();
      ToastUtil.startShort(baseAt, tab.getText().toString());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
  }
}
