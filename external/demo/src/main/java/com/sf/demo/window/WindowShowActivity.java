package com.sf.demo.window;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.data.DemoData;
import com.sf.demo.window.alert.AlertUtil;
import com.sf.demo.window.alert.dialog.LoadingDialog;
import com.sf.demo.window.download.DownloadManager;
import com.sf.demo.window.keyboard.alipay.AlipayPop;
import com.sf.demo.window.keyboard.transpass.TransPop;
import com.sf.demo.window.keyboard.verifycode.VerifyPop;
import com.sf.demo.window.keyboard.wechat.WechatPayPop;
import com.sf.demo.window.pop.PopUtil;
import com.sf.demo.window.update.AppUpdateManager;
import com.sf.utility.ToastUtil;
import com.sf.utility.notification.NotifyManager;
import com.sf.widget.flowlayout.FlowTagList;
import com.sf.widget.popwindow.MenuPopwindow;

/**
 * Created by sufan on 17/6/26.
 */

public class WindowShowActivity extends UIRootActivity {

  private FlowTagList tag_fl;

  private String[] mTags = {"标题栏右侧弹窗", "MD系统弹窗", "一个按钮", "两个按钮",
      "IOS一个按钮", "IOS两个按钮", "IOS底部弹窗", "加载框", "支付宝密码窗", "微信支付密码窗",
      "密码格+系统键盘", "系统输入框+虚拟键盘", "系统通知栏", "客户端更新", "下载进度框"};

  private Handler mHandler;


  @Override
  protected int getLayoutId() {
    return R.layout.activity_demo_show;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("弹窗效果集合");
    mHeadRightTv.setVisibility(View.VISIBLE);
    mHeadRightTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MenuPopwindow popwindow = new MenuPopwindow(WindowShowActivity.this);
        popwindow.showAsCoverAnchorDropDown(mHeadTitleTv);
      }
    });
  }

  @Override
  public void initView() {
    tag_fl = (FlowTagList) findViewById(R.id.tag_fl);
    dynamicAddView(tag_fl, "tagColor", R.color.themeColor);
  }

  @Override
  public void initData() {
    tag_fl.setTags(mTags);
    mHandler = new Handler(Looper.getMainLooper());
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

  }

  private void doTag(String text, int position) {
    if ("标题栏右侧弹窗".equals(text)) {
      PopUtil.showPopList(this, DemoData.getPopList());
    } else if ("MD系统弹窗".equals(text)) {
      AlertUtil.showMaterialDialog(this, "我是弹窗内容", "取消", "确定", new AlertUtil.AlertCallback() {
        @Override
        public void onText(String str) {
          ToastUtil.startShort(baseAt, str);
        }
      });
    } else if ("一个按钮".equals(text)) {
      AlertUtil.showOneBtnDialog(this, "我有一个按钮", "确定", new AlertUtil.AlertCallback() {
        @Override
        public void onText(String str) {
          ToastUtil.startShort(baseAt, str);
        }
      });

    } else if ("两个按钮".equals(text)) {
      AlertUtil.showTwoBtnDialog(this, "我有两个按钮", "取消", "确定", new AlertUtil.AlertCallback() {
        @Override
        public void onText(String str) {
          ToastUtil.startShort(baseAt, str);
        }
      });
    } else if ("IOS一个按钮".equals(text)) {
      mHandler.postDelayed(new Runnable() {
        @Override
        public void run() {
          AlertUtil.showIOSOneBtn(baseAt, "标题", "我有一个按钮", "我知道了", new AlertUtil.AlertCallback() {
            @Override
            public void onText(String str) {
              ToastUtil.startShort(baseAt, str);
            }
          });
        }
      }, 0);

    } else if ("IOS两个按钮".equals(text)) {
      AlertUtil.showIOSTwoBtn(this, "标题", "我有两个按钮", "取消", "确认", new AlertUtil.AlertCallback() {
        @Override
        public void onText(String str) {
          ToastUtil.startShort(baseAt, str);
        }
      });
    } else if ("IOS底部弹窗".equals(text)) {
      PopUtil.showBottomPop(this, DemoData.getTextList(), new PopUtil.PopCallback() {
        @Override
        public void onText(String str) {
          ToastUtil.startShort(baseAt, str);
        }
      });
    } else if ("加载框".equals(text)) {
      Dialog dialog =
          LoadingDialog.createLoadingDialog(this, 5 * 1000, new LoadingDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(Dialog dialog) {
              ToastUtil.startShort(baseAt, "超时了");
            }
          });
      dialog.show();
    } else if ("支付宝密码窗".equals(text)) {
      AlipayPop alipayPop = new AlipayPop(this);
      alipayPop.show();
    } else if ("微信支付密码窗".equals(text)) {
      WechatPayPop wechatPayPop = new WechatPayPop(this);
      wechatPayPop.show();
    } else if ("密码格+系统键盘".equals(text)) {
      TransPop transPop = new TransPop(this);
      transPop.show();
    } else if ("系统输入框+虚拟键盘".equals(text)) {
      VerifyPop verifyPop = new VerifyPop(this);
      verifyPop.show();
    } else if ("系统通知栏".equals(text)) {
      NotifyManager manager = new NotifyManager(this);
      manager.sendNotifyMsg(NotifyManager.ChannelType.NEWS, "消息标题", "消息内容");
    } else if ("客户端更新".equals(text)) {
      AppUpdateManager.getInstance(this).checkUpdate(new AppUpdateManager.ICheckUpdateResult() {
        @Override
        public void success() {
          AlertUtil.showCommonErrorDialog(baseAt, "无需更新");
        }

        @Override
        public void fail(String message) {

          AlertUtil.showCommonErrorDialog(baseAt, message);
        }
      });
    } else if ("下载进度框".equals(text)) {

      DownloadManager.getInstance(this).start(new DownloadManager.IDownloadResult() {
        @Override
        public void success() {

        }

        @Override
        public void fail(String message) {

        }
      });

    }
  }
}
