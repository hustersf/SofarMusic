package com.sf.sofarmusic.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.PlayerBaseActivity;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 16/12/1.
 */

public class LocalActivity extends PlayerBaseActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private TabLayout local_tl;
    private ViewPager local_vp;

    private SingleFragment mSingleFragment;
    private ArtistFragment mArtistFragment;
    private AlbumFragment mAlbumFragment;
    private FileFragment mFileFragment;
    private LocalFmAdapter localFmAdapter;
    private List<Fragment> mFmList;
    private List<String> mTitleList;
    private String[] titles = {"单曲", "歌手", "专辑", "文件夹"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        initHead();
        initView();
        initData();
        initEvent();
    }


    private void initData() {

        //tablayout+viewpager
        mSingleFragment = new SingleFragment();
        mArtistFragment = new ArtistFragment();
        mAlbumFragment = new AlbumFragment();
        mFileFragment = new FileFragment();
        mFmList = new ArrayList<>();
        mFmList.add(mSingleFragment);
        mFmList.add(mArtistFragment);
        mFmList.add(mAlbumFragment);
        mFmList.add(mFileFragment);
        FragmentManager fm = getSupportFragmentManager();
        mTitleList = Arrays.asList(titles);
        localFmAdapter = new LocalFmAdapter(fm, mFmList, mTitleList);
        local_vp.setAdapter(localFmAdapter);
        local_vp.setCurrentItem(0);
        local_vp.setOffscreenPageLimit(4);

        local_tl.addTab(local_tl.newTab().setText(titles[0]));
        local_tl.addTab(local_tl.newTab().setText(titles[1]));
        local_tl.addTab(local_tl.newTab().setText(titles[2]));
        local_tl.addTab(local_tl.newTab().setText(titles[3]));
        local_tl.setupWithViewPager(local_vp);

    }

    private void initEvent() {

         //监听单曲界面的变化
         mSingleFragment.setOnUpdateListener(new SingleFragment.OnUpdateListener() {
             @Override
             public void onUpdate() {
                 updateBottom();
                 updateFragmentData();
             }
         });

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        local_tl = (TabLayout) findViewById(R.id.local_tl);
        local_vp = (ViewPager) findViewById(R.id.local_vp);
        dynamicAddView(local_tl, "tabLayoutIndicator", R.color.themeColor);
        dynamicAddView(local_tl, "tabLayoutTextColor", R.color.main_text_color);
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

        head_title.setText("本地音乐");

        head_right.setText("管理");
        head_right.setVisibility(View.GONE);
        head_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.startShort(baseAt, "管理");
            }
        });

    }

    @Override
    protected void updateSongList() {
        super.updateSongList();
        mSingleFragment.refreshData();
        updateFragmentData();
    }


    private void updateFragmentData(){
        mArtistFragment.refreshData();
        mAlbumFragment.refreshData();
        mFileFragment.refreshData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==100){
           //100说明了，ShowDetailActivity界面 选择了歌曲
            mSingleFragment.refreshData();
            updateFragmentData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
