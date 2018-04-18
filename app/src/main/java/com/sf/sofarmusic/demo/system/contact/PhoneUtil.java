package com.sf.sofarmusic.demo.system.contact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;

import com.sf.sofarmusic.base.BaseActivity;
import com.sf.sofarmusic.callback.CallBackIntent;
import com.sf.sofarmusic.callback.PermissionsResultListener;
import com.sf.sofarmusic.demo.window.alert.AlertUtil;
import com.sf.sofarmusic.util.SheetDialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/7/27.
 * 获取通讯录电话号码
 */

public class PhoneUtil {

    public static void getPhoneNumber(final BaseActivity activity, final PhoneCallback callback) {
        String des = "获取电话号码需要读取通讯录权限";
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
        activity.requestPermissions(des, permissions, 100, new PermissionsResultListener() {
            @Override
            public void onPermissionGranted() {

                jump2Contacts(activity,callback);
            }

            @Override
            public void onPermissionDenied() {
                String content = "相关权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
                AlertUtil.showTwoBtnDialog(activity, content, "取消", "确定", new AlertUtil.AlertCallback() {
                    @Override
                    public void onText(String str) {
                        if ("确定".equals(str)) {
                            //跳转至设置页面进行授权，不能适配所有的手机系统
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


    //跳转至通讯录界面
    private static void jump2Contacts(final BaseActivity activity, final PhoneCallback callback){
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        activity.startActivityForResult(intent, new CallBackIntent() {
            @Override
            public void onResult(Intent data) {


                ContentResolver cr = activity.getContentResolver();
                Uri uri = data.getData();
                // content://com.android.contacts/contacts/lookup/3285i25847320211128321/46
                Cursor cursor = cr.query(uri, null, null, null, null);
                cursor.moveToFirst();
                getContactPhone(activity, cursor,callback);
            }
        });
    }


    //获取电话号码
    private static void getContactPhone(BaseActivity activity, Cursor cursor, final PhoneCallback callback) {
        List<String> phoneList = new ArrayList<String>();
        // 只要有1个或者多个电话号码，hasphone的值为1
        String hasPhone = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        String id = cursor.getString(cursor
                .getColumnIndex(ContactsContract.Contacts._ID));
        String result = null;

        if (hasPhone.equalsIgnoreCase("1")) {
            hasPhone = "true";
        } else {
            hasPhone = "false";
        }
        if (Boolean.parseBoolean(hasPhone)) {
            Cursor phones = activity.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + id, null, null);
            while (phones.moveToNext()) {
                result = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneList.add(result);
            }
            phones.close();
        }

        if (phoneList.size() != 0 && phoneList.size() == 1) {
            if (callback != null) {
                callback.onPhone(result);
            }
        } else {
            SheetDialogUtil.showPhoneList(activity, "电话号码列表", phoneList,
                    new SheetDialogUtil.PhoneCallback() {

                        @Override
                        public void OnPhone(String phoneNum) {

                            if (callback != null) {
                                callback.onPhone(phoneNum);
                            }
                        }
                    });
        }

    }


    //电话号码回调
    public interface PhoneCallback {
        void onPhone(String phone);
    }
}
