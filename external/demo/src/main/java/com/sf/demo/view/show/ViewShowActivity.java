package com.sf.demo.view.show;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.view.calendar.CalendarActivity;
import com.sf.demo.view.chainheadview.ChainHeadViewActivity;
import com.sf.demo.view.highlight.HighLightActivity;
import com.sf.demo.view.linechart.IncomeExpendActivity;
import com.sf.demo.view.numtextview.NumberTextActivity;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/6/26.
 */

public class ViewShowActivity extends UIRootActivity {

    private FlowTagList tag_fl;
    private RelativeLayout rl_layout;

    private int i = 0;

    private String[] mTags = {"圆盘菜单", "幸运转盘", "手势密码", "日历", "收入支出折线图", "NumberTextView",
            "高亮引导图", "链式头像"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_demo_show);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo_show;
    }

    @Override
    protected void initTitle() {
        head_title.setText("自定义View集合");
    }

    @Override
    public void initView() {
        tag_fl = (FlowTagList) findViewById(R.id.tag_fl);
        dynamicAddView(tag_fl, "tagColor", R.color.themeColor);

        rl_layout = findViewById(R.id.rl_layout);
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

        rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int marginR = ((RelativeLayout.LayoutParams) rl_layout.getLayoutParams()).rightMargin;
                if (i % 2 == 0) {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(rl_layout, "translationX", 0f, marginR);
                    anim.start();
                } else {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(rl_layout, "translationX", marginR, 0f);
                    anim.start();
                }
                i++;
            }
        });

    }

    private void doTag(String text, int position) {
        if ("圆盘菜单".equals(text)) {
            Intent circle = new Intent(this, CircleMenuActivity.class);
            startActivity(circle);
        } else if ("幸运转盘".equals(text)) {
            Intent lucky = new Intent(this, LuckyPanActivity.class);
            startActivity(lucky);
        } else if ("手势密码".equals(text)) {
            Intent lock = new Intent(this, LockPatternActivity.class);
            startActivity(lock);
        } else if ("日历".equals(text)) {
            Intent calendar = new Intent(this, CalendarActivity.class);
            startActivity(calendar);
        } else if ("收入支出折线图".equals(text)) {
            Intent linechart = new Intent(this, IncomeExpendActivity.class);
            startActivity(linechart);
        } else if ("NumberTextView".equals(text)) {
            Intent number = new Intent(this, NumberTextActivity.class);
            startActivity(number);
        } else if ("高亮引导图".equals(text)) {
            Intent highLight = new Intent(this, HighLightActivity.class);
            startActivity(highLight);
        } else if ("链式头像".equals(text)) {
            Intent chain = new Intent(this, ChainHeadViewActivity.class);
            startActivity(chain);
        }
    }
}
