package com.sf.demo.list;

import android.content.Intent;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.list.drag.MenuEditActivity;
import com.sf.demo.list.expand.ExpandListActivity;
import com.sf.demo.list.page.PageGridActivity;
import com.sf.demo.list.refresh.LoadMoreActivity;
import com.sf.demo.list.slide.MessageListActivity;
import com.sf.demo.list.sort.BankListActivity;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/6/26.
 */

public class ListIndexActivity extends UIRootActivity {


  private FlowTagList tag_fl;

  private String[] mTags = {"排序查询列表", "滑动删除列表", "拖拽排序列表", "分页菜单", "折叠列表", "上拉加载更多"};



  @Override
  protected int getLayoutId() {
    return R.layout.activity_demo_show;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("列表样式集合");
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
    if ("排序查询列表".equals(text)) {
      Intent sort = new Intent(this, BankListActivity.class);
      startActivity(sort);
    } else if ("滑动删除列表".equals(text)) {
      Intent slide = new Intent(this, MessageListActivity.class);
      startActivity(slide);
    } else if ("拖拽排序列表".equals(text)) {
      Intent drag = new Intent(this, MenuEditActivity.class);
      startActivity(drag);
    } else if ("分页菜单".equals(text)) {
      Intent page = new Intent(this, PageGridActivity.class);
      startActivity(page);
    } else if ("折叠列表".equals(text)) {
      Intent expand = new Intent(this, ExpandListActivity.class);
      startActivity(expand);
    } else if ("上拉加载更多".equals(text)) {
      Intent loadMore = new Intent(this, LoadMoreActivity.class);
      startActivity(loadMore);
    }
  }
}
