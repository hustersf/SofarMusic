package com.sf.demo.list.timeline;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TimeLineActivity extends UIRootActivity {

  RecyclerView recyclerView;
  TimeLineAdApter adApter;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_time_line;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("时间轴列表");
  }

  @Override
  protected void initView() {
    recyclerView = findViewById(R.id.time_line_rv);

  }

  @Override
  protected void initData() {
    List<TimeItem> list = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      TimeItem item = new TimeItem();
      item.time = "时间节点：" + i;
      item.content = "内容节点：" + i;
      list.add(item);
    }


    adApter = new TimeLineAdApter();
    adApter.setList(list);
    recyclerView.setAdapter(adApter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.addItemDecoration(new TimeLineItemDecoration(this));
  }

  @Override
  protected void initEvent() {

  }
}
