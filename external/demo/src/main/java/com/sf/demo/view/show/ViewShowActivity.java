package com.sf.demo.view.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.view.calendar.CalendarActivity;
import com.sf.demo.view.chainheadview.ChainHeadViewActivity;
import com.sf.demo.view.danmu.DanmuActivity;
import com.sf.demo.view.highlight.HighLightActivity;
import com.sf.demo.view.linechart.IncomeExpendActivity;
import com.sf.demo.view.numtextview.NumberTextActivity;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/6/26.
 */

public class ViewShowActivity extends UIRootActivity {

  private FlowTagList tag_fl;

  private int i = 0;

  private String[] mTags = {"圆盘菜单", "幸运转盘", "手势密码", "日历", "收入支出折线图", "NumberTextView",
      "高亮引导图", "链式头像", "弹幕控件"};

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    setContentView(R.layout.activity_demo_show);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_demo_show;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("自定义View集合");
  }

  @Override
  public void initView() {
    tag_fl = (FlowTagList) findViewById(R.id.tag_fl);
    dynamicAddView(tag_fl, "tagColor", R.color.themeColor);
  }

  @Override
  public void initData() {
    tag_fl.setTags(mTags);
  }

  @Override
  public void initEvent() {
    tag_fl.setOnTagClickListener(new FlowTagList.OnTagClickListener() {
      @Override
      public void OnTagClick(String text, int position) {
        doTag(text, position);
      }
    });
  }

  private void doTag(String text, int position) {
    if ("圆盘菜单".equals(text)) {
      Intent circle = new Intent(this, CircleMenuActivity.class);
      startActivity(circle);
    } else if ("幸运转盘".equals(text)) {
      Intent lucky = new Intent(this, LuckyPanActivity.class);
      startActivity(lucky);
    } else if ("手势密码".equals(text)) {
      Intent lock = new Intent(this, LockPatternActivity.class);
      startActivity(lock);
    } else if ("日历".equals(text)) {
      Intent calendar = new Intent(this, CalendarActivity.class);
      startActivity(calendar);
    } else if ("收入支出折线图".equals(text)) {
      Intent linechart = new Intent(this, IncomeExpendActivity.class);
      startActivity(linechart);
    } else if ("NumberTextView".equals(text)) {
      Intent number = new Intent(this, NumberTextActivity.class);
      startActivity(number);
    } else if ("高亮引导图".equals(text)) {
      Intent highLight = new Intent(this, HighLightActivity.class);
      startActivity(highLight);
    } else if ("链式头像".equals(text)) {
      Intent chain = new Intent(this, ChainHeadViewActivity.class);
      startActivity(chain);
    } else if ("弹幕控件".equals(text)) {
      Intent danmu = new Intent(this, DanmuActivity.class);
      startActivity(danmu);
    }
  }
}
