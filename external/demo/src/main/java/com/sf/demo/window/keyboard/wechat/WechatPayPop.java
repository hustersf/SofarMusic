package com.sf.demo.window.keyboard.wechat;

import java.util.List;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.demo.window.keyboard.KeyboardAdapter;
import com.sf.demo.window.keyboard.PasswordView;
import com.sf.demo.window.keyboard.VirtualKeyboardView;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/6/29.
 * 仿微信支付密码窗
 */

public class WechatPayPop extends PopupWindow {
    private Activity mContext;
    private View mView;
    private ImageView xx_iv;

    //密码格子
    private PasswordView passView;
    private TextView[] tvList;
    private ImageView[] imgList;
    private int currentIndex = -1;    //用于记录当前输入密码格位置
    private String mPassStr;     //密码

    //键盘
    private VirtualKeyboardView keyboardView;
    private List<String> valueList;

    private int mKeyboardHeight;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.startShort(mContext, "支付密码：" + mPassStr);
            dismiss();
        }
    };

    public WechatPayPop(Activity context) {
        super(context);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_wechatpay, null);
        setContentView(mView);

        initView();
        initData();
        initEvent();

        setAttribute();

    }

    private void initData() {
        valueList = keyboardView.getValues();

        tvList = passView.getTvList();
        imgList = passView.getImgList();
    }

    private void initView() {
        passView = (PasswordView) mView.findViewById(R.id.passView);
        passView.setCorner(DensityUtil.dp2px(mContext, 0));

        keyboardView = (VirtualKeyboardView) mView.findViewById(R.id.keyboardView);
        keyboardView.init(false);

        xx_iv = (ImageView) mView.findViewById(R.id.xx_iv);

        //设置键盘的高度为256dp  54*4+40  不知道为什么设置为wrap_content总是会充满整个屏幕
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        mKeyboardHeight = DensityUtil.dp2px(mContext, 54 * 4 + 40);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, mKeyboardHeight);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        keyboardView.setLayoutParams(lp);
    }

    private void initEvent() {

        xx_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        keyboardEvent();
        passViewEvent();

    }

    private void setAttribute() {
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        int height = DeviceUtil.getMetricsHeight(mContext);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.CityPickerAnim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x66000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new ColorDrawable());
    }


    public void show() {
        backgroundAlpha(0.5f);
        showAtLocation(mView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1.0f);
    }

    private void keyboardEvent() {
        keyboardView.getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  keyboardView.setVisibility(View.GONE);
                AnimDismiss();
            }
        });


        //键盘监听，改变PasswprdView的点的显示和隐藏
        keyboardView.getAdapter().setOnItemClickListener(new KeyboardAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                doPosition(position);
            }
        });
    }

    private void passViewEvent() {
        tvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {
                    mPassStr = "";
                    for (int i = 0; i < 6; i++) {
                        mPassStr += tvList[i].getText().toString();
                    }
                    mHandler.sendEmptyMessageDelayed(0, 500);
                }

            }
        });

        passView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   keyboardView.setVisibility(View.VISIBLE);
                AnimShow();
            }
        });
    }


    private void doPosition(int position) {
        if (position < 11 && position != 9) {
            if (currentIndex >= -1 && currentIndex < 5) {      //判断输入位置————要小心数组越界
                ++currentIndex;
                tvList[currentIndex].setText(valueList.get(position));

                tvList[currentIndex].setVisibility(View.INVISIBLE);
                imgList[currentIndex].setVisibility(View.VISIBLE);
            }
        } else {
            if (position == 11) {      //点击退格键
                if (currentIndex - 1 >= -1) {      //判断是否删除完毕————要小心数组越界

                    tvList[currentIndex].setText("");

                    tvList[currentIndex].setVisibility(View.VISIBLE);
                    imgList[currentIndex].setVisibility(View.INVISIBLE);

                    currentIndex--;
                }
            }
        }

    }

    private void backgroundAlpha(float alpha) {
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }


    private void AnimDismiss() {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(keyboardView, "translationY", 0f, mKeyboardHeight);
        oa1.start();
    }

    private void AnimShow() {
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(keyboardView, "translationY", mKeyboardHeight, 0f);
        oa1.start();
    }

}
