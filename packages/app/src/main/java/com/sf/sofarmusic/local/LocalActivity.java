package com.sf.sofarmusic.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;
import com.sf.utility.ToastUtil;

/**
 * 本地音乐
 */
public class LocalActivity extends PlayerBaseActivity {

  private TextView headBack, headTitle, headRight;

  private TabLayout localTabLayout;
  private ViewPager localVp;

  private SingleFragment mSingleFragment;
  private AuthorFragment mArtistFragment;
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
    mSingleFragment = new SingleFragment();
    mArtistFragment = new AuthorFragment();
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
    localVp.setAdapter(localFmAdapter);
    localVp.setCurrentItem(0);
    localVp.setOffscreenPageLimit(4);

    localTabLayout.addTab(localTabLayout.newTab().setText(titles[0]));
    localTabLayout.addTab(localTabLayout.newTab().setText(titles[1]));
    localTabLayout.addTab(localTabLayout.newTab().setText(titles[2]));
    localTabLayout.addTab(localTabLayout.newTab().setText(titles[3]));
    localTabLayout.setupWithViewPager(localVp);
  }

  private void initEvent() {

  }

  private void initView() {
    localTabLayout = findViewById(R.id.local_tl);
    localVp = findViewById(R.id.local_vp);
    dynamicAddView(localTabLayout, "tabLayoutIndicator", R.color.themeColor);
    dynamicAddView(localTabLayout, "tabLayoutTextColor", R.color.main_text_color);
  }

  /**
   * 通用的标题逻辑
   */
  private void initHead() {
    headBack = findViewById(R.id.head_back);
    headTitle = findViewById(R.id.head_title);
    headRight = findViewById(R.id.head_right);
    headBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    headTitle.setText("本地音乐");

    headRight.setText("管理");
    headRight.setVisibility(View.GONE);
    headRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ToastUtil.startShort(baseAt, "管理");
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

}
