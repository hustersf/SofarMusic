package com.sf.sofarmusic.main;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.base.permission.PermissionUtil;
import com.sf.base.util.AppManager;
import com.sf.base.util.FontUtil;
import com.sf.demo.window.alert.AlertUtil;
import com.sf.libzxing.util.QRCodeUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.play.core.PlayerBaseActivity;
import com.sf.sofarmusic.data.LocalData;
import com.sf.sofarmusic.enity.MenuItem;
import com.sf.sofarmusic.menu.MenuAdapter;
import com.sf.sofarmusic.menu.poweroff.PowerAlert;
import com.sf.sofarmusic.menu.poweroff.PowerOffTask;
import com.sf.sofarmusic.skin.SkinActivity;
import com.sf.sofarmusic.util.SkinUtil;
import com.sf.utility.AppUtil;
import com.sf.utility.Base64Util;
import com.sf.utility.DeviceUtil;
import com.sf.utility.SharedPreUtil;
import com.sf.utility.ToastUtil;

public class MainActivity extends PlayerBaseActivity
    implements
      View.OnClickListener,
      ViewPager.OnPageChangeListener,
      MenuAdapter.OnItemClickListener {

  private DrawerLayout drawer;
  private Toolbar toolbar;
  private TextView manager_tv, music_tv, people_tv, search_tv;


  private RecyclerView menu_rv;
  private MenuAdapter mMenuAdapter;
  private List<MenuItem> mMenuList;

  private TextView day_night_icon_tv, set_icon_tv, exit_icon_tv;
  private TextView day_night_tv;
  private RelativeLayout day_night_rl, set_rl, exit_rl;

  private ViewPager main_vp;
  private OnlineFragment onlineFragment;
  private ManagerFragment managerFragment;
  private PeopleFragment peopleFragment;
  private MainFmAdapter mMainAdapter;
  private List<Fragment> mFmList;

  private int mScreenWidth;

  private BroadcastReceiver receiver;


  private PowerOffTask mTask; // 定时停止播放任务



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    initData();
    initEvent();
    setNotifyReceiver();
  }


  @Override
  protected void onStart() {
    super.onStart();
    updateSkinMode();
    mMenuAdapter.notifySkinName();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
  }


  private void initData() {

    // 设置Toolbar最左边的图标（菜单，返回键的切换）
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    // 设置字体
    Typeface iconfont = FontUtil.setFont(this);
    manager_tv.setTypeface(iconfont);
    music_tv.setTypeface(iconfont);
    people_tv.setTypeface(iconfont);
    search_tv.setTypeface(iconfont);
    manager_tv.setTextColor(Color.parseColor("#64FFFFFF"));
    people_tv.setTextColor(Color.parseColor("#64FFFFFF"));

    // 左侧menu下方几个按钮
    day_night_icon_tv.setTypeface(iconfont);
    set_icon_tv.setTypeface(iconfont);
    exit_icon_tv.setTypeface(iconfont);


    // 设置左侧menu的RecyclerView
    menu_rv.setLayoutManager((new LinearLayoutManager(this,
        LinearLayoutManager.VERTICAL, false)));
    mMenuList = LocalData.getMenuListData();
    mMenuAdapter = new MenuAdapter(this, mMenuList);
    menu_rv.setAdapter(mMenuAdapter);
    mMenuAdapter.setOnItemClickListener(this);

    // 初始化Fragment
    onlineFragment = new OnlineFragment();
    managerFragment = new ManagerFragment();
    peopleFragment = new PeopleFragment();
    mFmList = new ArrayList<>();
    mFmList.add(onlineFragment);
    mFmList.add(managerFragment);
    mFmList.add(peopleFragment);
    FragmentManager fm = getSupportFragmentManager();
    mMainAdapter = new MainFmAdapter(fm, mFmList);
    main_vp.setAdapter(mMainAdapter);
    main_vp.setCurrentItem(0);
    main_vp.setOffscreenPageLimit(3);

    mScreenWidth = DeviceUtil.getMetricsWidth(baseAt);
  }

  private void initEvent() {
    main_vp.addOnPageChangeListener(this);
    music_tv.setOnClickListener(this);
    manager_tv.setOnClickListener(this);
    people_tv.setOnClickListener(this);
    search_tv.setOnClickListener(this);

    day_night_rl.setOnClickListener(this);
    set_rl.setOnClickListener(this);
    exit_rl.setOnClickListener(this);



    // 监听抽屉
    drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
      @Override
      public void onDrawerSlide(View drawerView, float slideOffset) {

      }

      @Override
      public void onDrawerOpened(View drawerView) {
        // ObjectAnimator oa1 = ObjectAnimator.ofFloat(music_rl, "translationX", 0f, mScreenWidth);
        // oa1.start();
      }

      @Override
      public void onDrawerClosed(View drawerView) {
        // ObjectAnimator oa2 = ObjectAnimator.ofFloat(music_rl, "translationX", mScreenWidth, 0f);
        // oa2.start();
      }

      @Override
      public void onDrawerStateChanged(int newState) {

      }
    });
  }

  private void initView() {
    drawer = (DrawerLayout) findViewById(R.id.drawer);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

    manager_tv = (TextView) findViewById(R.id.manager_tv);
    music_tv = (TextView) findViewById(R.id.music_tv);
    people_tv = (TextView) findViewById(R.id.people_tv);
    search_tv = (TextView) findViewById(R.id.search_tv);

    main_vp = (ViewPager) findViewById(R.id.main_vp);

    menu_rv = (RecyclerView) findViewById(R.id.menu_rv);
    day_night_icon_tv = (TextView) findViewById(R.id.day_night_icon_tv);
    set_icon_tv = (TextView) findViewById(R.id.set_icon_tv);
    exit_icon_tv = (TextView) findViewById(R.id.exit_icon_tv);
    day_night_tv = (TextView) findViewById(R.id.day_night_tv);


    day_night_rl = (RelativeLayout) findViewById(R.id.day_night_rl);
    set_rl = (RelativeLayout) findViewById(R.id.set_rl);
    exit_rl = (RelativeLayout) findViewById(R.id.exit_rl);

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.music_tv:
        main_vp.setCurrentItem(0);
        break;
      case R.id.manager_tv:
        main_vp.setCurrentItem(1);
        break;
      case R.id.people_tv:
        main_vp.setCurrentItem(2);
        break;
      case R.id.search_tv:
        // Intent find = new Intent(baseAt, SkinActivity.class);
        // startActivity(find);
        break;
      case R.id.day_night_rl:
        changeSkinMode();
        break;
      case R.id.set_rl:
        showDeviceInfo();
        break;
      case R.id.exit_rl:
        playerHelper.stop();
        AppManager.getAppManager().AppExit(this);
        break;
    }

  }


  // 刷新日间夜间模式
  private void updateSkinMode() {
    SharedPreUtil sp = new SharedPreUtil(this);
    boolean isNight = sp.getToggleState("isNight");
    if (!isNight) {
      day_night_tv.setText("夜间模式");
      day_night_icon_tv.setText(getResources().getString(R.string.icon_moon));
    } else {
      day_night_tv.setText("日间模式");
      day_night_icon_tv.setText(getResources().getString(R.string.icon_sun));
    }
  }

  // 改变日间夜间模式
  private void changeSkinMode() {

    SharedPreUtil sp = new SharedPreUtil(this);
    String skinName = sp.getToggleString("skinName"); // 保存的皮肤的包名
    int color = sp.getToggleInt("themeColor");
    if ("日间模式".equals(day_night_tv.getText().toString())) {
      day_night_tv.setText("夜间模式");
      day_night_icon_tv.setText(getResources().getString(R.string.icon_moon));
      if ("".equals(skinName)) {
        // 默认主题
        SkinUtil.restoreDefaultTheme(this);
      } else if ("sp.skin".equals(skinName)) {
        SkinUtil.changeColorFromSp(this, color);
      } else {
        SkinUtil.changeSkin(this, skinName);
      }
    } else {
      day_night_tv.setText("日间模式");
      day_night_icon_tv.setText(getResources().getString(R.string.icon_sun));
      SkinUtil.changeSkin(this, "night.skin");
    }

  }

  @Override
  public void onBackPressed() {
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
      // 回到桌面
      // Intent intent = new Intent();
      // intent.setAction(Intent.ACTION_MAIN);
      // intent.addCategory(Intent.CATEGORY_HOME);
      // startActivity(intent);
      // overridePendingTransition(0, R.anim.zoom_exit);
    }
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    changeMenuIcon(position);
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  private void changeMenuIcon(int position) {
    switch (position) {
      case 0:
        music_tv.setTextColor(Color.parseColor("#FFFFFF"));
        manager_tv.setTextColor(Color.parseColor("#64FFFFFF"));
        people_tv.setTextColor(Color.parseColor("#64FFFFFF"));
        break;
      case 1:
        music_tv.setTextColor(Color.parseColor("#64FFFFFF"));
        manager_tv.setTextColor(Color.parseColor("#FFFFFF"));
        people_tv.setTextColor(Color.parseColor("#64FFFFFF"));
        break;
      case 2:
        music_tv.setTextColor(Color.parseColor("#64FFFFFF"));
        manager_tv.setTextColor(Color.parseColor("#64FFFFFF"));
        people_tv.setTextColor(Color.parseColor("#FFFFFF"));
        break;
    }

  }

  @Override
  public void onItemClick(String des) {

    if ("主题换肤".equals(des)) {
      Intent skin = new Intent(MainActivity.this, SkinActivity.class);
      startActivity(skin);
    } else if ("定时停止播放".equals(des)) {
      PowerAlert.showPowerOffDialog(this, new PowerAlert.PowerCallback() {
        @Override
        public void onText(String str) {
          if ("开始计时".equals(str)) {
            if (PowerAlert.min == 0) {
              ToastUtil.startShort(baseAt, "定时关闭已取消");
              return;
            }

            ToastUtil.startShort(baseAt, "将在" + PowerAlert.min + "分钟后停止播放");
            mTask = new PowerOffTask(PowerAlert.min);
            mTask.openTask(new PowerOffTask.TimeCallback() {
              @Override
              public void onTime(int minute) {
                if (minute == 0) {
                  playerHelper.stop();
                  AppManager.getAppManager().AppExit(baseAt);
                  return;
                }
                mMenuAdapter.notifyTime(minute);
              }
            });
          } else if ("停止计时".equals(str)) {
            ToastUtil.startShort(baseAt, "定时关闭已取消");
            mMenuAdapter.notifyTime(0);
            if (mTask != null) {
              mTask.closeTask();
            }
          }
        }
      });
    } else if ("扫一扫".equals(des)) {
      scan();
    } else {
      ToastUtil.startShort(baseAt, des);
    }

  }


  // 扫一扫
  private void scan() {
    String refusedHint = "相机权限被禁止，我们需要该权限打开相机，否则无法使用该功能";
    String settingHint = "相机权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
    PermissionUtil.requestPermission(this, Manifest.permission.CAMERA, refusedHint, settingHint)
        .subscribe(permission -> {
          if (permission.granted) {
            QRCodeUtil qrCodeUtil = new QRCodeUtil(baseAt);
            qrCodeUtil.DecodeQRCode(new QRCodeUtil.DecodeQRCodeResult() {
              @Override
              public void result(String result) {
                ToastUtil.startShort(baseAt, result);
              }
            });
          }
        });
  }

  private void showDeviceInfo() {
    String model = DeviceUtil.getModel();
    String serNum = DeviceUtil.getSerialNumber(this);
    String uuid = DeviceUtil.getUUid(this);
    String os = DeviceUtil.getOS();
    String ip = DeviceUtil.getIP(this);
    String netWorkType = DeviceUtil.getNetWorkType(this);
    String packageName = AppUtil.getPackageName(this);
    String versionName = AppUtil.getVersionName(this);
    String WHpx = DeviceUtil.getMetricsWidth(this) + "*" + DeviceUtil.getMetricsHeight(this);
    String dpiInfo =
        DeviceUtil.getMetricsDensityDpi(this) + "-" + DeviceUtil.getMetricsDensity(this);

    String enstr = Base64Util.encrypt(uuid);
    String destr = Base64Util.decrypt(enstr);

    String ss = "设备型号:" + model + "\n"
    // + "IMEI:" + iemi + "\n"
        + "SerialNumber:" + serNum + "\n"
        + "UUID:" + uuid + "\n"
        + "操作系统:" + os + "\n"
        + "IP地址:" + ip + "\n"
        + "网络类型:" + netWorkType + "\n"
        + "程序包名:" + packageName + "\n"
        + "程序版本号:" + versionName + "\n"
        + "base64加密:" + enstr + "\n"
        + "base64解密:" + destr + "\n"
        + "手机分辨率(w*h):" + WHpx + "\n"
        + "手机dpi:" + dpiInfo + "\n"
        + "手机是否Root:" + AppUtil.isRootSystem();



    AlertUtil.showOneBtnDialog(this, ss, "我知道了", null);
  }


  // 设置通知栏的广播
  private void setNotifyReceiver() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(Constant.NOTIFY_CLOSE);
    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constant.NOTIFY_CLOSE)) {
          AppManager.getAppManager().AppExit(baseAt);
        }
      }
    };
    registerReceiver(receiver, intentFilter);
  }

}
