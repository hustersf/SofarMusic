package com.sf.demo.view.calendar;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.utility.ToastUtil;


/**
 * Created by sufan on 17/7/3.
 */

public class CalendarView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private View mView;

    private RecyclerView calendar_rv;
    private CalendarAdapter mAdapter;

    private String date;
    private GestureDetector gestureDetector;
    private final int RIGHT = 0;
    private final int LEFT = 1;
    private List<DateEntity> mDateList;

    private TextView front_month_tv, now_month_tv, next_month_tv, ok_tv, back_tv;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_calendar_month, null);

        calendar_rv = (RecyclerView) mView.findViewById(R.id.calendar_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 7);
        calendar_rv.setLayoutManager(gridLayoutManager);

        front_month_tv = (TextView) mView.findViewById(R.id.front_month_tv);
        now_month_tv = (TextView) mView.findViewById(R.id.now_month_tv);
        next_month_tv = (TextView) mView.findViewById(R.id.next_month_tv);
        ok_tv = (TextView) mView.findViewById(R.id.ok_tv);
        back_tv = (TextView) mView.findViewById(R.id.back_tv);

        addView(mView);
        initData();
        initEvent();
    }

    public void setDate(String dateString) {
        date = dateString;
        if (TextUtils.isEmpty(dateString)) {
            date = DateUtil.getCurrDate("yyyy-MM-dd");
        }
        mAdapter.updateDate(DateUtil.getMonth(date));
        now_month_tv.setText("当前月份：" + DateUtil.formatDate(date, "yyyy-MM"));
    }


    private void initData() {
        date = DateUtil.getCurrDate("yyyy-MM-dd");
        mDateList = DateUtil.getMonth(date);
        mAdapter = new CalendarAdapter(mContext, mDateList);
        calendar_rv.setAdapter(mAdapter);

        now_month_tv.setText("当前月份：" + DateUtil.formatDate(date, "yyyy-MM"));

    }

    private void initEvent() {
        gestureDetector = new GestureDetector(mContext, onGestureListener);
        calendar_rv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        front_month_tv.setOnClickListener(this);
        next_month_tv.setOnClickListener(this);
        ok_tv.setOnClickListener(this);
        back_tv.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos, DateEntity entity) {
                date = entity.date;
                now_month_tv.setText("当前月份：" + DateUtil.formatDate(date, "yyyy-MM"));
            }
        });
    }

    /**
     * 手势监听是否是左右滑动
     */
    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    if (e1 == null || e2 == null) {
                        return true;
                    }

                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if(Math.abs(y)> Math.abs(x)){
                        return true;
                    }

                    if (x > 100) {
                        doResult(RIGHT);
                    } else if (x < -100) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                date = DateUtil.getSomeMonthDay(date, -1);
                mAdapter.updateDate(DateUtil.getMonth(date));
                now_month_tv.setText("当前月份：" + DateUtil.formatDate(date, "yyyy-MM"));
                break;
            case LEFT:
                date = DateUtil.getSomeMonthDay(date, +1);
                mAdapter.updateDate(DateUtil.getMonth(date));
                now_month_tv.setText("当前月份：" + DateUtil.formatDate(date, "yyyy-MM"));
                break;

        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.front_month_tv) {
            doResult(RIGHT);

        } else if (i == R.id.next_month_tv) {
            doResult(LEFT);

        } else if (i == R.id.ok_tv) {
            ToastUtil.startShort(mContext, date);

        } else if (i == R.id.back_tv) {
            date = DateUtil.getCurrDate("yyyy-MM-dd");
            mAdapter.updateDate(DateUtil.getMonth(date));
            now_month_tv.setText("当前月份：" + DateUtil.formatDate(date, "yyyy-MM"));

        }
    }
}

