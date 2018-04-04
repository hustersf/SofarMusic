package com.sf.sofarmusic.demo.viewpager.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sufan on 17/6/24.
 * 1. 添加最后一条数据到第一条，添加第一条数据到最后一条；
 * 2. 设置监听器；
 * 3. 设置初始化时设置当前页面为第二页
 * <p>
 * 假设3张图片，则是     3 1 2 3 1
 */

public abstract class LoopVPAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener {

    protected Context mContext;
    private List<View> mViews;
    private ViewPager mViewPager;
    private BannerIndicator mIndicator;

    private int currentPosition;

    // 执行周期性或定时任务
    private ScheduledExecutorService mScheduledExecutorService;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mViewPager.setCurrentItem(currentPosition);
        }
    };


    public LoopVPAdapter(Context context, List<T> datas, ViewPager viewPager, BannerIndicator indicator) {
        mContext = context;

        mViews = new ArrayList<>();
        if (datas.size() > 1) {
            datas.add(0, datas.get(datas.size() - 1));
            datas.add(datas.get(1));
        }


        for (T data : datas) {
            mViews.add(getItemView(data));
        }

        mViewPager = viewPager;
        viewPager.setAdapter(this);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(1, false);

        mIndicator = indicator;
        mIndicator.setIndicator(0);

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        //若viewpager滑动未停止，直接返回
        if (state != ViewPager.SCROLL_STATE_IDLE) return;
        // 若当前为第一张，设置页面为倒数第二张
        if (currentPosition == 0) {
            mViewPager.setCurrentItem(mViews.size() - 2, false);

            mIndicator.setIndicator(mViews.size() - 2 - 1);
        } else if (currentPosition == mViews.size() - 1) {
            // 若当前为倒数第一张，设置页面为第二张
            mViewPager.setCurrentItem(1, false);

            mIndicator.setIndicator(0);
        } else {
            mIndicator.setIndicator(currentPosition - 1);
        }

    }

    //分页指示器
    public interface BannerIndicator {

        void InitIndicatorItems(int itemsNumber);

        void setIndicator(int position);
    }


    /**
     * 开启定时任务
     */
    public void startAutoRun() {
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        /**
         * 循环 创建并执行一个在给定初始延迟后首次启用的定期操作， 后续操作具有给定的周期；也就是将在 initialDelay 后开始执行，
         * 然后在initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行， 依此类推
         */
        mScheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(), 5, 5, TimeUnit.SECONDS);
    }

    /**
     * 关闭定时任务
     */
    public void stopAutoRun() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
        }
    }


    class ViewPagerTask implements Runnable {

        @Override
        public void run() {
            if (mViews != null) {
                currentPosition = (currentPosition + 1) % mViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

}
