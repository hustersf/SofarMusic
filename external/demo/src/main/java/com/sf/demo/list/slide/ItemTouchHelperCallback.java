
package com.sf.demo.list.slide;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.sf.demo.list.slide.itemtouchhelperextension.ItemTouchHelperExtension;


public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {

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
}
