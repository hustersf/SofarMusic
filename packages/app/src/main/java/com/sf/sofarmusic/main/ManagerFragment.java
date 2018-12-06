package com.sf.sofarmusic.main;

import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.base.LazyLoadBaseFragment;
import com.sf.base.permission.PermissionUtil;
import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.local.LocalActivity;
import com.sf.sofarmusic.local.MusicLoader;


/**
 * Created by sufan on 1/11/8.
 */

public class ManagerFragment extends LazyLoadBaseFragment
    implements
      SwipeRefreshLayout.OnRefreshListener,
      View.OnClickListener {

  private View view;
  private SwipeRefreshLayout manager_srf;

  private RelativeLayout local_rl, down_rl;
  private TextView local_icon_tv, down_icon_tv;
  private TextView local_count_tv, down_count_tv;

  private int i; // 本地音乐数量
  private int j; // 下载音乐数量

  // 6.0权限申请
  private final int READ_EXTERNAL_CODE = 100; // 读取权限


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_manager, container, false);
    return view;
  }

  @Override
  protected void initData() {
    LoadMusic();
  }


  private void LoadMusic() {
    manager_srf.setRefreshing(false);
    String des = "读取存储卡权限被禁止，我们需要该权限读取本地音乐文件，否则无法使用该功能";
    String content = "读取权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
    PermissionUtil
        .requestPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, des, content)
        .subscribe(permission -> {
          if (permission.granted) {
            getLoaclMusic();
          }
        });
  }

  private void getLoaclMusic() {
    manager_srf.post(new Runnable() {
      @Override
      public void run() {
        manager_srf.setRefreshing(true);
        MusicLoader.getInstance().LoadLocalMusicList(activity, new MusicLoader.LoadCallback() {
          @Override
          public void onLoad(Object obj) {
            Constant.sLocalList = (List<PlayItem>) obj;
            i = Constant.sLocalList.size();
            local_count_tv.setText("(" + i + ")");
            manager_srf.setRefreshing(false);
          }
        });
      }
    });

  }


  @Override
  protected void initView() {
    manager_srf = (SwipeRefreshLayout) view.findViewById(R.id.manager_srf);

    int c1 = getResources().getColor(R.color.white);
    manager_srf.setProgressBackgroundColorSchemeColor(c1); // 设置圆圈颜色
    int c2 = getResources().getColor(R.color.colorPrimary);
    manager_srf.setColorSchemeColors(c2); // 设置进度调颜色
    dynamicAddView(manager_srf, "swipCircleColor", R.color.themeColor);

    local_rl = (RelativeLayout) view.findViewById(R.id.local_rl);
    down_rl = (RelativeLayout) view.findViewById(R.id.down_rl);

    local_count_tv = (TextView) view.findViewById(R.id.local_count_tv);
    down_count_tv = (TextView) view.findViewById(R.id.down_count_tv);

    local_icon_tv = (TextView) view.findViewById(R.id.local_icon_tv);
    down_icon_tv = (TextView) view.findViewById(R.id.down_icon_tv);

    Typeface iconfont = FontUtil.setFont(activity);
    local_icon_tv.setTypeface(iconfont);
    down_icon_tv.setTypeface(iconfont);
  }

  @Override
  protected void initEvent() {
    manager_srf.setOnRefreshListener(this);

    local_rl.setOnClickListener(this);
    down_rl.setOnClickListener(this);

  }


  @Override
  public void onRefresh() {
    LoadMusic();
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.local_rl:
        Intent local = new Intent(getActivity(), LocalActivity.class);
        local.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(local);
        break;
      case R.id.down_rl:
        // Intent download = new Intent(getActivity(), DownLoadActivity.class);
        // download.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        // startActivity(download);
        break;
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }


}
