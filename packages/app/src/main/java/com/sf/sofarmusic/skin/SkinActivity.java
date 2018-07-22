package com.sf.sofarmusic.skin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.BaseActivity;
import com.sf.sofarmusic.data.LocalData;
import com.sf.sofarmusic.enity.SkinItem;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.utility.SharedPreUtil;
import com.sf.sofarmusic.util.SkinUtil;
import com.sf.utility.ToastUtil;
import com.sf.sofarmusic.widget.swipe.SwipeBack;

import java.util.List;

/**
 * Created by sufan on 16/11/5.
 */

public class SkinActivity extends BaseActivity implements SkinAdapter.OnSkinItemClickListener {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private RecyclerView skin_rv;
    private SkinAdapter mSkinAdapter;
    private List<SkinItem> mSkinList;

    private static final int REQUEST_CODE=100;  //>0的整数即可

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
        initHead();
        initView();
        initData();
        initEvent();


        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        Log.d("TAG",launchIntent.getComponent().getClassName());
    }


    @Override
    protected void onPause() {
        super.onPause();
        writeSkinJson();
    }


    private void initData() {
        //recyclerview
        readSkinJson();
    }

    private void readSkinJson() {
        SharedPreUtil sp=new SharedPreUtil(this);
        String skinJson=sp.getToggleString("skinJson");
        if("".equals(skinJson)) {
            mSkinList = LocalData.getSkinListData();
        }else {
            mSkinList= JSONArray.parseArray(skinJson, SkinItem.class);
        }
        mSkinAdapter = new SkinAdapter(this, mSkinList);
        skin_rv.setAdapter(mSkinAdapter);
    }

    private void writeSkinJson() {
        SharedPreUtil sp=new SharedPreUtil(this);
        String skinJson= JSON.toJSON(mSkinList).toString();
        sp.setToggleString("skinJson",skinJson);
    }

    private void initEvent() {
        SwipeBack.attach(this);
        mSkinAdapter.setOnSkinItemClickListener(this);

        //测试是否滑动到了底部
        skin_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){

                }
            }
        });
    }

    private void initView() {
        skin_rv = (RecyclerView) findViewById(R.id.skin_rv);
        skin_rv.setLayoutManager((new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false)));
        skin_rv.getItemAnimator().setChangeDuration(0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

    }

    private void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("主题换肤");

        head_right.setText("管理");
        head_right.setVisibility(View.VISIBLE);
        head_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.startShort(baseAt, "管理");
            }
        });

    }

    @Override
    public void onSkin(String des) {

        if ("官方蓝".equals(des)) {
            SkinUtil.changeSkin(baseAt, "themeblue.skin");
        } else if ("官方粉".equals(des)) {
            SkinUtil.changeSkin(baseAt, "themepink.skin");
        } else if ("自选颜色".equals(des)) {
            Intent color = new Intent(baseAt, ColorActivity.class);
            startActivityForResult(color,REQUEST_CODE);
        } else {
            String skinName = des + ".skin";
            SkinUtil.changeSkin(baseAt, skinName);
        }

    }


    /**
     *
     * @param requestCode     setResult方法返回的结果码
     * @param resultCode     startActivityForResult中的请求码
     * @param data           setResult方法返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==100) {
            //100,说明ColorActivity按了确定键，这样才更新adapter
           mSkinAdapter.refreshList(3);  //positio=3，即是自选颜色框
        }
    }




}
