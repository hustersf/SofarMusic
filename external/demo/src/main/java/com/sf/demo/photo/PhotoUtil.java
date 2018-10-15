package com.sf.demo.photo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.sf.base.BaseActivity;
import com.sf.base.callback.CallBackIntent;
import com.sf.base.callback.PermissionsResultListener;
import com.sf.demo.window.alert.AlertUtil;
import com.sf.demo.window.pop.PopUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by sufan on 17/7/12.
 */

public class PhotoUtil {


    private static String imgPath;//图片保存地址
    private static String fp = "com.sf.sofarmusic.fileprovider";

    public static void getPhotoBitmap(final Context context, final BitmapCallBack callBack) {
        String[] permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA};
        fp = context.getPackageName() + ".fileprovider";

        String des = "更换头像需要获取照相机权限和读取手机目录权限";
        final BaseActivity activity = (BaseActivity) context;

        activity.requestPermissions(des, permissions, 100, new PermissionsResultListener() {
            @Override
            public void onPermissionGranted() {
                showSelectList(activity, callBack);
            }

            @Override
            public void onPermissionDenied() {
                String content = "相关权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
                AlertUtil.showTwoBtnDialog(activity, content, "取消", "确定", new AlertUtil.AlertCallback() {
                    @Override
                    public void onText(String str) {
                        if ("确定".equals(str)) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        }
                    }
                });
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
        imgPath = FileUtil.generateImgePath();
        File imgFile = new File(imgPath);  //设置拍照后的图片的保存位置
        Log.i("TAG", imgFile.getAbsolutePath());
        Uri imgUri = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            imgUri = FileProvider.getUriForFile(activity, fp, imgFile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

        final Uri uri = imgUri;
        activity.startActivityForResult(intent, new CallBackIntent() {
            @Override
            public void onResult(Intent data) {
                if (data == null || data.getData() == null)
                    //  ToastUtils.startShort(activity, "拍照数据返回为空");
                    Log.i("TAG", "拍照数据返回为空");

                startPhotoZoom(uri, activity, new IntentCallBack() {
                    @Override
                    public void onIntent(Intent intent) {
                        callBack.onBitmap(getImageView(intent));
                    }
                });


            }
        });

    }

    private static void openAlbum(final BaseActivity activity, final BitmapCallBack callBack) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, new CallBackIntent() {
            @Override
            public void onResult(Intent data) {
                startPhotoZoom(data.getData(), activity, new IntentCallBack() {
                    @Override
                    public void onIntent(Intent intent) {
                        callBack.onBitmap(getImageView(intent));
                    }
                });

            }
        });
    }


    //图片裁减
    private static void startPhotoZoom(Uri uri, BaseActivity activity, final IntentCallBack callBack) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        //相机和相册之后调用的裁减，传递过来的uri已经做了7.0的适配，如果单独使用，需要向相机那样适配

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件,这句不加，拍照之后的裁减图片会提示无法裁减，相册不加也可以
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", true);

        //不是1比1 主要是为了适配华为手机一比一时，裁减是圆形的
        intent.putExtra("aspectX", 9998);
        intent.putExtra("aspectY", 9999);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);

        /**
         *  这句话不用做7.0的适配，我也是醉了
         *  当然原图片和裁减图片保存地址可以不同
         *  裁减图片比原图片小很多（华为手机，原图片3m多，裁减后8kb）
         */
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imgPath)));  //设置裁减后的图片的地址

        activity.startActivityForResult(intent, new CallBackIntent() {
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


    private String getFileName() {
        // 用日期作为文件名，确保唯一性
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss");
        String fileName = formatter.format(date) + ".PNG";
        return fileName;
    }


    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
