package com.sf.sofarmusic.demo.view.show;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.view.LockPatternView;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.LogUtil;

/**
 * Created by sufan on 17/6/26.
 */

public class LockPatternActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private LockPatternView lock;
    private TextView pass_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_lock_pattern);
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


        head_title.setText("手势密码");

        head_right.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        lock = (LockPatternView) findViewById(R.id.lock);
        pass_tv = (TextView) findViewById(R.id.pass_tv);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

        lock.setOnPointChangeListener(new LockPatternView.OnPointChangeListener() {
            @Override
            public void onPointLess(int size) {
                if (size == 0) {
                    pass_tv.setText("请绘制解锁图案");
                } else {
                    pass_tv.setText("至少" + size + "个图案");
                }
            }

            @Override
            public void onPointEnd(String pass) {
                pass_tv.setText(pass);
            }
        });

    }
}
