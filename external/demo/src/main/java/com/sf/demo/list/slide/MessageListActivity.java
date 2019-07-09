package com.sf.demo.list.slide;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.enity.MessageItem;
import com.sf.demo.list.slide.itemtouchhelperextension.ItemTouchHelperExtension;

/**
 * Created by sufan on 17/6/19.
 */

public class MessageListActivity extends UIRootActivity {

  private RecyclerView message_rv;
  private MessageListAdapter mAdapter;
  private List<MessageItem> mMessageList;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_message_list;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("可左右滑动删除的列表");
  }

  @Override
  public void initView() {
    message_rv = (RecyclerView) findViewById(R.id.message_rv);

    message_rv.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  public void initData() {

    mMessageList = new ArrayList<>();
    mMessageList = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      MessageItem item = new MessageItem();
      if (i % 2 == 0) {
        item.title = "微信团队";
        item.time = "12月18日";
        item.content = "欢迎你使用微信";
      } else {
        item.title = "腾讯新闻";
        item.time = "18:18";
        item.content = "XX地区出现大量鱼虾死亡";
      }
      mMessageList.add(item);
    }

    mAdapter = new MessageListAdapter(this, mMessageList);
    message_rv.setAdapter(mAdapter);
  }


  @Override
  public void initEvent() {
    // final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
    // itemTouchHelper.attachToRecyclerView(message_rv);

    ItemTouchHelperCallback callback = new ItemTouchHelperCallback();
    ItemTouchHelperExtension mItemTouchHelper = new ItemTouchHelperExtension(callback);
    mItemTouchHelper.attachToRecyclerView(message_rv);
  }



  // 自带的这个无法触发删除的点击事件
  ItemTouchHelper.SimpleCallback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
      ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
      return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
        RecyclerView.ViewHolder target) {
      return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
        float dX, float dY, int actionState, boolean isCurrentlyActive) {
      MessageListAdapter.ItemHolder holder = (MessageListAdapter.ItemHolder) viewHolder;

      if (dX < -holder.delete_tv.getWidth()) {
        dX = -holder.delete_tv.getWidth();
      }
      holder.item_rl.setTranslationX(dX);
      return;
    }


  };



}
