package com.sf.base.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;


/**
 * Created by sufan on 16/11/21.
 * 弹出高度为设置的高度(dialogheight)的一半
 */

public class MyBottomSheetDialog extends BottomSheetDialog {

    private Context mContext;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Window mWindow;

    public MyBottomSheetDialog(@NonNull Context context) {
        super(context);
        mContext=context;
        mWindow=getWindow();
    }

    public MyBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        mContext=context;
        mWindow=getWindow();
    }

    protected MyBottomSheetDialog(@NonNull Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
        mWindow=getWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //解决使用BottomSheetDialog时，状态栏变黑的问题，并固定了最大的高度（dialogHeight为最大高度）
        int screenHeight = DeviceUtil.getMetricsHeight(mContext);
        int statusBarHeight = DeviceUtil.getStatusBarHeight(mContext);
        int dialogHeight = DensityUtil.dp2px(mContext,400);
      //  int dialogHeight = screenHeight - statusBarHeight;
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        //设置弹出高度,如果不设置弹出高度，高度默认为dialogHeight的一半
        setPeekHeight(dialogHeight);
    }

    private void setPeekHeight(int height) {
        if(getBottomSheetBehavior()!=null){
            mBottomSheetBehavior.setPeekHeight(height);
        }
    }


    private BottomSheetBehavior getBottomSheetBehavior() {
        if (mBottomSheetBehavior != null) {
            return mBottomSheetBehavior;
        }

        View view = mWindow.findViewById(android.support.design.R.id.design_bottom_sheet);
        // setContentView() 没有调用
        if (view == null) {
            return null;
        }
        mBottomSheetBehavior = BottomSheetBehavior.from(view);
        return mBottomSheetBehavior;
    }
}
