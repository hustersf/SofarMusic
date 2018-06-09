package com.sf.sofarmusic.demo.view.highlight.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.view.highlight.core.Component;
import com.sf.sofarmusic.util.ToastUtil;

/**
 * Created by sufan on 17/7/27.
 */

public class ComponentTab3 implements Component{

    private TextView title_tv,next_tv;



    @Override
    public View getView(LayoutInflater inflater) {
        final View view=inflater.inflate(R.layout.layer_tab1,null);
        title_tv=(TextView)view.findViewById(R.id.title_tv);
        next_tv=(TextView)view.findViewById(R.id.next_tv);

        next_tv.setText("我知道了");
        title_tv.setText("点击这里可以返回");
        next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.startShort(v.getContext(),"我知道了");

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
        return Component.FIT_START;
    }

    @Override
    public int getXOffset() {
        return 20;
    }

    @Override
    public int getYOffset() {
        return 20;
    }
}
