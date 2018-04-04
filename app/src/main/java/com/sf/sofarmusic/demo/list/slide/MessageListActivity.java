package com.sf.sofarmusic.demo.list.slide;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.enity.MessageItem;
import com.sf.sofarmusic.demo.list.slide.itemtouchhelperextension.ItemTouchHelperExtension;
import com.sf.sofarmusic.util.FontUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/19.
 */

public class MessageListActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private RecyclerView message_rv;
    private MessageListAdapter mAdapter;
    private List<MessageItem> mMessageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_message_list);
        super.onCreate(savedInstanceState);
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
    public void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("可左右滑动删除的列表");

        head_right.setVisibility(View.GONE);

    }

    @Override
    public void initEvent() {
//        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
//        itemTouchHelper.attachToRecyclerView(message_rv);

        ItemTouchHelperCallback callback = new ItemTouchHelperCallback();
        ItemTouchHelperExtension mItemTouchHelper = new ItemTouchHelperExtension(callback);
        mItemTouchHelper.attachToRecyclerView(message_rv);
    }



    //自带的这个无法触发删除的点击事件
    ItemTouchHelper.SimpleCallback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.LEFT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }


        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            MessageListAdapter.ItemHolder holder = (MessageListAdapter.ItemHolder) viewHolder;

            if (dX < -holder.delete_tv.getWidth()) {
                dX = -holder.delete_tv.getWidth();
            }
            holder.item_rl.setTranslationX(dX);
            return;
        }


    };



}
