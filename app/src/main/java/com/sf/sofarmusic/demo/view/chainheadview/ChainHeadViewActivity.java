package com.sf.sofarmusic.demo.view.chainheadview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.util.FontUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 2017/10/24.
 */

public class ChainHeadViewActivity extends DemoActivity {
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;


    private ChainHeadView chainHeadView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_chain_head_view);
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

        head_title.setText("链式头像");

        head_right.setVisibility(View.GONE);

    }

    @Override
    public void initView() {
        chainHeadView=(ChainHeadView)findViewById(R.id.chain_hv);

    }

    @Override
    public void initData() {
        List<HeadInfo> datas=new ArrayList<>();
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.author_head_img);
        for(int i=0;i<12;i++){
            HeadInfo headInfo=new HeadInfo();
            headInfo.headBt=bitmap;
            headInfo.headName="头像"+i;
            datas.add(headInfo);
        }

        chainHeadView.init(5,datas);

    }

    @Override
    public void initEvent() {

    }
}
