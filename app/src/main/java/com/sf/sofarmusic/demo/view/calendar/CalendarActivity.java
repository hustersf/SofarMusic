package com.sf.sofarmusic.demo.view.calendar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.util.FontUtil;

/**
 * Created by sufan on 17/7/3.
 */

public class CalendarActivity extends DemoActivity {
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private WeekView week;
    private CalendarView calendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_calendar);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("日历");

        head_right.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        calendar=(CalendarView)findViewById(R.id.calendar);
     //   calendar.setDate("2018-07-06");

        week=(WeekView) findViewById(R.id.week);
      //  week.setDate("2017-07-10");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
