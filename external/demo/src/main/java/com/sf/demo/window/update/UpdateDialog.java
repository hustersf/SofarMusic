package com.sf.demo.window.update;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sf.demo.R;

/**
 * Created by sufan on 17/8/1.
 */

public class UpdateDialog extends Dialog {
    private Context mContext;
    private UpdateInfo updateInfo;

    private TextView title_tv, size_tv, update_tv, ignore_tv;
    private Button update_btn;
    private HorizontalProgress progress;
    private ImageView close_iv;
    private ScrollView info_sl;

    private UpdateListener mListener;

    public UpdateDialog(Context context, UpdateInfo updateInfo, UpdateListener listener) {
        super(context);
        mContext = context;
        this.updateInfo = updateInfo;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_update);
        setAttribute();
        initView();
        initData();
        initEvent();

    }


    private void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        size_tv = (TextView) findViewById(R.id.size_tv);
        update_tv = (TextView) findViewById(R.id.update_tv);
        ignore_tv = (TextView) findViewById(R.id.ignore_tv);
        update_btn = (Button) findViewById(R.id.update_btn);
        progress = (HorizontalProgress) findViewById(R.id.download_pb);
        close_iv = (ImageView) findViewById(R.id.close_iv);
        info_sl = (ScrollView) findViewById(R.id.info_sl);
    }

    private void initData() {
        title_tv.setText("是否升级到" + updateInfo.clientVersionName + "版本?");
        size_tv.setText("新版本大小:" + updateInfo.apkSize);
        update_tv.setText(updateInfo.hintMessage);
        if ("1".equals(updateInfo.forceUpdate)) {
            setCancelable(false);
            ignore_tv.setVisibility(View.GONE);
        } else {
            setCancelable(true);
            ignore_tv.setVisibility(View.VISIBLE);
        }

    }

    private void initEvent() {
        if (mListener != null) {
            update_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update_btn.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    ignore_tv.setVisibility(View.GONE);
                    info_sl.fullScroll(ScrollView.FOCUS_DOWN);
                    mListener.onUpdate();
                }
            });
        }

        ignore_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(updateInfo.forceUpdate)) {
                    dismiss();
                    ((Activity) mContext).finish();
                } else {
                    dismiss();
                }
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

    public interface UpdateListener {
        void onUpdate();
    }
}
