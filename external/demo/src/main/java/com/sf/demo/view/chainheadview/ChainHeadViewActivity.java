package com.sf.demo.view.chainheadview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 2017/10/24.
 */

public class ChainHeadViewActivity extends UIRootActivity {


    private ChainHeadView chainHeadView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chain_head_view;
    }

    @Override
    protected void initTitle() {
        head_title.setText("链式头像");
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
