package com.sf.demo.system.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import com.sf.base.BaseActivity;
import com.sf.base.callback.ActivityCallbackAdapter;
import com.sf.base.permission.PermissionUtil;
import com.sf.demo.system.contact.model.ContactInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sufan on 17/7/27.
 * 获取通讯录电话号码
 */
public class ContactUtil {

  /**
   * 异步获取通讯录信息
   */
  public static Observable<List<ContactInfo>> getContactsAsync(Context context) {
    return Observable.fromCallable(new Callable<List<ContactInfo>>() {
      @Override
      public List<ContactInfo> call() throws Exception {
        return getContactsNew(context);
      }
    }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }

  @Deprecated
  private static List<ContactInfo> getContacts(Context context) {
    List<ContactInfo> list = new ArrayList<>();

    Cursor cursor = context.getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    while (cursor.moveToNext()) {
      List<String> phoneList = new ArrayList<>();
      // 只要有1个或者多个电话号码，hasphone的值为1
      String hasPhone =
          cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
      String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
      String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

      if ("1".equals(hasPhone)) {
        Cursor phones =
            context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
        while (phones.moveToNext()) {
          String result = phones
              .getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          phoneList.add(result);
        }
        phones.close();
      }

      ContactInfo item = new ContactInfo();
      item.name = name;
      item.phones = phoneList;
      list.add(item);
    }
    cursor.close();
    return list;
  }

  private static List<ContactInfo> getContactsNew(Context context) {
    List<ContactInfo> list = new ArrayList<>();

    // 搜索字段
    String[] projection = new String[] {
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.Contacts.SORT_KEY_PRIMARY};

    if (Build.VERSION.SDK_INT >= 19) {
      projection[3] = "phonebook_label";
    }

    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

    Cursor cursor = context.getContentResolver().query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
        null, null, sortOrder);
    if (cursor != null) {
      // key: contactId,value: 该contactId在联系人集合data的index
      Map<String, Integer> contactIdMap = new HashMap<>();
      while (cursor.moveToNext()) {
        // 获取联系人的ID
        String contactId = cursor.getString(0);
        // 获取联系人的姓名
        String name = cursor.getString(1);
        // 获取联系人的号码
        String phoneNumber = cursor.getString(2);
        String sortKey = cursor.getString(3);

        if (contactIdMap.containsKey(contactId)) {
          int index = contactIdMap.get(contactId);
          ContactInfo item = list.get(index);
          if (item.phones == null) {
            item.phones = new ArrayList<>();
          }
          item.phones.add(phoneNumber);
        } else {
          List<String> phoneList = new ArrayList<>();
          phoneList.add(phoneNumber);
          ContactInfo item = new ContactInfo();
          item.id = contactId;
          item.key = sortKey;
          item.name = name;
          item.phones = phoneList;
          list.add(item);
          contactIdMap.put(contactId, list.size() - 1);
        }
      }
      cursor.close();
    }
    return list;
  }


  /**
   * 跳转至系统通讯录获取手机号码
   */
  public static void getPhoneNumber(final BaseActivity activity, final PhoneCallback callback) {
    String des = "获取电话号码需要读取通讯录权限";
    String content = "相关权限被禁止,该功能无法使用\n如要使用,请前往设置进行授权";
    PermissionUtil.requestPermission(activity, Manifest.permission.READ_CONTACTS, des, content)
        .subscribe(permission -> {
          if (permission.granted) {
            jump2Contacts(activity, callback);
          }
        });
  }


  // 跳转至通讯录界面
  private static void jump2Contacts(final BaseActivity activity, final PhoneCallback callback) {
    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    activity.startActivityForResult(intent, new ActivityCallbackAdapter() {
      @Override
      public void onResult(Intent data) {

        ContentResolver cr = activity.getContentResolver();
        Uri uri = data.getData();
        // content://com.android.contacts/contacts/lookup/3285i25847320211128321/46
        Cursor cursor = cr.query(uri, null, null, null, null);
        cursor.moveToFirst();
        getContactPhoneAsync(activity, cursor).subscribe(phones -> {
          if (callback != null) {
            callback.onPhone(phones);
          }
        });
      }
    });
  }


  /**
   * 异步获取某个联系人的手机号码
   */
  private static Observable<List<String>> getContactPhoneAsync(BaseActivity activity,
      Cursor cursor) {
    return Observable.fromCallable(new Callable<List<String>>() {
      @Override
      public List<String> call() throws Exception {
        return getContactPhone(activity, cursor);
      }
    }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }


  private static List<String> getContactPhone(BaseActivity activity, Cursor cursor) {
    List<String> phoneList = new ArrayList<>();
    // 只要有1个或者多个电话号码，hasphone的值为1
    String hasPhone =
        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

    if ("1".equals(hasPhone)) {
      Cursor phones =
          activity.getContentResolver().query(
              ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
              null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
      while (phones.moveToNext()) {
        String result =
            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        phoneList.add(result);
      }
      phones.close();
    }
    cursor.close();
    return phoneList;
  }


  // 电话号码回调
  public interface PhoneCallback {
    void onPhone(List<String> phones);
  }
}
