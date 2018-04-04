package com.sf.sofarmusic.demo.view.show;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.view.LuckyPan;
import com.sf.sofarmusic.util.FontUtil;

/**
 * Created by sufan on 17/6/17.
 */

public class LuckyPanActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private LuckyPan lucky_sv;
    private ImageView lucky_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_lucky_pan);
        super.onCreate(savedInstanceState);
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

        head_title.setText("幸运转盘");

        head_right.setVisibility(View.GONE);

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


