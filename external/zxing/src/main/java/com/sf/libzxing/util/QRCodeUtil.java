package com.sf.libzxing.util;

import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.sf.libzxing.activity.CaptureActivity;
import com.sf.libzxing.decode.DecodeFormatManager;
import com.sf.libzxing.encode.EncodeUtil;

/**
 * Created by sufan on 17/7/13.
 */

public class QRCodeUtil {

    private Activity activity;
    private DecodeQRCodeResult decodeQRCodeResult;
    private QRCodeUtil.ScanQRCodeReceiver receiver;

    //二维码默认大小
    private int defaultSize;

    //条形码默认宽高
    private int defaultWidth;
    private int defaultHeight;


    public QRCodeUtil(Activity activity) {
        this.activity = activity;
        defaultSize = dp2px(200);
        defaultWidth = dp2px(250);
        defaultHeight = dp2px(50);
    }


    public Bitmap CreateQRCode(String str) {
        Bitmap bitmap = null;
        bitmap = EncodeUtil.createQRCode(str, defaultSize, defaultSize, null);
        return bitmap;
    }

    public Bitmap CreateQRCode(String str, Bitmap logo) {
        Bitmap bitmap = null;
        bitmap = EncodeUtil.createQRCode(str, defaultSize, defaultSize, logo);
        return bitmap;
    }

    public Bitmap CreateQRCode(String str, int size) {
        Bitmap bitmap = null;
        bitmap = EncodeUtil.createQRCode(str, size, size, null);
        return bitmap;
    }

    public Bitmap CreateQRCode(String str, int size, Bitmap logo) {
        Bitmap bitmap = null;
        bitmap = EncodeUtil.createQRCode(str, size, size, logo);
        return bitmap;
    }

    public Bitmap CreateOneCode(String str) {
        Bitmap bitmap = null;
        bitmap = EncodeUtil.createOneCode(str, defaultWidth, defaultHeight);
        return bitmap;
    }

    public Bitmap CreateOneCode(String str, int width, int height) {
        Bitmap bitmap = null;
        bitmap = EncodeUtil.createOneCode(str, width, height);
        return bitmap;
    }



    public String DecodeQRCode(Bitmap bitmap) {
        Result result = null;
        try {
            MultiFormatReader e = new MultiFormatReader();
            Vector decodeFormats = new Vector();
            if(decodeFormats == null || decodeFormats.isEmpty()) {
                decodeFormats = new Vector();
                decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
                decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
                decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
            }

            Hashtable hints = new Hashtable(2);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
            hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
            e.setHints(hints);
            int lWidth = bitmap.getWidth();
            int lHeight = bitmap.getHeight();
            System.out.println("图片宽高：" + lWidth + "----" + lHeight);
            BitmapLuminanceSource rSource = new BitmapLuminanceSource(bitmap);
            HybridBinarizer hybridBinarizer = new HybridBinarizer(rSource);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
            result = e.decodeWithState(binaryBitmap);
        } catch (Exception var11) {
            Toast.makeText(this.activity, "二维码解析失败！", Toast.LENGTH_SHORT).show();
        }

        return result == null?"":result.getText();
    }

    public void DecodeQRCode(DecodeQRCodeResult decodeQRCodeResult) {
        this.decodeQRCodeResult = decodeQRCodeResult;
        this.initScanQRCodeReceiver();
        Intent intent = new Intent(this.activity, CaptureActivity.class);
        this.activity.startActivity(intent);
    }



    public void initScanQRCodeReceiver() {
        this.receiver = new QRCodeUtil.ScanQRCodeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ZxingUtil.receiver_action);
        this.activity.registerReceiver(this.receiver, filter);
    }

    public void destroyScanQRCodeReceiver() {
        if (this.receiver != null) {
            this.activity.unregisterReceiver(this.receiver);
        }

    }

    private class ScanQRCodeReceiver extends BroadcastReceiver {
        private ScanQRCodeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ZxingUtil.receiver_action)) {
                if (intent.getExtras() != null) {
                    QRCodeUtil.this.decodeQRCodeResult.result(intent.getExtras().getString("result"));
                }
                QRCodeUtil.this.destroyScanQRCodeReceiver();
            }

        }
    }


    public interface DecodeQRCodeResult {
        void result(String result);
    }


    public static int dp2px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    public static int sp2px(float sp) {
        return (int) (sp * Resources.getSystem().getDisplayMetrics().scaledDensity + 0.5f);
    }

}
