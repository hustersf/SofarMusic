package com.sf.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.base.util.FontUtil;

/**
 * Created by sufan on 2018/4/18.
 * 将公共的UI抽出来
 * BaseActivity抽出的是公共的逻辑
 */

public abstract class UIRootActivity extends BaseActivity {

    //公共标题
    protected TextView head_back, head_title, head_right;
    protected Toolbar toolbar;
    private LinearLayout ll_root;


    protected abstract int getLayoutId();

    protected abstract void initTitle();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_root);
        ll_root = findViewById(R.id.ll_root);
        int subRootViewId = getLayoutId();
        if (subRootViewId > 0) {
            getLayoutInflater().inflate(subRootViewId, ll_root);
        }

        setTitle();
        initTitle();
        initView();
        initData();
        initEvent();
    }



    private void setTitle() {
        head_back = findViewById(R.id.head_back);
        head_title = findViewById(R.id.head_title);
        head_right = findViewById(R.id.head_right);
        toolbar = findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);
        Typeface iconFont = FontUtil.setFont(this);
        head_back.setTypeface(iconFont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
