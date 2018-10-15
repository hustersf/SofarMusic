package com.sf.demo.picker.text;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.sf.demo.R;
import com.sf.demo.picker.city.WheelRecyclerView;
import com.sf.utility.DeviceUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sufan on 17/6/20.
 * mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
 * mPickerWindow.setFocusable(true);
 * 以上两句保证了点击popwindow之外的地方，可以使得pop消失
 */

public class TextPicker implements PopupWindow.OnDismissListener, View.OnClickListener {

    private OnTextSelectListener mOnTextSelectListener;

    private PopupWindow mPickerWindow;

    private View mParent;
    private View mPickerView;

    private WheelRecyclerView mTextWheel;

    private Activity mContext;

    private List<String> mDatas;

    public TextPicker(Activity context, View parent){
        mContext = context;
        mParent = parent;

        //初始化选择器
        mPickerView = LayoutInflater.from(context).inflate(R.layout.layout_text_picker, null);
        mTextWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_text);
        mPickerView.findViewById(R.id.cancel_tv).setOnClickListener(this);
        mPickerView.findViewById(R.id.confirm_tv).setOnClickListener(this);

        mPickerWindow = new PopupWindow(mPickerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPickerWindow.setFocusable(true);
        mPickerWindow.setAnimationStyle(R.style.CityPickerAnim);
        mPickerWindow.setOnDismissListener(this);

        initData();
    }

    private void initData(){
        String[] texts={"一天存款","七天通知","哈哈","hjdhdjgsdgjadgagdgadgajdgaj"};
        mDatas= Arrays.asList(texts);

        mTextWheel.setDividerWidth(DeviceUtil.getMetricsWidth(mContext)/2);
        mTextWheel.setData(mDatas);
    }

    /**
     * 弹出Window时使背景变暗
     *
     * @param alpha
     */
    private void backgroundAlpha(float alpha) {
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }




    public TextPicker setOnTextSelectListener(OnTextSelectListener listener) {
        mOnTextSelectListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_tv) {
            mPickerWindow.dismiss();

        } else if (i == R.id.confirm_tv) {
            if (mOnTextSelectListener != null) {
                String text = mDatas.get(mTextWheel.getSelected());
                mOnTextSelectListener.onTextSelect(text);
                mPickerWindow.dismiss();
            }

        }
    }

    public void show() {
        backgroundAlpha(0.5f);
        if(mParent!=null) {
            mPickerWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        }else{
            mPickerWindow.showAtLocation(mPickerView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }

    public interface OnTextSelectListener {
        void onTextSelect(String text);
    }
}
