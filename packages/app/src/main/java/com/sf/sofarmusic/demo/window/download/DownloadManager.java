package com.sf.sofarmusic.demo.window.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sf.utility.NetUtil;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 17/8/16.
 */

public class DownloadManager {

    private Context mContext;
    private static DownloadManager INSTANCE = null;

    private DownloadDialog dialog;
    private int progress = 0;

    public static DownloadManager getInstance(Context context) {
        INSTANCE = new DownloadManager(context);
        return INSTANCE;
    }

    private DownloadManager(Context context) {
        mContext = context;
    }

    public void start(IDownloadResult result){

        if (!NetUtil.isNetAvailable(mContext)) {
            // 网络关闭状态
            if (result != null) {
                result.fail("网络未开启,请检查网络！");
            }
            return;
        }

        dialog=new DownloadDialog(mContext);
        dialog.show();
        download(result);
    }


    private void download(IDownloadResult result){

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
                        ToastUtil.startShort(mContext, "下载完成");
                        break;
                }

            }
        };
        handler.sendEmptyMessageDelayed(0, 500);

    }


    public interface IDownloadResult {
        void success();

        void fail(String message);
    }
}
