package com.sf.base.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.sf.base.R;

public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除堆栈中的Activity
     */
    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        /**
         * 报错，ConcurrentModificationException
         * 原因，迭代器是依赖于集合而存在的，在判断成功后，集合的中新添加了元素，而迭代器却不知道，所以就报错了，这个错叫并发修改异常。
         * 解决办法， A:迭代器迭代元素，迭代器修改元素 B:集合遍历元素，集合修改元素(普通for)
         */
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }

    }

    /**
     * 结束指定类名之上的Activity
     */
    public void finishTopActivity(Class<?> cls) {
        List<Activity> activities = new ArrayList<>();
        boolean flag = false;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                flag = true;
            } else {
                if (flag) {
                    activities.add(activity);
                }
            }
        }

        for (Activity activity : activities) {
            finishActivity(activity);
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public static void startActivityWithAnimation(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_animation_in_from_right, R.anim.activity_animation_out_to_left);
    }

    public static void startActivityWithOutAnimation(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void finishActivityWithAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_animation_in_from_left, R.anim.activity_animation_out_to_right);
    }

    public static void finishActivityWithOutAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }
}
