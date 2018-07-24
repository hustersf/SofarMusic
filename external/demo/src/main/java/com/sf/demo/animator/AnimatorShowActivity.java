package com.sf.demo.animator;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/7/4.
 */

public class AnimatorShowActivity extends UIRootActivity {

    private FlowTagList tag_fl;
    private String[] mTags={"圆盘菜单","旋转动画"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo_show;
    }

    @Override
    protected void initTitle() {
        head_title.setText("动画展示");

    }

    @Override
    public void initView() {
        tag_fl = (FlowTagList) findViewById(R.id.tag_fl);
        dynamicAddView(tag_fl, "tagColor", R.color.themeColor);
    }

    @Override
    public void initData() {
        tag_fl.setTags(mTags);
    }

    @Override
    public void initEvent() {
        tag_fl.setOnTagClickListener(new FlowTagList.OnTagClickListener() {
            @Override
            public void OnTagClick(String text, int position) {
                for (int i = 0; i < mTags.length; i++) {
                    if (i == position) {
                        tag_fl.setChecked(true, position);
                    } else {
                        tag_fl.setChecked(false, i);
                    }
                }
                tag_fl.notifyAllTag();

                doTag(text,position);
            }
        });
    }

    private void doTag(String text,int position){


    }
}
