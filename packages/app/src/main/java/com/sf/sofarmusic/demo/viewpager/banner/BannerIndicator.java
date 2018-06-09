package com.sf.sofarmusic.demo.viewpager.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sf.sofarmusic.R;


/**
 * Created by wanghan on 2017/1/18.
 */

public class BannerIndicator extends LinearLayout implements LoopVPAdapter.BannerIndicator {

    private int count;

    public BannerIndicator(Context context) {
        this(context, null);
    }

    public BannerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void InitIndicatorItems(int itemsNumber) {
        count = itemsNumber;
        removeAllViews();
        for (int i = 0; i < itemsNumber; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.dot_unselected_white);
            imageView.setPadding(10, 0, 10, 0);
            addView(imageView);
        }
    }

    @Override
    public void setIndicator(int position) {
        ImageView imageView = null;
        for (int i = 0; i < count; i++) {
            imageView = (ImageView) getChildAt(i);
            if (i == position) {
                imageView.setImageResource(R.drawable.dot_selected);
            } else {
                imageView.setImageResource(R.drawable.dot_unselected_white);
            }
        }
    }


}
