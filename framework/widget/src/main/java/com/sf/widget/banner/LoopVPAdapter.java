package com.sf.widget.banner;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sf.utility.CollectionUtil;

/**
 * 无限循环滚动的抽象类
 * 重写 {@link #getItemView(T)},实现UI样式即可
 * 
 * 1. 添加最后一条数据到第一条，添加第一条数据到最后一条；
 * 2. 设置监听器；
 * 3. 设置初始化时设置当前页面为第二页
 * 
 * 假设3张图片，则是 3 1 2 3 1
 */
public abstract class LoopVPAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

  protected Context mContext;
  private List<View> mViews;
  private List<T> mModels;
  private List<T> mOriginList;
  private ViewPager mViewPager;
  private BannerIndicator mIndicator;

  private int mCurrentPosition;
  private static final int AUTO_SCROLL_DURATION = 3000;

  private long mScrollInterval = AUTO_SCROLL_DURATION;
  private boolean mAutoScroll = true;
  private OnPageSelectListener mOnPageSelectListener;

  public static final Handler mHandler = new Handler(Looper.getMainLooper());
  private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

  public LoopVPAdapter(Context context) {
    this(context, new ArrayList<>());
  }


  public LoopVPAdapter(Context context, List<T> list) {
    mViews = new ArrayList<>();
    mOriginList = list;
    mContext = context;
  }

  /**
   * 与ViewPager和page指示器绑定
   */
  public void bindHost(ViewPager viewPager, BannerIndicator indicator) {
    mIndicator = indicator;
    mViewPager = viewPager;
    mViewPager.setAdapter(this);
    mViewPager.addOnPageChangeListener(this);
    update(mOriginList);
    if (mAutoScroll) {
      sendMessage();
    }
  }

  /**
   * 更新数据源
   */
  public void update(List<T> list) {
    if (CollectionUtil.isEmpty(list)) {
      return;
    }

    if (mModels == null) {
      mModels = new ArrayList<>();
    }
    mModels.clear();
    mModels.addAll(list);

    if (mViews == null) {
      mViews = new ArrayList<>();
    }
    mViews.clear();
    if (mModels.size() > 1) {
      mModels.add(0, mModels.get(mModels.size() - 1));
      mModels.add(mModels.get(1));
    }

    for (T data : mModels) {
      mViews.add(getItemView(data));
    }
    notifyDataSetChanged();
    mIndicator.initIndicatorItems(list.size());
    mIndicator.setIndicator(0);
    mViewPager.setCurrentItem(1, false);
  }


  @Override
  public int getCount() {
    return mViews.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    container.addView(mViews.get(position));
    return mViews.get(position);
  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView(mViews.get(position));
  }

  public abstract View getItemView(T data);


  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

  @Override
  public void onPageSelected(int position) {
    mCurrentPosition = position;
    setIndicator();
  }


  @Override
  public void onPageScrollStateChanged(int state) {
    mScrollState = state;
    sendMessage();
    setPage(state);
  }

  private void setPage(int state) {
    // 若viewpager滑动未停止，直接返回
    if (state != ViewPager.SCROLL_STATE_IDLE) return;
    // 若当前为第一张，设置页面为倒数第二张
    if (mCurrentPosition == 0) {
      mViewPager.setCurrentItem(mViews.size() - 2, false);
    } else if (mCurrentPosition == mViews.size() - 1) {
      // 若当前为倒数第一张，设置页面为第二张
      mViewPager.setCurrentItem(1, false);
    }
  }

  private void setIndicator() {
    // 若当前为第一张，设置页面为倒数第二张
    int index;
    if (mCurrentPosition == 0) {
      index = mViews.size() - 2 - 1;
    } else if (mCurrentPosition == mViews.size() - 1) {
      // 若当前为倒数第一张，设置页面为第二张
      index = 0;
    } else {
      index = mCurrentPosition - 1;
    }
    mIndicator.setIndicator(index);
    if (mOnPageSelectListener != null) {
      mOnPageSelectListener.onPageSelect(index);
    }
  }

  // 分页指示器
  public interface BannerIndicator {

    void initIndicatorItems(int itemsNumber);

    void setIndicator(int position);
  }

  private void sendMessage() {
    mHandler.removeCallbacks(mScrollRunnable);
    mHandler.postDelayed(mScrollRunnable, mScrollInterval);
  }

  private Runnable mScrollRunnable = new Runnable() {

    @Override
    public void run() {
      if (mContext instanceof Activity) {
        Activity activity = (Activity) mContext;
        if (activity == null || activity.isFinishing()) {
          mHandler.removeCallbacks(mScrollRunnable);
          return;
        }
      }

      if (mViews != null && mScrollState == ViewPager.SCROLL_STATE_IDLE) {
        mCurrentPosition = (mCurrentPosition + 1) % mViews.size();
        mViewPager.setCurrentItem(mCurrentPosition);
        sendMessage();
      }
    }
  };

  /**
   * 开启滚动
   */
  public void startScroll() {
    sendMessage();
  }

  /**
   * 停止滚动
   */
  public void stopScroll() {
    mHandler.removeCallbacks(mScrollRunnable);
  }

  /**
   * 设置滚动的间隔
   */
  public void setAutoScrollDuration(long duration) {
    mScrollInterval = duration;
  }

  /**
   * 是否自动开启滚动
   */
  public void setAutoScroll(boolean autoScroll) {
    mAutoScroll = autoScroll;
  }

  public void setOnPageSelectListener(OnPageSelectListener listener) {
    mOnPageSelectListener = listener;
  }

  public interface OnPageSelectListener {
    void onPageSelect(int position);
  }

}
