package com.sf.demo.md;

import android.content.Intent;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.md.share_elemnt.ShareElementActivtyA;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/6/26.
 */

public class MDShowActivity extends UIRootActivity {

    private FlowTagList tag_fl;

    private String[] mTags = {"Coordinator+AppBar",
            "Coordinator+AppBar+CollapsingToolbar","共享元素"};


    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo_show;
    }

    @Override
    protected void initTitle() {
        head_title.setText("MD效果页面");
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

                doTag(text, position);
            }
        });

    }

    private void doTag(String text, int position) {
        if(mTags[0].equals(text)){
            Intent intent=new Intent(this,MD1Activity.class);
            startActivity(intent);
        }else if(mTags[1].equals(text)){
            Intent intent=new Intent(this,MD2Activity.class);
            startActivity(intent);
        }else if(mTags[2].equals(text)){
            Intent intent=new Intent(this,ShareElementActivtyA.class);
            startActivity(intent);
        }
    }
}
