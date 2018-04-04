package com.sf.sofarmusic.demo.view.linechart;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.data.DemoData;
import com.sf.sofarmusic.util.FontUtil;

import java.util.List;

/**
 * Created by sufan on 17/7/7.
 */

public class IncomeExpendActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private TextView change_tv, record_tv;
    private MothBillView linechart;

    private List<IncomeExpendInfo> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_income_expend);
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

        head_title.setText("收入支出折线图");

        head_right.setVisibility(View.GONE);

    }

    @Override
    public void initView() {

        change_tv = (TextView) findViewById(R.id.change_tv);
        record_tv = (TextView) findViewById(R.id.record_tv);
        linechart = (MothBillView) findViewById(R.id.linechart);

    }

    @Override
    public void initData() {
        list = DemoData.getIncomeExpendList();
        linechart.setData(1, list);

    }

    @Override
    public void initEvent() {

        change_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("切换收入记录".equals(change_tv.getText().toString())) {
                    record_tv.setText("收入记录");
                    change_tv.setText("切换支出记录");
                    linechart.setData(0, list);
                } else {
                    record_tv.setText("支出记录");
                    change_tv.setText("切换收入记录");
                    linechart.setData(1, list);
                }
            }
        });

    }
}
