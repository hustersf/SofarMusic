package com.sf.sofarmusic.demo.view.numtextview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.util.FontUtil;

/**
 * Created by sufan on 17/7/11.
 */

public class NumberTextActivity extends DemoActivity {
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private NumberAnimTextView number1_tv,number2_tv,number3_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_number_textview);
        super.onCreate(savedInstanceState);
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

        head_title.setText("滚动数字");

        head_right.setVisibility(View.GONE);

    }

    @Override
    public void initView() {
        number1_tv=(NumberAnimTextView)findViewById(R.id.number1_tv);
        number2_tv=(NumberAnimTextView)findViewById(R.id.number2_tv);
        number3_tv=(NumberAnimTextView)findViewById(R.id.number3_tv);

    }

    @Override
    public void initData() {

        number1_tv.setDuration(2000);
        number1_tv.setNumberString("99.7588");

        number2_tv.setDuration(1000);
        number2_tv.setNumberString("123456");
        number2_tv.setPostfixString("%");


        number3_tv.setNumberString("99999.99");
        number3_tv.setPrefixString("￥");
        number3_tv.setDuration(1000);

    }

    @Override
    public void initEvent() {

    }
}
