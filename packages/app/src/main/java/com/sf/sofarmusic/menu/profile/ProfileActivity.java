package com.sf.sofarmusic.menu.profile;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.base.BaseActivity;
import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.ToastUtil;
import com.sf.utility.ViewUtil;
import com.sf.widget.progress.RingProgress;
import com.sf.widget.recyclerview.itemdecoration.GridDividerItemDecoration;
import com.sf.widget.refresh.CommonRefreshLayout;
import com.sf.widget.refresh.RefreshLayout;


public class ProfileActivity extends BaseActivity implements IPullZoom {

  private static final String TAG = "ProfileActivity";

  private Toolbar toolbar;
  private TextView back_tv, title_tv, serach_tv, menu_tv;
  private RecyclerView rv_profile;

  private AppBarLayout appBarLayout;
  private TabLayout tl_profile;

  private LinearLayout ll_head;
  private ImageView iv_background;

  private LinearLayout layout_tab;

  private RingProgress pb_task;

  // 滑动事件相关参数
  private int mTitleHeight;
  private int mHeadHeight;
  private int mZoomViewHeight;
  private int mColor = 0x00000000;

  private ProfileAdapter mAdapter;

  private PullToZoomCoordinatorLayout mCoordinatorLayout;
  private int mHeaderOffSetSize;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    initView();
    initData();
    initEvent();
    addWindowView();
  }

  private void initView() {
    // 标题布局
    toolbar = findViewById(R.id.toolbar);
    back_tv = findViewById(R.id.back_tv);
    title_tv = findViewById(R.id.title_tv);
    serach_tv = findViewById(R.id.search_tv);
    menu_tv = findViewById(R.id.menu_tv);
    Typeface iconfont = FontUtil.setFont(baseAt);
    back_tv.setTypeface(iconfont);
    serach_tv.setTypeface(iconfont);
    menu_tv.setTypeface(iconfont);
    back_tv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    // 头部布局
    ll_head = findViewById(R.id.ll_head);
    appBarLayout = findViewById(R.id.app_bar);
    iv_background = findViewById(R.id.iv_background);
    RelativeLayout.LayoutParams bgLp =
        (RelativeLayout.LayoutParams) iv_background.getLayoutParams();
    // 图片分辨率为960*640
    bgLp.height = ((int) (1.0f * 640 / 960 * DeviceUtil.getMetricsWidth(this)));
    // 拉伸效果
    mCoordinatorLayout = findViewById(R.id.pull_coordinator);
    mCoordinatorLayout.setPullZoom(iv_background, bgLp.height, (int) (1.5f * bgLp.height), this);


    // tab布局
    layout_tab = findViewById(R.id.layout_tab);
    tl_profile = layout_tab.findViewById(R.id.tl_profile);
    tl_profile.addTab(tl_profile.newTab().setText("作品"));
    tl_profile.addTab(tl_profile.newTab().setText("喜欢"));
    tl_profile.addTab(tl_profile.newTab().setText(("关注")));
    // tab不会滑出屏幕，但会被标题栏盖住，这样保证tab不会被盖住
    LinearLayout.LayoutParams tabLp = (LinearLayout.LayoutParams) layout_tab.getLayoutParams();
    tabLp.height += getResources().getDimensionPixelOffset(R.dimen.tool_bar_height);
    LinearLayout.LayoutParams headLp = (LinearLayout.LayoutParams) ll_head.getLayoutParams();
    headLp.bottomMargin -= getResources().getDimensionPixelOffset(R.dimen.tool_bar_height);

    // 列表布局
    rv_profile = findViewById(R.id.rv_profile);
    rv_profile.setLayoutManager((new GridLayoutManager(this, 3,
        GridLayoutManager.VERTICAL, false)));
    // 增加分割线
    int dividerWidth = DensityUtil.dp2px(this, 2);
    int dividerColor = getResources().getColor(R.color.themeColor);
    rv_profile.addItemDecoration(new GridDividerItemDecoration(dividerWidth, dividerColor));
  }

  private void initData() {
    List<Photo> photos = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      Photo photo = new Photo();
      photo.photoId = String.valueOf(i);
      photos.add(photo);
    }
    mAdapter = new ProfileAdapter(this, photos);
    rv_profile.setAdapter(mAdapter);

  }

  private void initEvent() {

    PubTabSelectedListener listener = new PubTabSelectedListener();
    tl_profile.addOnTabSelectedListener(listener);

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
    final Bitmap bitmap = ((BitmapDrawable) iv_background.getDrawable()).getBitmap();
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
        mHeaderOffSetSize = verticalOffset;
        if (mHeadHeight == 0 || mTitleHeight == 0 || mZoomViewHeight == 0) {
          return;
        }

        // 标题 栏渐变
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

        // 更新进度
        if (pb_task != null) {
          pb_task.setProgress((int) (1.0f * alpha / 255 * pb_task.getMax()));
        }
      }
    });

  }

  /**
   * 测试WindowManage之addView
   */
  private void addWindowView() {
    WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.width = DensityUtil.dp2px(this, 100);
    lp.height = DensityUtil.dp2px(this, 100);
    lp.gravity = Gravity.RIGHT | Gravity.TOP;
    lp.x = DensityUtil.dp2px(this, 20);
    lp.y = DensityUtil.dp2px(this, 50);

    lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    lp.format = PixelFormat.RGBA_8888; // 去掉window黑色背景

    View view = ViewUtil.inflate(this, R.layout.layout_float_task_progress);
    pb_task = view.findViewById(R.id.pb_task);
    manager.addView(view, lp);
  }

  @Override
  public boolean isReadyForPullStart() {
    return mHeaderOffSetSize == 0;
  }

  @Override
  public void onPullZooming(int newScrollValue) {

  }

  @Override
  public void onPullZoomEnd() {

  }

  private class PubTabSelectedListener implements TabLayout.OnTabSelectedListener {

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
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
