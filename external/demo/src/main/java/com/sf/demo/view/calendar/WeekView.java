package com.sf.demo.view.calendar;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sf.demo.R;

/**
 * Created by sufan on 17/7/3.
 */

public class WeekView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private View mView;

    private RecyclerView week_rv;
    private TextView front_week_tv, now_day_tv, next_week_tv;

    private String date;
    private List<DateEntity> mDateList;
    private WeekAdapter mAdapter;

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_calendar_week, null);

        week_rv = (RecyclerView) mView.findViewById(R.id.week_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 7);
        week_rv.setLayoutManager(gridLayoutManager);

        front_week_tv = (TextView) mView.findViewById(R.id.front_week_tv);
        now_day_tv = (TextView) mView.findViewById(R.id.now_day_tv);
        next_week_tv = (TextView) mView.findViewById(R.id.next_week_tv);

        addView(mView);

        initData();
        initEvent();
    }

    public void setDate(String dateString) {
        date = dateString;
        if (TextUtils.isEmpty(dateString)) {
            date = DateUtil.getCurrDate("yyyy-MM-dd");
        }
        mAdapter.updateDate(DateUtil.getWeek(date));
        now_day_tv.setText(date);
    }

    private void initData() {
        date = DateUtil.getCurrDate("yyyy-MM-dd");
        mDateList = DateUtil.getWeek(date);
        mAdapter = new WeekAdapter(mContext, mDateList);
        week_rv.setAdapter(mAdapter);
        now_day_tv.setText(date);

    }

    private void initEvent() {

        front_week_tv.setOnClickListener(this);
        next_week_tv.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new WeekAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int pos, DateEntity entity) {
                date = entity.date;
                now_day_tv.setText(date);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.front_week_tv) {
            date = DateUtil.getSomeDays(date, -7);
            mAdapter.updateDate(DateUtil.getWeek(date));
            now_day_tv.setText(date);

        } else if (i == R.id.next_week_tv) {
            date = DateUtil.getSomeDays(date, +7);
            mAdapter.updateDate(DateUtil.getWeek(date));
            now_day_tv.setText(date);

        }
    }
}
