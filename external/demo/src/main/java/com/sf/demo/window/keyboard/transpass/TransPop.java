package com.sf.demo.window.keyboard.transpass;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.demo.window.keyboard.PasswordView;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.KeyBoardUtil;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/6/29.
 * 密码格子＋系统键盘（或者第三方的加密键盘）
 * 虚拟键盘没做加密
 */

public class TransPop extends PopupWindow {

    private Activity mContext;
    private View mView;
    private ImageView xx_iv;

    private EditText hide_et;

    //密码格子
    private PasswordView passView;
    private TextView[] tvList;
    private ImageView[] imgList;
    private int currentIndex = -1;    //用于记录当前输入密码格位置
    private String mPassStr;     //密码

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.startShort(mContext, "支付密码：" + hide_et.getText().toString());
            KeyBoardUtil.hideKeyBoard(mContext,hide_et);
            dismiss();
        }
    };

    public TransPop(Activity context) {
        super(context);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_trans, null);
        setContentView(mView);

        initView();
        initData();
        initEvent();

        setAttribute();

    }

    private void initData() {

        tvList = passView.getTvList();
        imgList = passView.getImgList();
    }

    private void initView() {
        passView = (PasswordView) mView.findViewById(R.id.passView);
        passView.setCorner(DensityUtil.dp2px(mContext, 0));

        xx_iv = (ImageView) mView.findViewById(R.id.xx_iv);

        hide_et = (EditText) mView.findViewById(R.id.hide_et);

        KeyBoardUtil.openKeyBoard(mContext,hide_et);

    }

    private void initEvent() {

        xx_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.hideKeyBoard(mContext,hide_et);
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
        this.setBackgroundDrawable(dw);
    }


    public void show() {
        //    backgroundAlpha(0.5f);
        showAtLocation(mView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //  backgroundAlpha(1.0f);
    }

    private void keyboardEvent() {

        hide_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               int length=s.toString().length();

                   for(int i=length;i<6;i++){
                       tvList[i].setText("");
                       tvList[i].setVisibility(View.VISIBLE);
                       imgList[i].setVisibility(View.GONE);
                   }

                   for(int i=0;i<length;i++){
                       tvList[i].setVisibility(View.GONE);
                       imgList[i].setVisibility(View.VISIBLE);
                   }

            }

            @Override
            public void afterTextChanged(Editable s) {
                 if(s.toString().length()==6){
                     mHandler.sendEmptyMessageDelayed(0,500);
                 }
            }
        });
    }

    private void passViewEvent() {


        passView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtil.openKeyBoard(mContext,hide_et);

            }
        });
    }


    private void doPosition(int position) {
        if (position < 11 && position != 9) {
            if (currentIndex >= -1 && currentIndex < 5) {      //判断输入位置————要小心数组越界
                ++currentIndex;
             //   tvList[currentIndex].setText(valueList.get(position));

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



}
