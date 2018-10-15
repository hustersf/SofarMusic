package com.sf.demo.view.highlight.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.demo.view.highlight.core.Component;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/7/27.
 */

public class ComponentTab2 implements Component {

    private String title;
    private TextView title_tv, next_tv;
    private int tag;


    public ComponentTab2(String title, int tag) {
        this.title = title;
        this.tag = tag;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        final View view = inflater.inflate(R.layout.layer_tab1, null);
        title_tv = (TextView) view.findViewById(R.id.title_tv);
        next_tv = (TextView) view.findViewById(R.id.next_tv);

        title_tv.setText(title);
        next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.startShort(v.getContext(), "立即体验");

            }
        });

        return view;
    }

    @Override
    public int getAnchor() {
        if (tag == 1)
            return Component.ANCHOR_RIGHT;
        else
            return Component.ANCHOR_LEFT;

    }

    @Override
    public int getFitPosition() {
        return Component.FIT_START;
    }

    @Override
    public int getXOffset() {
        if (tag == 1)
            return 20;
        else
            return -20;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
