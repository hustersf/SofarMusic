package com.sf.demo.list.drama;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;

import java.util.ArrayList;
import java.util.List;

public class DramaBoardActivity extends UIRootActivity {

  private RecyclerView mDramaBoardRv;
  private DramaBoardAdapter mDramaBoardAdapter;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_drama_board;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("榜单列表");
  }

  @Override
  protected void initView() {
    int spanCount = 3;
    mDramaBoardRv = findViewById(R.id.rv_drama_board);
    GridLayoutManager layoutManager =
        new GridLayoutManager(this, spanCount, OrientationHelper.HORIZONTAL, false);
    mDramaBoardRv.setLayoutManager(layoutManager);
    mDramaBoardAdapter = new DramaBoardAdapter();
    mDramaBoardRv.setAdapter(mDramaBoardAdapter);
  }

  @Override
  protected void initData() {
    List<DramaInfo> dramaInfos = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      DramaInfo dramaInfo = new DramaInfo();
      dramaInfos.add(dramaInfo);
    }
    mDramaBoardAdapter.setList(dramaInfos);
    mDramaBoardAdapter.notifyDataSetChanged();
  }

  @Override
  protected void initEvent() {

  }
}
