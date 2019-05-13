package com.sf.demo.view.highlight.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.utility.ToastUtil;
import com.sf.widget.highlight.Component;

/**
 * Created by sufan on 17/7/27.
 */

public class ComponentTab4 implements Component{

    private String title;
    private TextView title_tv,next_tv;

    public ComponentTab4(String title){
        this.title=title;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        final View view=inflater.inflate(R.layout.layer_tab1,null);
        title_tv=(TextView)view.findViewById(R.id.title_tv);
        next_tv=(TextView)view.findViewById(R.id.next_tv);

        title_tv.setText(title);
        next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.startShort(v.getContext(),"立即体验");

            }
        });

        return view;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 5;
    }
}
