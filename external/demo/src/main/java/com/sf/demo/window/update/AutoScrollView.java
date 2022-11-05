package com.sf.demo.window.update;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;

/**
 * Created by sufan on 17/8/1.
 */

public class AutoScrollView extends ScrollView {
    private Context mContext;

    public AutoScrollView(Context context) {
        super(context);
        init(context);
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            //最大高度显示为屏幕内容高度的一半
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            //此处是关键，设置控件高度不能超过屏幕高度一半（在此替换成自己需要的高度）
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels / 3, MeasureSpec.AT_MOST);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
