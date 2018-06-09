package com.sf.sofarmusic.menu.poweroff;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.util.DeviceUtil;

/**
 * Created by sufan on 17/10/4.
 */

public class PowerAlert {

    public static int min = 30;   //定时关闭的分钟数
    public static boolean hasTask = false;   //是否开启定时任务


    public static void showPowerOffDialog(Context context, final PowerCallback callback) {

        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.setCancelable(true); // 点击dlg之外的区域dlg是否消失
        dlg.show();

        Window window = dlg.getWindow();
        window.setContentView(R.layout.layout_dialog_power_off);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = DeviceUtil.getMetricsWidth(context) / 20 * 18;
        dlg.getWindow().setAttributes(params);

        final TextView time_tv = (TextView) window.findViewById(R.id.time_tv);
        TimeProgress time_pb = (TimeProgress) window.findViewById(R.id.time_pb);
        TextView cancel_tv = (TextView) window.findViewById(R.id.cancel_tv);
        final TextView confirm_tv = (TextView) window.findViewById(R.id.confirm_tv);

        time_tv.setText(min + "");
        time_pb.setProgress((int) (min * 1.0f / 120 * 100));

        if (hasTask) {
            confirm_tv.setText("停止计时");
        } else {
            confirm_tv.setText("开始计时");
        }


        time_pb.setOnProgressListener(new TimeProgress.OnProgressListener() {
            @Override
            public void onProgress(int progress) {
                min = (int) (120 * 1.0f * progress / 100);
                min = min / 5 * 5;
                time_tv.setText(min + "");
            }
        });

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                callback.onText("取消");
            }
        });

        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                callback.onText(confirm_tv.getText().toString().trim());

                if (min == 0) {
                    if(hasTask){
                        hasTask=!hasTask;
                    }
                } else {
                    hasTask = !hasTask;
                }
            }
        });
    }


    public interface PowerCallback {
        void onText(String str);
    }
}
