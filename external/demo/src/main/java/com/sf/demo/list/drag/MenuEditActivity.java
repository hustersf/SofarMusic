package com.sf.demo.list.drag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.data.DemoData;
import com.sf.demo.enity.MenuItem;
import com.sf.utility.SharedPreUtil;

/**
 * Created by sufan on 17/6/19.
 */

public class MenuEditActivity extends UIRootActivity {

  private RecyclerView menu_rv;
  private MenuEditAdapter mAdapter;
  private List<MenuItem> mMenuList;

  private int mDashedPosition;


  @Override
  protected void onPause() {
    super.onPause();
    writeMenuEditJson();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.activity_menu_edit;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("可拖拽的菜单");
  }

  @Override
  public void initView() {
    menu_rv = findViewById(R.id.menu_rv);

    menu_rv.setLayoutManager((new GridLayoutManager(this, 4,
        GridLayoutManager.VERTICAL, false)));

  }

  @Override
  public void initData() {
    List<MenuItem> defaultList = DemoData.getDefaultMenu();
    List<MenuItem> allList = DemoData.getMyApplicationList();

    mMenuList = new ArrayList<>();

    int myAppNum;
    SharedPreUtil sp = new SharedPreUtil(this);
    String menuEditJson = sp.getToggleString("menuEditJson");
    if ("".equals(menuEditJson)) {
      mMenuList.addAll(defaultList);
    } else {
      mMenuList.addAll(JSONArray.parseArray(menuEditJson, MenuItem.class));
    }

    myAppNum = mMenuList.size() - 1;

    mMenuList.addAll(allList);

    MenuItem item = new MenuItem();
    item.isDashed = true;
    mMenuList.add(myAppNum + 1, item);

    mAdapter = new MenuEditAdapter(this, mMenuList);
    menu_rv.setAdapter(mAdapter);
  }

  @Override
  public void initEvent() {
    final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
    itemTouchHelper.attachToRecyclerView(menu_rv);

    mAdapter.setOnItemLongClickListener(new MenuEditAdapter.OnItemLongClickListener() {
      @Override
      public void OnItemLongClick(int position, MenuItem item, RecyclerView.ViewHolder holder,
          int dashedPosition) {
        itemTouchHelper.startDrag(holder);
        mDashedPosition = dashedPosition;
      }
    });


  }


  private void writeMenuEditJson() {
    SharedPreUtil sp = new SharedPreUtil(this);
    List<MenuItem> addList = new ArrayList<>();
    for (MenuItem item : mMenuList) {
      if (!item.isAdd) {
        addList.add(item);
      }
    }
    String menuEditJson = JSON.toJSON(addList).toString();
    sp.setToggleString("menuEditJson", menuEditJson);
  }


  ItemTouchHelper.SimpleCallback mCallback = new ItemTouchHelper.SimpleCallback(
      ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN,
      ItemTouchHelper.DOWN) {
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
        RecyclerView.ViewHolder target) {

      int fromPosition = viewHolder.getAdapterPosition();// 得到拖动ViewHolder的position
      int toPosition = target.getAdapterPosition();// 得到目标ViewHolder的position
      if (toPosition > 0 && toPosition < mDashedPosition) {
        if (fromPosition < toPosition) {
          for (int i = fromPosition; i < toPosition; i++) {
            Collections.swap(mMenuList, i, i + 1);
          }
        } else {
          for (int i = fromPosition; i > toPosition; i--) {
            Collections.swap(mMenuList, i, i - 1);
          }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);

      }
      return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isItemViewSwipeEnabled() {
      return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
      return false;
    }

    // 当长按选中item的时候（拖拽开始的时候）调用
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
      super.onSelectedChanged(viewHolder, actionState);
      if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
        final float alpha = 0.5f;
        viewHolder.itemView.setAlpha(alpha);
      }
    }

    // 当手指松开的时候（拖拽完成的时候）调用
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      super.clearView(recyclerView, viewHolder);
      viewHolder.itemView.setAlpha(1);
      Log.i("TAG", "clearView_position:" + viewHolder.getAdapterPosition());
    }
  };
}
