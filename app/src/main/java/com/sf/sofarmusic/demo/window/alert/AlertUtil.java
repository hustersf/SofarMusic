package com.sf.sofarmusic.demo.window.alert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.util.DensityUtil;
import com.sf.sofarmusic.util.DeviceUtil;

import java.util.logging.Handler;

/**
 * Created by sufan on 17/6/26.
 * Dialog 和 AlertDialog
 * AlertDialog导致布局背景图片不起作用，且弹不出键盘
 */

public class AlertUtil {

    public static void showMaterialDialog(Context context, String content, final String cancel, final String confirm, final AlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("我是标题栏");
        builder.setMessage(content);
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.onText(cancel);
            }
        });

        builder.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.onText(confirm);
            }
        });

        Activity ac = (Activity) context;
        if (ac == null || ac.isDestroyed() || ac.isFinishing()) {
            return;
        }
        builder.show();
    }


    //包含一个按钮
    public static void showOneBtnDialog(Context context, String content, final String confirm, final AlertCallback callback) {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true); // 点击dlg之外的区域dlg是否消失


        Activity ac = (Activity) context;
        if (ac == null || ac.isDestroyed() || ac.isFinishing()) {
            return;
        }
        dlg.show();


        Window window = dlg.getWindow();
        window.setContentView(R.layout.layout_dialog);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = DeviceUtil.getMetricsWidth(context) / 20 * 18;
        dlg.getWindow().setAttributes(params);

        TextView content_tv = (TextView) window.findViewById(R.id.content_tv);
        content_tv.setText(content);

        TextView cancel_tv = (TextView) window.findViewById(R.id.cancel_tv);
        TextView confirm_tv = (TextView) window.findViewById(R.id.confirm_tv);
        cancel_tv.setVisibility(View.GONE);
        confirm_tv.setText(confirm);


        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                if (callback != null) {
                    callback.onText(confirm);
                }
            }
        });


    }


    //包含两个按钮
    public static void showTwoBtnDialog(Context context, String content, final String cancel, final String confirm, final AlertCallback callback) {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true); // 点击dlg之外的区域dlg是否消失

        Activity ac = (Activity) context;
        if (ac == null || ac.isDestroyed() || ac.isFinishing()) {
            return;
        }
        dlg.show();


        Window window = dlg.getWindow();
        window.setContentView(R.layout.layout_dialog);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = DeviceUtil.getMetricsWidth(context) / 20 * 18;
        dlg.getWindow().setAttributes(params);

        TextView content_tv = (TextView) window.findViewById(R.id.content_tv);
        content_tv.setText(content);

        TextView cancel_tv = (TextView) window.findViewById(R.id.cancel_tv);
        TextView confirm_tv = (TextView) window.findViewById(R.id.confirm_tv);
        cancel_tv.setText(cancel);
        confirm_tv.setText(confirm);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                if (callback != null) {
                    callback.onText(cancel);
                }
            }
        });

        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                if (callback != null) {
                    callback.onText(confirm);
                }
            }
        });

    }


    //IOS风格的一个按钮
    public static void showIOSOneBtn(Context context, String title, String content, final String confirm, final AlertCallback callback) {
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);// 去掉背景框
        dlg.setCancelable(true); // 点击dlg之外的区域dlg是否消失


        Activity ac = (Activity) context;
        if (ac == null || ac.isDestroyed() || ac.isFinishing()) {
            return;
        }
        dlg.show();


        //去掉dialog上的蓝色线（部分手机有）
        int dividerID = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dlg.findViewById(dividerID);
        if (divider != null)
            divider.setBackgroundColor(Color.TRANSPARENT);


        Window window = dlg.getWindow();
        window.setContentView(R.layout.layout_dialog_ios_one);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = DensityUtil.dp2px(context, 270);
        dlg.getWindow().setAttributes(params);

        TextView title_tv = (TextView) window.findViewById(R.id.title_tv);
        title_tv.setText(title);

        TextView content_tv = (TextView) window.findViewById(R.id.content_tv);
        content_tv.setText(content);

        TextView confirm_tv = (TextView) window.findViewById(R.id.confirm_tv);
        confirm_tv.setText(confirm);

        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (callback != null) {
                    callback.onText(confirm);
                }
            }
        });

    }

    /**
     * 使用AlertDialog，布局文件中的圆角背景图片不起作用
     * 用的是md风格对话框的背景
     */
    public static void showIOSTwoBtn(Context context, String title, String content, final String cancel, final String confirm, final AlertCallback callback) {
        //  final AlertDialog dlg = new AlertDialog.Builder(context).create();
        final Dialog dlg = new Dialog(context);
        dlg.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);// 去掉背景框
        dlg.setCancelable(true); // 点击dlg之外的区域dlg是否消失
        Activity ac = (Activity) context;
        if (ac == null || ac.isDestroyed() || ac.isFinishing()) {
            return;
        }
        dlg.show();


        //去掉dialog上的蓝色线（部分手机有）
        int dividerID = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dlg.findViewById(dividerID);
        if (divider != null)
            divider.setBackgroundColor(Color.TRANSPARENT);


        Window window = dlg.getWindow();
        window.setContentView(R.layout.layout_dialog_ios_two);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = DensityUtil.dp2px(context, 270);
        dlg.getWindow().setAttributes(params);


        TextView title_tv = (TextView) window.findViewById(R.id.title_tv);
        title_tv.setText(title);

        TextView content_tv = (TextView) window.findViewById(R.id.content_tv);
        content_tv.setText(content);

        TextView confirm_tv = (TextView) window.findViewById(R.id.confirm_tv);
        confirm_tv.setText(confirm);

        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (callback != null) {
                    callback.onText(confirm);
                }
            }
        });

        TextView cancel_tv = (TextView) window.findViewById(R.id.cancel_tv);
        cancel_tv.setText(cancel);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                if (callback != null) {
                    callback.onText(cancel);
                }
            }
        });

    }

    public static void showCommonErrorDialog(Context context, String content) {
        showOneBtnDialog(context, content, "我知道了", null);
    }


    public interface AlertCallback {
        void onText(String str);
    }
}
