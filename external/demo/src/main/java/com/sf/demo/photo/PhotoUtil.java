package com.sf.demo.photo;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.text.SimpleDateFormat;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.sf.base.BaseActivity;
import com.sf.base.callback.ActivityCallbackAdapter;
import com.sf.base.permission.PermissionUtil;
import com.sf.demo.window.pop.PopUtil;

/**
 * Created by sufan on 17/7/12.
 */

public class PhotoUtil {


  private static String cameraPath;// 拍照后的图片保存地址
  private static String cropPath;// 裁剪后的图片地址
  private static String fp = "com.sf.sofarmusic.fileprovider";

  public static void getPhotoBitmap(final Context context, final BitmapCallBack callBack) {
    fp = context.getPackageName() + ".fileprovider";
    String[] permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
      android.Manifest.permission.CAMERA};

    String refuseHint = "更换头像需要获取照相机权限和存储权限";
    String settingHint = "相机权限或者存储权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
    final BaseActivity activity = (BaseActivity) context;

    PermissionUtil.requestPermissions(activity, permissions, refuseHint, settingHint)
      .subscribe(aBoolean -> {
        if (aBoolean) {
          showSelectList(activity, callBack);
        }
      });
  }

  private static void showSelectList(final BaseActivity activity, final BitmapCallBack callBack) {
    String[] ss = {"拍照", "从相册中选择"};
    PopUtil.showBottomPop(activity, Arrays.asList(ss), new PopUtil.PopCallback() {
      @Override
      public void onText(String str) {
        if ("拍照".equals(str)) {
          openCamera(activity, callBack);
        } else {
          openAlbum(activity, callBack);
        }
      }
    });

  }


  private static void openCamera(final BaseActivity activity, final BitmapCallBack callBack) {
    cameraPath = FileUtil.generateImgePath();
    File imgFile = new File(cameraPath); // 设置拍照后的图片的保存位置
    Log.i("TAG", imgFile.getAbsolutePath());
    Uri imgUri = null;
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      // 通过FileProvider创建一个content类型的Uri
      imgUri = FileProvider.getUriForFile(activity, fp, imgFile);
      // 添加这一句表示对目标应用临时授权该Uri所代表的文件
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } else {
      imgUri = Uri.fromFile(imgFile);
    }

    intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

    final Uri uri = imgUri;
    activity.startActivityForResult(intent, new ActivityCallbackAdapter() {
      @Override
      public void onResult(Intent data) {
        if (data == null || data.getData() == null) {
          Log.i("TAG", "拍照数据返回为空");
        }

        startPhotoZoom(uri, activity, new IntentCallBack() {
          @Override
          public void onIntent(Intent intent) {
            callBack.onBitmap(getImageView());
          }
        });
      }
    });

  }

  private static void openAlbum(final BaseActivity activity, final BitmapCallBack callBack) {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    activity.startActivityForResult(intent, new ActivityCallbackAdapter() {
      @Override
      public void onResult(Intent data) {
        startPhotoZoom(data.getData(), activity, new IntentCallBack() {
          @Override
          public void onIntent(Intent intent) {
            callBack.onBitmap(getImageView());
          }
        });

      }
    });
  }


  // 图片裁减
  private static void startPhotoZoom(Uri uri, BaseActivity activity,
    final IntentCallBack callBack) {

    cropPath = FileUtil.generateAvatarImagePath();

    Intent intent = new Intent("com.android.camera.action.CROP");
    // 相机和相册之后调用的裁减，传递过来的uri已经做了7.0的适配，如果单独使用，需要向相机那样适配
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      // 添加这一句表示对目标应用临时授权该Uri所代表的文件,这句不加，拍照之后的裁减图片会提示无法裁减，相册不加也可以
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
    intent.setDataAndType(uri, "image/*");
    // 设置裁剪
    intent.putExtra("crop", true);

    // 不是1比1 主要是为了适配华为手机一比一时，裁减是圆形的
    intent.putExtra("aspectX", 9998);
    intent.putExtra("aspectY", 9999);
    // outputX outputY 是裁剪图片宽高(影响清晰度)
    intent.putExtra("outputX", 320);
    intent.putExtra("outputY", 320);

    //这句话 导致小米手机裁剪界面 crash
    //intent.putExtra("return-data", true);
    //解决方案:将裁剪后的图片保存在 uri 中
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cropPath))); //设置裁减后的图片的地址
    activity.startActivityForResult(intent, new ActivityCallbackAdapter() {
      @Override
      public void onResult(Intent data) {
        callBack.onIntent(data);
      }
    });

  }

  // 压缩图片
  public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height,
    int scale) {
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();
    width = w;
    height = h;
    Matrix matrix = new Matrix();
    float scaleWidth = ((float) (width / scale) / w);
    float scaleHeight = ((float) (height / scale) / h);
    matrix.postScale(scaleWidth, scaleHeight);
    Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    return newbmp;
  }

  // 图片回调接口
  public interface BitmapCallBack {

    public void onBitmap(Bitmap bitmap);
  }

  // 裁剪图片回调
  public interface IntentCallBack {

    public void onIntent(Intent intent);
  }

  // 保存裁剪之后的图片
  private static Bitmap getImageView(Intent data) {
    Bitmap photo = null;
    Bundle bundle = data.getExtras();
    if (bundle != null) {
      photo = bundle.getParcelable("data");
    }
    return photo;
  }

  // 从保存的图片地址中获取图片
  private static Bitmap getImageView() {
    return BitmapFactory.decodeFile(cropPath);
  }

  private String getFileName() {
    // 用日期作为文件名，确保唯一性
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat(
      "yyyy-MM-dd_HH-mm-ss");
    String fileName = formatter.format(date) + ".PNG";
    return fileName;
  }

}
