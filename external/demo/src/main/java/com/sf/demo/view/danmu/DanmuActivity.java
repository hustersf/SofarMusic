package com.sf.demo.view.danmu;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.danmu.DanmuView;
import com.sf.widget.danmu.IDanmuItem;
import com.sf.widget.danmu.TextDanmuItem;

import java.util.ArrayList;
import java.util.List;

public class DanmuActivity extends UIRootActivity {

  private DanmuView danmuView;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_danmu;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("弹幕控件");
  }

  @Override
  protected void initView() {
    danmuView = findViewById(R.id.danmu_view);
  }

  @Override
  protected void initData() {
    List<IDanmuItem> danmuItems = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      TextDanmuItem item = TextDanmuItem.buildItem(this, "张疯**子刚刚开奖 净赚2000金币:"+i);
      item.setLineWidth(1);
      item.setCorner(16);
      danmuItems.add(item);
    }
    danmuView.addItemList(danmuItems);
    danmuView.show();
  }

  @Override
  protected void initEvent() {

  }
}
