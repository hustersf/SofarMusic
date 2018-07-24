package com.sf.demo.window.download;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sf.demo.R;


/**
 * Created by sufan on 17/8/1.
 */

public class DownloadDialog extends Dialog {
    private Context mContext;

    private TextView cancel_tv;
    private CerProgress progress;


    public DownloadDialog(Context context) {
        super(context);
        mContext = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_download);
        setAttribute();
        initView();
        initData();
        initEvent();

    }


    private void initView() {
        cancel_tv=(TextView)findViewById(R.id.cancel_tv);
        progress=(CerProgress)findViewById(R.id.progress);

    }

    private void initData() {


    }

    private void initEvent() {
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }


    private void setAttribute() {
        setCanceledOnTouchOutside(false);
        // 设置SelectPicPopupWindow弹出窗体的背景
        getWindow().setBackgroundDrawable(new ColorDrawable());

        //去掉dialog上的蓝色线（部分手机有）
        int dividerID = mContext.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(dividerID);
        if (divider != null)
            divider.setBackgroundColor(Color.TRANSPARENT);

    }

    public void setProgress(int percent) {
        progress.setProgress(percent);
    }

}
