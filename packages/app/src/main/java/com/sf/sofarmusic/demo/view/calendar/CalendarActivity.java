package com.sf.sofarmusic.demo.view.calendar;

import com.sf.sofarmusic.R;
import com.sf.base.UIRootActivity;

/**
 * Created by sufan on 17/7/3.
 */

public class CalendarActivity extends UIRootActivity {

    private WeekView week;
    private CalendarView calendar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initTitle() {
        head_title.setText("日历");
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
