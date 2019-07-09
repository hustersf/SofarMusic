package com.sf.demo.performance;

import android.content.Intent;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.performance.json.JsonTestActivity;
import com.sf.widget.flowlayout.FlowTagList;

public class PerformanceActivity extends UIRootActivity {

  private FlowTagList tag_fl;
  private String[] mTags = {"json解析"};

  @Override
  protected int getLayoutId() {
    return R.layout.activity_demo_show;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("性能测试");
  }

  @Override
  protected void initView() {
    tag_fl = findViewById(R.id.tag_fl);
    dynamicAddView(tag_fl, "tagColor", R.color.themeColor);
  }

  @Override
  protected void initData() {
    tag_fl.setTags(mTags);
  }

  @Override
  protected void initEvent() {
    tag_fl.setOnTagClickListener(new FlowTagList.OnTagClickListener() {
      @Override
      public void OnTagClick(String text, int position) {
        for (int i = 0; i < mTags.length; i++) {
          if (i == position) {
            tag_fl.setChecked(true, position);
          } else {
            tag_fl.setChecked(false, i);
          }
        }
        tag_fl.notifyAllTag();

        doTag(text, position);
      }
    });
  }

  private void doTag(String text, int position) {
    if (position == 0) {
      Intent intent = new Intent(this, JsonTestActivity.class);
      startActivity(intent);
    }
  }
}
