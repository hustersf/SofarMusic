package com.sf.demo.system;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.sf.base.UIRootActivity;
import com.sf.base.permission.PermissionUtil;
import com.sf.demo.Constant;
import com.sf.demo.R;
import com.sf.demo.system.calendar.CalendarReminderUtil;
import com.sf.demo.system.contact.ContactUtil;
import com.sf.demo.system.contact.model.ContactInfo;
import com.sf.demo.system.notification.NotifyContentActivity;
import com.sf.demo.system.sensor.StepCountSensorManagerHelper;
import com.sf.demo.system.smscode.SmsReceiver;
import com.sf.demo.util.SheetDialogUtil;
import com.sf.demo.window.alert.AlertUtil;
import com.sf.utility.CollectionUtil;
import com.sf.utility.ToastUtil;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/7/27.
 */

public class SystemShowActivity extends UIRootActivity {
  private static final String TAG = "SystemShowActivity";

  private FlowTagList tag_fl;

  private String[] mTags = {"跳转至系统通讯录", "获取通讯录信息", "截取短信验证码", "获取通知栏信息", "获取系统计步器步数", "向系统日历添加事件"};

  private TextView stepCountTv;


  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      super.handleMessage(msg);
      switch (msg.what) {
        case Constant.SMS_RECEIVED:
          AlertUtil.showCommonErrorDialog(baseAt, "验证码:" + msg.obj);
          break;
      }
    }
  };



  @Override
  protected int getLayoutId() {
    return R.layout.activity_demo_show;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("系统相关");
  }

  @Override
  public void initView() {
    tag_fl = findViewById(R.id.tag_fl);
    dynamicAddView(tag_fl, "tagColor", R.color.themeColor);

    stepCountTv = findViewById(R.id.tv_step_count);
  }

  @Override
  public void initData() {
    tag_fl.setTags(mTags);

    showSMSCode();
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
    if (mTags[0].equals(text)) {
      ContactUtil.getPhoneNumber(this, phones -> {
        if (phones.size() == 0) {
          ToastUtil.startShort(baseAt, "此人无手机号码");
        } else if (phones.size() == 1) {
          AlertUtil.showCommonErrorDialog(baseAt, phones.get(0));
        } else {
          SheetDialogUtil.showPhoneList(baseAt, "电话号码列表", phones,
              new SheetDialogUtil.PhoneCallback() {
                @Override
                public void OnPhone(String phoneNum) {
                  AlertUtil.showCommonErrorDialog(baseAt, phoneNum);
                }
              });
        }
      });

    } else if (mTags[1].equals(text)) {
      String des = "获取电话号码需要读取通讯录权限";
      String content = "相关权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
      PermissionUtil.requestPermission(baseAt, Manifest.permission.READ_CONTACTS, des, content)
          .subscribe(permission -> {
            if (permission.granted) {
              ContactUtil.getContactsAsync(baseAt).subscribe(contactInfos -> {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < contactInfos.size(); i++) {
                  ContactInfo item = contactInfos.get(i);
                  sb.append(item.name);
                  sb.append("\t");
                  if (!CollectionUtil.isEmpty(item.phones)) {
                    for (String phone : item.phones) {
                      sb.append(phone);
                      sb.append("___");
                    }
                  }
                  sb.append("\n");
                }
                AlertUtil.showCommonErrorDialog(baseAt, sb.toString());
              });
            }
          });

    } else if (mTags[2].equals(text)) {

    } else if (mTags[3].equals(text)) {
      Intent intent = new Intent(this, NotifyContentActivity.class);
      startActivity(intent);
    } else if (mTags[4].equals(text)) {
      if (StepCountSensorManagerHelper.getInstance(this).isSupportStepCountSensor()) {
        stepCountTv
            .setText("今日步数：" + StepCountSensorManagerHelper.getInstance(this).getStepCount());
      } else {
        AlertUtil.showCommonErrorDialog(this, "此设备不支持计步传感器");
      }
    } else if (mTags[5].equals(text)) {
      String des = "需要日历权限添加事件提醒";
      String content = "相关权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
      String[] permissions =
          {Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR};
      PermissionUtil.requestPermissions(baseAt, permissions, des, content)
          .subscribe(permission -> {
            if (permission) {
              long beginTime = System.currentTimeMillis() + 10 * 60 * 1000;
              long endTime = beginTime + 1 * 60 * 60 * 1000;
              int remindMinute = 5;
              CalendarReminderUtil.addCalendarEventRemind(baseAt, "日历标题测试", "日历描述测试", beginTime,
                  endTime, remindMinute, new CalendarReminderUtil.onCalendarRemindListener() {
                    @Override
                    public void onFailed(int error_code) {
                      ToastUtil.startShort(baseAt, "日历插入失败：" + error_code);
                    }

                    @Override
                    public void onSuccess() {
                      ToastUtil.startShort(baseAt, "日历插入成功");
                    }
                  });
            }
          });
    }
  }

  private void showSMSCode() {
    String des = "请求获取读取短信权限";
    PermissionUtil.requestPermission(this, Manifest.permission.READ_SMS, des, "")
        .subscribe(permission -> {
          if (permission.granted) {
            SmsReceiver.setHandler(mHandler);
          } else {
            ToastUtil.startShort(baseAt, "权限拒绝，无法截取验证码");
          }
        });

  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StepCountSensorManagerHelper.getInstance(this).registerStepCountSensor();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    SmsReceiver.removeHandler();
    StepCountSensorManagerHelper.getInstance(this).unRegisterStepCountSensor();
  }
}
