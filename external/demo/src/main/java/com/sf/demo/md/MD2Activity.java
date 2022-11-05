package com.sf.demo.md;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sf.base.util.FontUtil;
import com.sf.demo.DemoActivity;
import com.sf.demo.R;

/**
 * Created by sufan on 17/7/5.
 */

public class MD2Activity extends DemoActivity {
    private TextView head_back, head_title, head_right;

    private CollapsingToolbarLayout collapsing;
    private FloatingActionButton fab;


    private RecyclerView md2_rv;
    private List<String> mDatas;
    private TypeListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_md2);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);


        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("");

        head_right.setVisibility(View.GONE);

    }

    @Override
    public void initView() {
        md2_rv=(RecyclerView)findViewById(R.id.md2_rv);
        StaggeredGridLayoutManager
          layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        md2_rv.setLayoutManager(layoutManager);

        collapsing=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        dynamicAddView(collapsing,"contentScrimColor",R.color.head_title_bg_color);

        collapsing.setTitle("视差效果");
        //设置还没收缩时状态下字体颜色
        collapsing.setExpandedTitleColor(Color.WHITE);
       // collapsing.setExpandedTitleGravity(Gravity.CENTER);

        //设置收缩后Toolbar上字体的颜色
        collapsing.setCollapsedTitleTextColor(Color.WHITE);
      //  collapsing.setCollapsedTitleGravity(Gravity.CENTER_VERTICAL);

        fab=(FloatingActionButton)findViewById(R.id.fab);

    }

    @Override
    public void initData() {
        mDatas=new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            mDatas.add("测试" + (i + 1));
        }
        mAdapter=new TypeListAdapter(this,mDatas);
        md2_rv.setAdapter(mAdapter);

    }

    @Override
    public void initEvent() {

    }
}
