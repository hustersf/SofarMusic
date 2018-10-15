package com.sf.demo.viewpager.banner;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/24.
 * 1. 添加最后一条数据到第一条，添加第一条数据到最后一条；
 * 2. 设置监听器；
 * 3. 设置初始化时设置当前页面为第二页
 * 
 * 假设3张图片，则是 3 1 2 3 1
 */

public abstract class LoopVPAdapter<T> extends PagerAdapter
    implements
      ViewPager.OnPageChangeListener {

  protected Context mContext;
  private List<View> mViews;
  private ViewPager mViewPager;
  private BannerIndicator mIndicator;

  private int mCurrentPosition;
  private static final int AUTO_SCROLL_DURATION = 3000;

  public static final Handler mHandler = new Handler(Looper.getMainLooper());
  private int mScrollState = ViewPager.SCROLL_STATE_IDLE;


  public LoopVPAdapter(Context context, List<T> datas, ViewPager viewPager,
      BannerIndicator indicator) {
    mContext = context;

    mViews = new ArrayList<>();
    if (datas.size() > 1) {
      datas.add(0, datas.get(datas.size() - 1));
      datas.add(datas.get(1));
    }


    for (T data : datas) {
      mViews.add(getItemView(data));
    }

    mIndicator = indicator;
    mIndicator.setIndicator(0);

    mViewPager = viewPager;
    viewPager.setAdapter(this);
    viewPager.addOnPageChangeListener(this);
    viewPager.setCurrentItem(1, false);

    sendMessage();
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
    if (mCurrentPosition == 0) {
      mIndicator.setIndicator(mViews.size() - 2 - 1);
    } else if (mCurrentPosition == mViews.size() - 1) {
      // 若当前为倒数第一张，设置页面为第二张
      mIndicator.setIndicator(0);
    } else {
      mIndicator.setIndicator(mCurrentPosition - 1);
    }
  }

  // 分页指示器
  public interface BannerIndicator {

    void InitIndicatorItems(int itemsNumber);

    void setIndicator(int position);
  }

  private void sendMessage() {
    mHandler.removeCallbacks(mScrollRunnable);
    mHandler.postDelayed(mScrollRunnable, AUTO_SCROLL_DURATION);
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


}
