package com.sf.demo.picker.date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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

/**
 * Created by sufan on 17/6/20.
 * mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
 * mPickerWindow.setFocusable(true);
 * 以上两句保证了点击popwindow之外的地方，可以使得pop消失
 */

public class DatePicker implements PopupWindow.OnDismissListener, View.OnClickListener {

    private OnDateSelectListener mOnDateSelectListener;

    private PopupWindow mPickerWindow;

    private View mParent;
    private View mPickerView;

    private WheelRecyclerView mYearWheel;
    private WheelRecyclerView mMonthWheel;
    private WheelRecyclerView mDayWheel;

    private Activity mContext;

    private List<String> mYears;
    private List<String> mMonths;
    private List<String> mDays;
    private static int START_YEAR = 1990, END_YEAR = 2100;


    private String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    private String[] months_little = {"4", "6", "9", "11"};

    private final List<String> list_big = Arrays.asList(months_big);
    private final List<String> list_little = Arrays.asList(months_little);


    public DatePicker(Activity context, View parent) {
        mContext = context;
        mParent = parent;

        //初始化选择器
        mPickerView = LayoutInflater.from(context).inflate(R.layout.layout_date_picker, null);
        mYearWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_year);
        mMonthWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_month);
        mDayWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_day);
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

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);    //从0开始
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //年
        mYears = new ArrayList<>();
        for (int i = START_YEAR; i <= END_YEAR; i++) {
            mYears.add(i + "");
        }
        mYearWheel.setData(mYears);
        mYearWheel.setLabel("年");
        mYearWheel.setSelect(year - START_YEAR);

        //月
        mMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                mMonths.add("0" + i);
            } else {
                mMonths.add("" + i);
            }
        }
        mMonthWheel.setData(mMonths);
        mMonthWheel.setLabel("月");
        mMonthWheel.setSelect(month);


        //日，判断大小月以及是否时闰年
        mDays = new ArrayList<>();
        filledDay(year, month + 1);
        mDayWheel.setLabel("日");
        mDayWheel.setSelect(day - 1);


        //年的监听
        mYearWheel.setOnSelectListener(new WheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position, String data) {
                int year = START_YEAR + position;
                filledDay(year, mMonthWheel.getSelected() + 1);
            }
        });

        //月的监听
        mMonthWheel.setOnSelectListener(new WheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position, String data) {
                filledDay(mYearWheel.getSelected() + START_YEAR, position + 1);
            }
        });
    }


    //根据年月，填充日的数据
    private void filledDay(int year, int month) {
        mDays.clear();
        if (list_big.contains(String.valueOf(month))) {
            for (int i = 1; i <= 31; i++) {
                if (i < 10) {
                    mDays.add("0" + i);
                } else {
                    mDays.add("" + i);
                }
            }
        } else if (list_little.contains(String.valueOf(month))) {
            for (int i = 1; i <= 30; i++) {
                if (i < 10) {
                    mDays.add("0" + i);
                } else {
                    mDays.add("" + i);
                }
            }
        } else {
            //闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                for (int i = 1; i <= 29; i++) {
                    if (i < 10) {
                        mDays.add("0" + i);
                    } else {
                        mDays.add("" + i);
                    }
                }
            } else {
                for (int i = 1; i <= 28; i++) {
                    if (i < 10) {
                        mDays.add("0" + i);
                    } else {
                        mDays.add("" + i);
                    }
                }
            }
        }
        mDayWheel.setData(mDays);
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


    public DatePicker setOnDateSelectListener(OnDateSelectListener listener) {
        mOnDateSelectListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_tv) {
            mPickerWindow.dismiss();

        } else if (i == R.id.confirm_tv) {
            if (mOnDateSelectListener != null) {
                String year = mYears.get(mYearWheel.getSelected());
                String month = mMonths.get(mMonthWheel.getSelected());
                String day = mDays.get(mDayWheel.getSelected());
                mOnDateSelectListener.onDateSelect(year, month, day);
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

    public interface OnDateSelectListener {
        void onDateSelect(String year, String month, String day);
    }


}

