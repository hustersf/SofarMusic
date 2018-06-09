package com.sf.sofarmusic.demo.window.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;

import com.sf.sofarmusic.util.AssetUtil;
import com.sf.sofarmusic.util.NetUtil;
import com.sf.sofarmusic.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Random;

import static com.sf.libnet.http.HttpConfig.context;

/**
 * Created by sufan on 17/8/1.
 * 客户端更新
 */

public class AppUpdateManager {
    private static AppUpdateManager INSTANCE = null;
    private Context mContext;
    private UpdateInfo updateInfo;
    private String mSavePath;

    private UpdateDialog dialog;

    private int progress = 0;

    public static AppUpdateManager getInstance(Context context) {
        //原本设计成单利模式,但是涉及到服务器切换,故修改原有设计,设计成多例模式
        INSTANCE = new AppUpdateManager(context);
        return INSTANCE;
    }

    private AppUpdateManager(Context context) {
        mContext = context;
    }

    public void checkUpdate(final ICheckUpdateResult result) {
        if (!NetUtil.isNetAvailable(mContext)) {
            // 网络关闭状态
            if (result != null) {
                result.fail("网络未开启,请检查网络！");
            }
            return;
        }

        //模拟网络请求更新的数据
        String updateStr = AssetUtil.getTextFromAssets(mContext, "json/update.json");
        try {
            updateInfo = PacketParser.parseUpdateInfo(new JSONObject(updateStr));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ("0".equals(updateInfo.clientUpdate)) {//不更新
            if (null != result) {
                result.success();
            }
            return;
        }

        Random random = new Random();
        int index = random.nextInt(4);
        if (index % 2 == 0) {
            updateInfo.forceUpdate="1";
        }else {
            updateInfo.forceUpdate="0";
        }

        dialog = new UpdateDialog(mContext, updateInfo, new UpdateDialog.UpdateListener() {
            @Override
            public void onUpdate() {
                downloadAPK(updateInfo, result);
            }
        });
        dialog.show();
    }

    //下载apk
    private void downloadAPK(final UpdateInfo updateInfo, final ICheckUpdateResult result) {

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        progress += 5;
                        dialog.setProgress(progress);
                        if (progress < 100) {
                            sendEmptyMessageDelayed(0, 500);
                        } else {
                            sendEmptyMessage(1);
                        }
                        break;
                    case 1:
                        dialog.dismiss();
                        ToastUtil.startShort(mContext, "下载完成，准备安装app");
                        break;
                }

            }
        };
        handler.sendEmptyMessageDelayed(0, 500);

    }


    //下载完成之后安装apk
    private void installAPK(UpdateInfo updateInfo) {
        String fp = mContext.getPackageName() + ".fileprovider";
        File apkfile = new File(mSavePath, updateInfo.clientVersionName);
        if (!apkfile.exists()) {
            return;
        }
        String cmd = "chmod 777 " + apkfile.toString();
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) { //适配安卓7.0
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri apkFileUri = FileProvider.getUriForFile(context.getApplicationContext(), fp, apkfile);
            i.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        }
        context.startActivity(i);
        //关闭当前程序
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
    }

    public interface ICheckUpdateResult {
        void success();

        void fail(String message);
    }
}
