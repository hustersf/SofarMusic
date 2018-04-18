package com.sf.sofarmusic.demo.system;

import android.Manifest;
import android.os.Handler;
import android.os.Message;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.UIRootActivity;
import com.sf.sofarmusic.callback.PermissionsResultListener;
import com.sf.sofarmusic.demo.system.contact.PhoneUtil;
import com.sf.sofarmusic.demo.system.smscode.SmsReceiver;
import com.sf.sofarmusic.demo.window.alert.AlertUtil;
import com.sf.sofarmusic.util.ToastUtil;
import com.sf.sofarmusic.view.FlowTagList;

/**
 * Created by sufan on 17/7/27.
 */

public class SystemShowActivity extends UIRootActivity {

    private FlowTagList tag_fl;

    private String[] mTags = {"获取通讯录电话号码","截取短信验证码"};


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            super.handleMessage(msg);
            switch (msg.what){
                case Constant.SMS_RECEIVED:
                    AlertUtil.showCommonErrorDialog(baseAt,"验证码:"+msg.obj);
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
        head_title.setText("系统相关");
    }

    @Override
    public void initView() {
        tag_fl = (FlowTagList) findViewById(R.id.tag_fl);
        dynamicAddView(tag_fl, "tagColor", R.color.themeColor);
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

    private void doTag(String text, int position){
        if("获取通讯录电话号码".equals(text)){
            PhoneUtil.getPhoneNumber(this, new PhoneUtil.PhoneCallback() {
                @Override
                public void onPhone(String phone) {
                    AlertUtil.showCommonErrorDialog(baseAt,phone);
                }
            });

        }else if("截取短信验证码".equals(text)){

        }
    }

    private void showSMSCode(){
        String des="请求获取读取短信权限";
        String[] permissions = new String[]{Manifest.permission.READ_SMS};
        baseAt.requestPermissions(des, permissions, 100, new PermissionsResultListener() {
            @Override
            public void onPermissionGranted() {
                SmsReceiver.setHandler(mHandler);

            }

            @Override
            public void onPermissionDenied() {
                ToastUtil.startShort(baseAt,"权限拒绝，无法截取验证码");

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmsReceiver.removeHandler();
    }
}
