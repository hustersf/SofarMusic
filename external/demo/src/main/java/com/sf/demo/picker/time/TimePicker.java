package com.sf.demo.picker.time;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.sf.demo.R;
import com.sf.demo.picker.city.WheelRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sufan on 17/6/20.
 * mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
 * mPickerWindow.setFocusable(true);
 * 以上两句保证了点击popwindow之外的地方，可以使得pop消失
 */

public class TimePicker implements PopupWindow.OnDismissListener, View.OnClickListener {

    private OnTimeSelectListener mOnTimeSelectListener;

    private PopupWindow mPickerWindow;

    private View mParent;
    private View mPickerView;

    private WheelRecyclerView mHourWheel;
    private WheelRecyclerView mMinWheel;

    private Activity mContext;

    private List<String> mHours;
    private List<String> mMins;

    public TimePicker(Activity context, View parent) {
        mContext = context;
        mParent = parent;

        //初始化选择器
        mPickerView = LayoutInflater.from(context).inflate(R.layout.layout_time_picker, null);
        mHourWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_hour);
        mMinWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_min);
        mPickerView.findViewById(R.id.cancel_tv).setOnClickListener(this);
        mPickerView.findViewById(R.id.confirm_tv).setOnClickListener(this);

        mPickerWindow = new PopupWindow(mPickerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPickerWindow.setFocusable(true);
        mPickerWindow.setAnimationStyle(R.style.CityPickerAnim);
        mPickerWindow.setOnDismissListener(this);

        initData();
    }

    private void initData() {

        mHours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                mHours.add("0" + i);
            } else {
                mHours.add("" + i);
            }
        }

        mMins = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                mMins.add("0" + i);
            } else {
                mMins.add("" + i);
            }
        }

        mHourWheel.setData(mHours);
        mMinWheel.setData(mMins);
        mHourWheel.setLabel("时");
        mMinWheel.setLabel("分");


//        yyyy-MM-dd HH:mm:ss"     HH代表24小时制，hh是12小时制
        SimpleDateFormat hourDF = new SimpleDateFormat("HH");
        SimpleDateFormat minDF = new SimpleDateFormat("mm");
        String hour = hourDF.format(new Date(System.currentTimeMillis()));
        String min = minDF.format(new Date(System.currentTimeMillis()));

        mHourWheel.setSelect(mHours.indexOf(hour));
        mMinWheel.setSelect(mMins.indexOf(min));


    }

    /**
     * 弹出Window时使背景变暗
     *
     * @param alpha
     */
    private void backgroundAlpha(float alpha) {
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }


    public TimePicker setOnTimeSelectListener(OnTimeSelectListener listener) {
        mOnTimeSelectListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_tv) {
            mPickerWindow.dismiss();

        } else if (i == R.id.confirm_tv) {
            if (mOnTimeSelectListener != null) {
                String hour = mHours.get(mHourWheel.getSelected());
                String min = mMins.get(mMinWheel.getSelected());
                mOnTimeSelectListener.onTimeSelect(hour, min);
                mPickerWindow.dismiss();
            }

        }
    }

    public void show() {
        backgroundAlpha(0.5f);
        if (mParent != null) {
            mPickerWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        } else {
            mPickerWindow.showAtLocation(mPickerView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }

    public interface OnTimeSelectListener {
        void onTimeSelect(String hour, String min);
    }
}
