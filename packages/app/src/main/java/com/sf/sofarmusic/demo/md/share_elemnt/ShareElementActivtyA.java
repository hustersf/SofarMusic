package com.sf.sofarmusic.demo.md.share_elemnt;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.base.UIRootActivity;
import com.sf.sofarmusic.demo.md.TypeListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShareElementActivtyA extends UIRootActivity {
    private RecyclerView rv_type;
    private TypeListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_element_a;
    }

    @Override
    protected void initTitle() {
        head_title.setText("共享元素");
    }

    @Override
    protected void initView() {
        rv_type=findViewById(R.id.rv_type);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_type.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            datas.add("item" + (i + 1));
        }
        mAdapter = new TypeListAdapter(this, datas);
        rv_type.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
       mAdapter.setOnItemClickListener(new TypeListAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(String data, int position, View itemView) {
               ViewCompat.setTransitionName(itemView, "share_element");
               Intent intent=new Intent(baseAt,ShareElementActivtyB.class);
               func1(intent,itemView);
           }
       });

    }

    private void func1(Intent intent,View itemView){
        //5.0共享元素动画效果和Activity转场动画效果，同时设置转场失效
//        intent.putExtra(FINISH_EXIT_PAGE_ANIMATION,R.anim.activity_animation_zoom_exit);
//        startActivity(intent);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(baseAt, itemView, "share_element");
            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }
}
