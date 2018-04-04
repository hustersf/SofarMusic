package com.sf.sofarmusic.demo.list.expand;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.data.DemoData;
import com.sf.sofarmusic.util.FontUtil;

import java.util.List;

/**
 * Created by sufan on 17/6/27.
 */

public class ExpandListActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private RecyclerView expand_rv;
    private ExpandListAdapter mAdapter;
    private List<MenuParent> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_expand_list);
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

        head_title.setText("折叠列表");

        head_right.setVisibility(View.GONE);


    }

    @Override
    public void initView() {
        expand_rv=(RecyclerView)findViewById(R.id.expand_rv);
        expand_rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        mDatas= DemoData.getExpandList();
        mAdapter=new ExpandListAdapter(this,mDatas);
        expand_rv.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {

    }
}
