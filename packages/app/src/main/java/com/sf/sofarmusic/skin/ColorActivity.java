package com.sf.sofarmusic.skin;

import static android.graphics.Typeface.createFromAsset;

import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sf.base.BaseActivity;
import com.sf.base.util.FontUtil;
import com.sf.demo.list.itemdecoration.OffsetDecoration;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.data.LocalData;
import com.sf.sofarmusic.enity.ColorItem;
import com.sf.sofarmusic.util.SkinUtil;
import com.sf.sofarmusic.view.HorizontalColorBar;
import com.sf.sofarmusic.view.LinkColorBar;
import com.sf.sofarmusic.view.OverScrollLayer;
import com.sf.sofarmusic.view.SlideQuadView;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.LogUtil;


/**
 * Created by sufan on 16/11/7.
 */

public class ColorActivity extends BaseActivity implements ColorAdapter.OnColorSelectedListener, View.OnClickListener, HorizontalColorBar.OnColorChangeListener {
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private ImageView square_iv;
    private TextView back_tv;

    private RecyclerView color_rv;
    private List<ColorItem> mColorList;
    private ColorAdapter mColorAdapter;

    private RelativeLayout rv_rl, color_rl;

    private int mWidth;  //屏幕宽度


    private HorizontalColorBar hbar;
    private LinkColorBar hbar2;

    private int mColor=0xFF313638;

    private final static int RESULT_CODE=100;

    private OverScrollLayer overScrollLayer;
    private SlideQuadView slideQuadView;
    private TextView moreTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        initHead();
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mColorList = LocalData.getColorListData();
        mColorAdapter = new ColorAdapter(baseAt, mColorList);
        color_rv.setAdapter(mColorAdapter);

        mWidth = DeviceUtil.getMetricsWidth(baseAt);

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        color_rv = (RecyclerView) findViewById(R.id.color_rv);
        square_iv = (ImageView) findViewById(R.id.square_iv);
        rv_rl = (RelativeLayout) findViewById(R.id.rv_rl);
        color_rl = (RelativeLayout) findViewById(R.id.color_rl);
        back_tv = (TextView) findViewById(R.id.back_tv);

        //bar
        hbar = (HorizontalColorBar) findViewById(R.id.hbar);
        hbar2 = (LinkColorBar) findViewById(R.id.hbar2);

        overScrollLayer = findViewById(R.id.over_scroll_layer);
        slideQuadView = findViewById(R.id.slide_quad_view);
        moreTv = findViewById(R.id.tv_more);


     //   dynamicAddView(toolbar, "background", R.color.colorPrimary);
        color_rv.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
      //  color_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        int itemSpace=DensityUtil.dp2px(this,15);
        int edgeSpace=DensityUtil.dp2px(this,10);
        color_rv.addItemDecoration(new OffsetDecoration(itemSpace,edgeSpace));

        //将背景初始化为黑色，否则内存会记住上次选中的颜色
        GradientDrawable gradient = (GradientDrawable) square_iv.getBackground();
        gradient.setColor(getResources().getColor(R.color.color_1));

        Typeface iconfont = FontUtil.setFont(this);
        back_tv.setTypeface(iconfont);

        hbar.add(hbar2);
    }

    private void initEvent() {
        mColorAdapter.setOnColorSelectedListener(this);
        back_tv.setOnClickListener(this);
        hbar.setOnColorChangeListener(this);

        RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) moreTv.getLayoutParams();
        int originRight=lp.rightMargin;
        int maxRight=-originRight+DensityUtil.dp2px(this,5);
        overScrollLayer.addOverScrollListener(new OverScrollLayer.OverScrollListener() {
            @Override
            public void onOverScroll(float distance) {
                slideQuadView.refreshView(distance);
                lp.rightMargin= (int) (originRight+maxRight*distance);
                moreTv.setLayoutParams(lp);
                LogUtil.d("ColorActivity","d:"+distance+" r:"+lp.rightMargin);

            }
        });

    }


    private void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        //设置字体
        Typeface iconfont = createFromAsset(getAssets(), "fonts/iconfont.ttf");
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("自选颜色");

        head_right.setText("确定");
        head_right.setVisibility(View.VISIBLE);
        head_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CODE);
                finish();
                SkinUtil.changeColorFromSp(baseAt,mColor);
            }
        });

    }


    @Override
    public void onColor(int position) {
        if (position == mColorList.size() - 1) {
            AnimIn();

        } else {
            int color = getResources().getColor(mColorList.get(position).color);
            GradientDrawable gradient = (GradientDrawable) square_iv.getBackground();
            gradient.setColor(color);
            mColor=color;
        }

        //刷新adapter
        if (position != mColorList.size() - 1) {
            mColorList.get(mColorList.size() - 1).isSelected=(false);
            for (int i = 0; i < mColorList.size() - 1; i++) {
                if (i == position) {
                    mColorList.get(position).isSelected=(true);
                } else {
                    mColorList.get(i).isSelected=(false);
                }
            }
            mColorAdapter.setNewData(mColorList);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_tv:
                AnimOut();
                break;
        }
    }

    private void AnimIn() {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(rv_rl, "translationX", 0f, -mWidth);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(color_rl, "translationX", mWidth, 0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(oa1, oa2);
        set.start();
        color_rl.setVisibility(View.VISIBLE);

    }

    private void AnimOut() {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(rv_rl, "translationX", -mWidth, 0f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(color_rl, "translationX", 0f, mWidth);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.playTogether(oa1, oa2);
        set.start();
    }

    @Override
    public void onColorBar(int color) {
        GradientDrawable gradient = (GradientDrawable) square_iv.getBackground();
        gradient.setColor(color);
        mColor=color;

        //刷新adapter
        for (int i = 0; i < mColorList.size(); i++) {
            if (i == mColorList.size() - 1) {
                mColorList.get(mColorList.size() - 1).isSelected=(true);
            } else {
                mColorList.get(i).isSelected=(false);
            }
        }
        mColorAdapter.setNewData(mColorList);

    }
}
