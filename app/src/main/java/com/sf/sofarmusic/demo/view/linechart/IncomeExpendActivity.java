package com.sf.sofarmusic.demo.view.linechart;

import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.UIRootActivity;
import com.sf.sofarmusic.demo.data.DemoData;

import java.util.List;

/**
 * Created by sufan on 17/7/7.
 */

public class IncomeExpendActivity extends UIRootActivity {

    private TextView change_tv, record_tv;
    private MothBillView linechart;

    private List<IncomeExpendInfo> list;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_income_expend;
    }

    @Override
    protected void initTitle() {
        head_title.setText("收入支出折线图");
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
