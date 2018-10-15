package com.sf.demo.view.show;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.view.LockPatternView;

/**
 * Created by sufan on 17/6/26.
 */

public class LockPatternActivity extends UIRootActivity {

    private LockPatternView lock;
    private TextView pass_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_lock_pattern);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock_pattern;
    }

    @Override
    protected void initTitle() {
        head_title.setText("手势密码");
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
