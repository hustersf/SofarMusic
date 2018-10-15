package com.sf.utility;

import android.os.Bundle;
import android.os.Message;

public class MessageUtil {
    /**
     * 初始化message 如果无数据则bundle传入null
     */
    public static Message getMessage(final int what, final Bundle bundle) {
        final Message message = Message.obtain();
        message.what = what;
        if (bundle != null) {
            message.setData(bundle);
        }
        return message;
    }

    /**
     * 初始化message
     **/
    public static Message getMessage(final int what, final Object obj) {
        final Message message = Message.obtain();
        message.what = what;
        message.obj = obj;
        return message;
    }

    /**
     * 获取message
     **/
    public static Message getMessage(final int what, final int arg1) {
        final Message message = Message.obtain();
        message.what = what;
        message.arg1 = arg1;
        return message;
    }

    /**
     * 获取message
     **/
    public static Message getMessage(final int what, final int arg1, final int arg2) {
        final Message message = Message.obtain();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        return message;
    }
}
