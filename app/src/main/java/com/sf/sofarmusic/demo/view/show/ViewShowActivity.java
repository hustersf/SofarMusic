package com.sf.sofarmusic.demo.view.show;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.view.calendar.CalendarActivity;
import com.sf.sofarmusic.demo.view.chainheadview.ChainHeadViewActivity;
import com.sf.sofarmusic.demo.view.highlight.HighLightActivity;
import com.sf.sofarmusic.demo.view.linechart.IncomeExpendActivity;
import com.sf.sofarmusic.demo.view.numtextview.NumberTextActivity;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.view.FlowTagList;

/**
 * Created by sufan on 17/6/26.
 */

public class ViewShowActivity extends DemoActivity {
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private FlowTagList tag_fl;

    private String[] mTags={"圆盘菜单","幸运转盘","手势密码","日历","收入支出折线图","NumberTextView",
    "高亮引导图","链式头像"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_demo_show);
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

        head_title.setText("自定义View集合");

        head_right.setVisibility(View.GONE);

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
        if("圆盘菜单".equals(text)){
            Intent circle=new Intent(this, CircleMenuActivity.class);
            startActivity(circle);
        }else if("幸运转盘".equals(text)){
            Intent lucky=new Intent(this, LuckyPanActivity.class);
            startActivity(lucky);
        }else if("手势密码".equals(text)){
            Intent lock=new Intent(this, LockPatternActivity.class);
            startActivity(lock);
        }else if("日历".equals(text)){
            Intent calendar=new Intent(this, CalendarActivity.class);
            startActivity(calendar);
        }else if("收入支出折线图".equals(text)){
            Intent linechart=new Intent(this, IncomeExpendActivity.class);
            startActivity(linechart);
        }else if("NumberTextView".equals(text)){
            Intent number=new Intent(this, NumberTextActivity.class);
            startActivity(number);
        }else if("高亮引导图".equals(text)){
            Intent highLight=new Intent(this, HighLightActivity.class);
            startActivity(highLight);
        }else if("链式头像".equals(text)){
            Intent chain=new Intent(this, ChainHeadViewActivity.class);
            startActivity(chain);
        }
    }
}
