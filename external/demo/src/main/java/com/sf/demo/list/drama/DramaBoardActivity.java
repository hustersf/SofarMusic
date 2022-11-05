package com.sf.demo.list.drama;

import android.graphics.Rect;
import android.view.View;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.utility.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

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
        new GridLayoutManager(this, spanCount, RecyclerView.HORIZONTAL, false);
    mDramaBoardRv.setLayoutManager(layoutManager);
    mDramaBoardAdapter = new DramaBoardAdapter();
    mDramaBoardRv.setAdapter(mDramaBoardAdapter);


    // 这里的10*2+ itemH !=30+itemH ，导致第二行的底部会多留出10dp，看起来就不像是上下间距10dp 而是上面10，下面20
    // 猜测是在HORIZONTAL的情况下，会让每一个item的高度保证一致；而在VERTICAL的情况下会保持宽度一致
    // 如果RecyclerView的高度是wrap，则高度变为会取maxItemH*spanCount
    // 如果固定了高度，则recyclerViewH/3 作为一个item的高度，item中的内容可能由于显示不下被裁减
    int betweenSpace = DensityUtil.dp2px(getApplicationContext(), 10);
    int sideSpace = DensityUtil.dp2px(getApplicationContext(), 30);
    RecyclerView.ItemDecoration boardItemDecoration = new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
          RecyclerView.State state) {
        final int totalCount = parent.getAdapter().getItemCount();
        final int childPosition = parent.getChildAdapterPosition(view);

        // 行间距
        if (childPosition % spanCount == 0) {
          // 第一行
          outRect.top = sideSpace;
          outRect.bottom = 0;
        } else if (childPosition % spanCount == spanCount - 1) {
          // 最后一行
          outRect.top = 0;
          outRect.bottom = sideSpace;
        } else {
          outRect.top = betweenSpace;
          outRect.bottom = betweenSpace;
        }


        // 列间距
        if (childPosition < spanCount) {
          // 第一列
          outRect.left = DensityUtil.dp2px(getApplicationContext(), 16);
        } else if (childPosition >= totalCount - spanCount) {
          // 最后一列
          outRect.right = DensityUtil.dp2px(getApplicationContext(), 30);
        }
      }
    };
    mDramaBoardRv.addItemDecoration(boardItemDecoration);

    DramaBoardSnapHelper snapHelper = new DramaBoardSnapHelper();
    snapHelper.attachToRecyclerView(mDramaBoardRv);
  }

  @Override
  protected void initData() {
    List<DramaInfo> dramaInfos = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
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
