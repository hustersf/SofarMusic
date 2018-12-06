package com.sf.demo.view.show;

import java.util.List;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.base.UIRootActivity;
import com.sf.base.util.ImageUtil;
import com.sf.demo.R;
import com.sf.demo.data.DemoData;
import com.sf.demo.enity.MenuItem;
import com.sf.demo.view.CircleImageView;
import com.sf.demo.view.CircleLayout;
import com.sf.utility.DensityUtil;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/6/17.
 */

public class CircleMenuActivity extends UIRootActivity {

    private CircleLayout circleMenu;
    private List<MenuItem> menuList;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_circle_menu;
    }

    @Override
    protected void initTitle() {
        head_title.setText("圆盘菜单");
    }

    @Override
    public void initView() {
        circleMenu = (CircleLayout) findViewById(R.id.circle_cl);

    }

    @Override
    public void initData() {
        addView();
    }


    @Override
    public void initEvent() {
        circleMenu.setOnItemClickListener(new CircleLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id, String name) {
                ToastUtil.startShort(baseAt,name);
            }
        });

    }

    private void addView() {
        menuList = DemoData.getMenuList();
        for (int i = 0; i < menuList.size(); i++) {

            MenuItem item=menuList.get(i);

            CircleImageView circleImageView=new CircleImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            circleImageView.setLayoutParams(lp);
            circleImageView.setOrientation(LinearLayout.VERTICAL);
            circleImageView.setName(item.name);

            ImageView imageView=new ImageView(this);
            LinearLayout.LayoutParams imageViewLayoutParams=new LinearLayout.LayoutParams(
                    DensityUtil.dp2px(this,30),DensityUtil.dp2px(this,30), Gravity.CENTER);
            imageViewLayoutParams.gravity=Gravity.CENTER;
            imageView.setLayoutParams(imageViewLayoutParams);
            ImageUtil.setDrawableByName(this,"demo_menu_"+item.imgName,imageView);

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams mTextViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mTextViewParams.gravity=Gravity.CENTER;  //相当于android:layout_gravity
            textView.setLayoutParams(mTextViewParams);
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

            if(item.name.length()>4){
                textView.setText(item.name.substring(0,4)+"...");
            }else {
                textView.setText(item.name);
            }

            circleImageView.addView(imageView);
            circleImageView.addView(textView);

            circleMenu.addView(circleImageView);

            if(i>10){
                return;
            }

        }

    }

}


