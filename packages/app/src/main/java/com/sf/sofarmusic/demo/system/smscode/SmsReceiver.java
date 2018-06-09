package com.sf.sofarmusic.demo.system.smscode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;

import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.util.MessageUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sufan on 17/7/27.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private String TAG="SmsReceiver";

    private static Handler mHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action==SMS_RECEIVED_ACTION && mHandler!=null){
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
                //将pdus里面的内容转化成Object[]数组
                Object pdusData[] = (Object[]) bundle.get("pdus");

                //解析短信内容
                SmsMessage[] msg=new SmsMessage[pdusData.length];
                for(int i=0;i<msg.length;i++){
                    byte pdus[] = (byte[]) pdusData[i];
                    msg[i] = SmsMessage.createFromPdu(pdus);

                    StringBuffer content = new StringBuffer();//获取短信内容
                    StringBuffer phoneNumber = new StringBuffer();//获取地址
                    //分析短信具体参数
                    for (SmsMessage temp : msg){
                        content.append(temp.getMessageBody());
                        phoneNumber.append(temp.getOriginatingAddress());
                    }
                  Log.i(TAG,"发送者号码："+phoneNumber.toString()+"  短信内容："+content.toString());

                   //在这里写自己的逻辑
                    final Pattern pattern = Pattern.compile("[0-9]+");
                    final Matcher matcher = pattern.matcher(content);
                    while (matcher.find()){
                        if (matcher.group().length() == 6) {
                            Log.i(TAG, "验证码：" + matcher.group());
                            mHandler.sendMessage(MessageUtil.getMessage(
                                    Constant.SMS_RECEIVED, matcher.group()));
                        }
                    }
                }
            }
        }

    }

    public static void setHandler(final Handler handler) {
        mHandler = handler;
    }

    public static void removeHandler() {
        mHandler = null;
    }

}
