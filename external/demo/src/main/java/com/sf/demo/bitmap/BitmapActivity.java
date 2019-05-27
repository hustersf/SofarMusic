package com.sf.demo.bitmap;

import android.content.Intent;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.bitmap.combine.CombineActivity;
import com.sf.widget.flowlayout.FlowTagList;

public class BitmapActivity extends UIRootActivity {

  private FlowTagList tag_fl;
  private String[] mTags = {"仿微信群头像"};

  @Override
  protected int getLayoutId() {
    return R.layout.activity_demo_show;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("Bitmap");
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
      Intent intent = new Intent(this, CombineActivity.class);
      startActivity(intent);
    }
  }
}
