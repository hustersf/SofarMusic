package com.sf.sofarmusic.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sf.base.BaseFragment;
import com.sf.base.permission.PermissionUtil;
import com.sf.base.util.FontUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.local.LocalActivity;
import com.sf.sofarmusic.local.MusicLoader;
import com.sf.sofarmusic.local.model.LocalSongDataHolder;


/**
 * Created by sufan on 1/11/8.
 */
public class ManagerFragment extends BaseFragment
    implements
      SwipeRefreshLayout.OnRefreshListener,
      View.OnClickListener {

  private SwipeRefreshLayout manager_srf;

  private RelativeLayout local_rl, down_rl;
  private TextView local_icon_tv, down_icon_tv;
  private TextView local_count_tv, down_count_tv;

  private int i; // 本地音乐数量
  private int j; // 下载音乐数量

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_manager, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    initEvent();
  }

  @Override
  protected void onFirstVisible() {
    super.onFirstVisible();
    initData();
  }

  private void initData() {
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
            getLocalMusic();
          }
        });
  }

  private void getLocalMusic() {
    manager_srf.post(new Runnable() {
      @Override
      public void run() {
        manager_srf.setRefreshing(true);
        MusicLoader.getInstance().loadLocalMusicListAsync(activity).subscribe(songs -> {
          LocalSongDataHolder.getInstance().setAllSongs(songs);
          i = songs.size();
          local_count_tv.setText("(" + i + ")");
          manager_srf.setRefreshing(false);
        });

      }
    });

  }


  protected void initView() {
    View view = getView();
    manager_srf = view.findViewById(R.id.manager_srf);

    int c1 = getResources().getColor(R.color.white);
    manager_srf.setProgressBackgroundColorSchemeColor(c1); // 设置圆圈颜色
    int c2 = getResources().getColor(R.color.colorPrimary);
    manager_srf.setColorSchemeColors(c2); // 设置进度调颜色
    dynamicAddView(manager_srf, "swipCircleColor", R.color.themeColor);

    local_rl = view.findViewById(R.id.local_rl);
    down_rl = view.findViewById(R.id.down_rl);

    local_count_tv = view.findViewById(R.id.local_count_tv);
    down_count_tv = view.findViewById(R.id.down_count_tv);

    local_icon_tv = view.findViewById(R.id.local_icon_tv);
    down_icon_tv = view.findViewById(R.id.down_icon_tv);

    Typeface iconfont = FontUtil.setFont(activity);
    local_icon_tv.setTypeface(iconfont);
    down_icon_tv.setTypeface(iconfont);
  }

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
