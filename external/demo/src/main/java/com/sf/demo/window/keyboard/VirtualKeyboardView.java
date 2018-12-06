package com.sf.demo.window.keyboard;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.sf.demo.R;
import com.sf.demo.list.itemdecoration.DividerGridItemDecoration;

/**
 * Created by sufan on 17/6/29.
 * 键盘高度54*4+40
 * 如果height=wrap_content会充满整个屏幕
 * 54 按键高度， 40隐藏键盘上方的高度
 */

public class VirtualKeyboardView extends RelativeLayout {
    private Context mContext;
    private List<String> mValueList;

    private RecyclerView keyboard_rv;
    private RelativeLayout layoutBack;
    private KeyboardAdapter keyboardAdapter;

    private boolean mHasDot;   //键盘是否有小数点

    public VirtualKeyboardView(Context context) {
        this(context, null);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = View.inflate(context, R.layout.layout_virtual_keyboard, null);
        layoutBack = (RelativeLayout) view.findViewById(R.id.layoutBack);
        keyboard_rv = (RecyclerView) view.findViewById(R.id.keyboard_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        keyboard_rv.setLayoutManager(gridLayoutManager);

        addView(view);      //必须要，不然不显示控件

    }

    private void initValueList() {
        mValueList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                mValueList.add(String.valueOf(i));
            } else if (i == 10) {
                if (mHasDot) {
                    mValueList.add(".");
                } else {
                    mValueList.add("");
                }
            } else if (i == 11) {
                mValueList.add(String.valueOf(0));
            } else if (i == 12) {
                mValueList.add("");
            }
        }

    }

    public void init(boolean hasDot) {
        mHasDot = hasDot;
        initValueList();
        setupView();
    }

    private void setupView() {
        keyboardAdapter = new KeyboardAdapter(mContext, mValueList);
        keyboard_rv.setAdapter(keyboardAdapter);

        //增加分割线
        GradientDrawable shapedrawable = new GradientDrawable();
        shapedrawable.setShape(GradientDrawable.RECTANGLE);
        shapedrawable.setColor(Color.parseColor("#e5e5e5"));
        int defaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 2f, mContext.getResources().getDisplayMetrics());
        shapedrawable.setSize(defaultWidth, defaultWidth);
        keyboard_rv.addItemDecoration(new DividerGridItemDecoration(shapedrawable));

    }

    public RelativeLayout getLayoutBack() {
        return layoutBack;
    }

    public RecyclerView getRecyclerView() {
        return keyboard_rv;
    }

    public List<String> getValues() {
        return mValueList;
    }


    public KeyboardAdapter getAdapter() {
        return keyboardAdapter;
    }

}
