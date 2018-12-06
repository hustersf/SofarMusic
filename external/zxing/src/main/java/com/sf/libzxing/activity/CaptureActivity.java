/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sf.libzxing.activity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.sf.libzxing.R;
import com.sf.libzxing.camera.CameraManager;
import com.sf.libzxing.decode.DecodeFormatManager;
import com.sf.libzxing.decode.DecodeThread;
import com.sf.libzxing.util.BeepManager;
import com.sf.libzxing.util.BitmapLuminanceSource;
import com.sf.libzxing.util.CaptureActivityHandler;
import com.sf.libzxing.util.InactivityTimer;
import com.sf.libzxing.util.LightControl;
import com.sf.libzxing.util.ZxingUtil;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */

public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;

	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	private Rect mCropRect = null;
	private boolean isHasSurface = false;

	// 自己新添加的VIEW
	private ImageView scan_picture_iv, scan_back_iv,light_iv;
	private boolean mIsOn=false;   //等是否亮

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);

		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.9f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);

		// 自己新添加的VIEW
		initView();
	}

	private void initView() {
		scan_picture_iv = (ImageView) findViewById(R.id.scan_picture_iv);
		scan_back_iv = (ImageView) findViewById(R.id.scan_back_iv);
		light_iv = (ImageView) findViewById(R.id.light_iv);

		scan_picture_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent innerIntent = new Intent();
				if (Build.VERSION.SDK_INT < 19) {
					innerIntent.setAction(Intent.ACTION_GET_CONTENT);
					innerIntent.setType("image/*");
					Intent wrapIntent = Intent.createChooser(innerIntent,
							"选择二维码图片");
					startActivityForResult(wrapIntent, 0);
				} else {
					// innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
					innerIntent.setAction(Intent.ACTION_PICK);
					innerIntent.setType("image/*");
					Intent wrapIntent = Intent.createChooser(innerIntent,
							"选择二维码图片");
					startActivityForResult(wrapIntent, 1);
				}
			}
		});

		scan_back_iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});

		light_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lightSwitch();
			}
		});
	}

	private void lightSwitch(){
		LightControl lightControl=new LightControl(cameraManager.getCamera());
		if(mIsOn){
			lightControl.turnOff();
			light_iv.setImageResource(R.drawable.scan_light_off);
			mIsOn=false;
		}else {
			lightControl.turnOn();
			light_iv.setImageResource(R.drawable.scan_light_on);
			mIsOn=true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		handler = null;

		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		Intent resultIntent = new Intent();
		resultIntent.setAction(ZxingUtil.receiver_action);
		sendBroadcast(resultIntent);
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param bundle
	 *            The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		inactivityTimer.onActivity();
		beepManager.playBeepSoundAndVibrate();

		decodeResult(rawResult);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager,
						DecodeThread.ALL_MODE);
			}

			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("Camera error");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 扫描二维码图片的方法
	public Result scanImg(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		int width = scanBitmap.getWidth();
		int height = scanBitmap.getHeight();
		int[] pixels = new int[width * height];

		RGBLuminanceSource source = new RGBLuminanceSource(width, height,
				pixels);
		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(binaryBitmap, hints);

		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Result scanBitmap(Bitmap bitmap) {
		Result result = null;
		try {
			MultiFormatReader multiFormatReader = new MultiFormatReader();

			Vector decodeFormats = new Vector();
			if ((decodeFormats == null) || (decodeFormats.isEmpty())) {
				decodeFormats = new Vector();

				decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
				decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
				decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
			}

			Hashtable hints = new Hashtable(2);
			hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

			hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

			multiFormatReader.setHints(hints);
			int lWidth = bitmap.getWidth();
			int lHeight = bitmap.getHeight();
			System.out.println("图片宽高：" + lWidth + "----" + lHeight);
			BitmapLuminanceSource rSource = new BitmapLuminanceSource(bitmap);

			HybridBinarizer hybridBinarizer = new HybridBinarizer(rSource);
			BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
			result = multiFormatReader.decodeWithState(binaryBitmap);
		} catch (Exception e) {
			Toast.makeText(this, "二维码解析失败！", Toast.LENGTH_SHORT).show();
		}
		return result;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}

		Uri photoUri = data.getData();
		switch (requestCode) {
		case 0:
			// <4.4
			cropPicture(photoUri);
			break;
		case 1:
			// >=4.4 (19)
//			String thePath = getPath(this, photoUri);
//			Uri uri=getImageContentUri(this,new File(thePath));
//			cropPicture(uri);
			cropPicture(photoUri);
			break;
		case 2:
			Bitmap bitmap = data.getParcelableExtra("data");
			if (bitmap != null) {
				decodeResult(scanBitmap(bitmap));
			} else {
				Toast.makeText(CaptureActivity.this, "二维码图片获取失败",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}

	// 裁剪图片
	private void cropPicture(Uri uri) {
		Intent innerIntent = new Intent("com.android.camera.action.CROP");
		innerIntent.setDataAndType(uri, "image/*");
		innerIntent.putExtra("crop", "true");

		//不是1比1 主要是为了适配华为手机一比一时，裁减是圆形的
		innerIntent.putExtra("aspectX", 9998);
		innerIntent.putExtra("aspectY", 9999);

		innerIntent.putExtra("outputX", 320);
		innerIntent.putExtra("outputY", 320);
		innerIntent.putExtra("return-data", true);
		innerIntent.putExtra("scale", true);
		startActivityForResult(innerIntent, 2);
		
	}



	private void decodeResult(Result result) {

		if (result != null) {
			Intent resultIntent = new Intent();
			resultIntent.setAction(ZxingUtil.receiver_action);
			Bundle bundle = new Bundle();
			bundle.putInt("width", mCropRect.width());
			bundle.putInt("height", mCropRect.height());
			bundle.putString("result", result.getText());
			resultIntent.putExtras(bundle);
			sendBroadcast(resultIntent);
			CaptureActivity.this.finish();
			
		} else {
		//	Toast.makeText(this, "您选择的可能不是二维码", Toast.LENGTH_SHORT).show();
		}
	}

}