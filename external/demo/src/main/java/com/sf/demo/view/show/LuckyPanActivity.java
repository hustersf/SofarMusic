package com.sf.demo.view.show;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.view.LuckyPan;

/**
 * Created by sufan on 17/6/17.
 */

public class LuckyPanActivity extends UIRootActivity {

    private LuckyPan lucky_sv;
    private ImageView lucky_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_lucky_pan);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lucky_pan;
    }

    @Override
    protected void initTitle() {
        head_title.setText("幸运转盘");
    }

    @Override
    public void initView() {
        lucky_sv = (LuckyPan) findViewById(R.id.luck_sv);
        lucky_iv = (ImageView) findViewById(R.id.lucky_iv);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        lucky_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lucky_sv.isStop()) {
                    lucky_sv.start(4);
                    lucky_iv.setImageResource(R.drawable.demo_lucky_pan_stop);
                }else {
                    lucky_sv.stop();
                    lucky_iv.setImageResource(R.drawable.demo_lucky_pan_start);
                }
            }
        });

    }


}


