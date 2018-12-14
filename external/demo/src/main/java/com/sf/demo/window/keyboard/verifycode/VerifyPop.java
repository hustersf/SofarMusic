package com.sf.demo.window.keyboard.verifycode;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.demo.window.keyboard.KeyboardAdapter;
import com.sf.demo.window.keyboard.VirtualKeyboardView;
import com.sf.utility.DensityUtil;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/6/29.
 * EditText+虚拟键盘
 * 点击EditText时，屏蔽系统键盘，而弹出自定义键盘
 */

public class VerifyPop extends PopupWindow {
    private Activity mContext;
    private View mView;
    private ImageView xx_iv;
    private EditText verify_et;
    private Button verify_btn,confirm_btn;
    private TextView hint_tv;
    private SmsTimer mSmsTimer;

    //键盘
    private VirtualKeyboardView keyboardView;
    private List<String> valueList;

    private int mKeyboardHeight;


    public VerifyPop(Activity context) {
        super(context);
        mContext = context;

        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pop_verify, null);
        setContentView(mView);

        initView();
        initData();
        initEvent();

        setAttribute();
    }

    private void initData() {
        valueList = keyboardView.getValues();

    }

    private void initView() {

        xx_iv = (ImageView) mView.findViewById(R.id.xx_iv);
        verify_et = (EditText) mView.findViewById(R.id.verify_et);
        verify_btn = (Button) mView.findViewById(R.id.verify_btn);
        confirm_btn = (Button) mView.findViewById(R.id.confirm_btn);
        hint_tv = (TextView) mView.findViewById(R.id.hint_tv);

        keyboardView = (VirtualKeyboardView) mView.findViewById(R.id.keyboardView);
        keyboardView.init(false);
        //设置键盘的高度为256dp  54*4+40  不知道为什么设置为wrap_content总是会充满整个屏幕
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        mKeyboardHeight = DensityUtil.dp2px(mContext, 54 * 4 + 40);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, mKeyboardHeight);
        keyboardView.setLayoutParams(lp);
        keyboardView.getLayoutBack().setVisibility(View.GONE);

        //设置不调用系统键盘
        skipSystemKeyboard();

        //按钮置灰
        confirm_btn.setBackgroundResource(R.drawable.button_gray_bg);
        confirm_btn.setEnabled(false);
        mSmsTimer =new SmsTimer(60*1000,1000,verify_btn,mContext);
        hint_tv.setText("请点击验证码以向你的手机发送验证码");
    }

    private void skipSystemKeyboard(){
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            verify_et.setInputType(InputType.TYPE_NULL);
        } else {
            mContext.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(verify_et, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initEvent() {

        xx_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }        });

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmsTimer.start();
                hint_tv.setText("以向尾号1111发送验证码");
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifyStr=verify_et.getText().toString().trim();
                if(verifyStr.length()<6){
                    ToastUtil.startShort(mContext,"验证码必须为6位");
                }else {
                    dismiss();
                    ToastUtil.startShort(mContext,"验证码:"+verifyStr);
                }
            }
        });

        keyboardEvent();
        inputEvent();
    }

    private void setAttribute() {
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private void inputEvent(){
        verify_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    confirm_btn.setBackgroundResource(R.drawable.button_red_bg);
                    confirm_btn.setEnabled(true);
                }else {
                    confirm_btn.setBackgroundResource(R.drawable.button_gray_bg);
                    confirm_btn.setEnabled(false);
                }
            }
        });
    }

    private void keyboardEvent() {

        //键盘监听，将值送入EditText
        keyboardView.getAdapter().setOnItemClickListener(new KeyboardAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                doPosition(position);
            }
        });
    }


    private void doPosition(int position) {
        if (position < 11 && position != 9) {
            String number = verify_et.getText().toString().trim();
            number = number + valueList.get(position);

            verify_et.setText(number);

            Editable ea = verify_et.getText();
            verify_et.setSelection(ea.length());
        } else {
            if (position == 9) {      //点击退格键
                String number = verify_et.getText().toString().trim();
                if (!number.contains(".")) {
                    number = number + valueList.get(position);
                    verify_et.setText(number);

                    Editable ea = verify_et.getText();
                    verify_et.setSelection(ea.length());
                }
            }

            if (position == 11) {      //点击退格键
                String number = verify_et.getText().toString().trim();
                if (number.length() > 0) {
                    number = number.substring(0, number.length() - 1);
                    verify_et.setText(number);

                    Editable ea = verify_et.getText();
                    verify_et.setSelection(ea.length());
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
