package com.sf.demo.list.expand;

import java.util.List;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.data.DemoData;

/**
 * Created by sufan on 17/6/27.
 */

public class ExpandListActivity extends UIRootActivity {

  private RecyclerView expand_rv;
  private ExpandListAdapter mAdapter;
  private List<MenuParent> mDatas;


  @Override
  protected int getLayoutId() {
    return R.layout.activity_expand_list;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("折叠列表");
  }

  @Override
  public void initView() {
    expand_rv = (RecyclerView) findViewById(R.id.expand_rv);
    expand_rv.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  public void initData() {
    mDatas = DemoData.getExpandList();
    mAdapter = new ExpandListAdapter(this, mDatas);
    expand_rv.setAdapter(mAdapter);
  }

  @Override
  public void initEvent() {

  }
}
