package com.sf.sofarmusic.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sf.libnet.callback.BitmapCallback;
import com.sf.libnet.callback.FileCallback;
import com.sf.libnet.callback.StringCallback;
import com.sf.libnet.control.NetWorkUtil;
import com.sf.libskin.base.SkinBaseActivity;
import com.sf.sofarmusic.PlayServiceAIDL;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.callback.CallBackIntent;
import com.sf.sofarmusic.callback.PermissionsResultListener;
import com.sf.sofarmusic.demo.window.alert.AlertUtil;
import com.sf.sofarmusic.util.AppManager;
import com.sf.sofarmusic.util.AppUtil;
import com.sf.sofarmusic.util.LogUtil;
import com.sf.sofarmusic.view.LoadView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sufan on 17/2/28.
 */

public class BaseActivity extends SkinBaseActivity implements ActivityInterface {
    private static final String TAG = "BaseActivity";

    public BaseActivity baseAt;
    public View rootView;

    //设置启动和退出Activity的动画
    public static final String START_EXIT_PAGE_ANIMATION = "start_exit_page_animation";
    public static final String START_ENTER_PAGE_ANIMATION = "start_enter_page_animation";
    public static final String FINISH_EXIT_PAGE_ANIMATION = "finish_exit_page_animation";
    public static final String FINISH_ENTER_PAGE_ANIMATION = "finish_enter_page_animation";
    public static final int NO_ANIM = 0;


    public FrameLayout mContentContainer;   //获取根视野
    private View mLoadView;
    private LoadView loadView;

    //6.0权限
    private PermissionsResultListener mListener;
    private int mRequestCode;


    //activity请求回调相关
    private static final int REQUESTCODE = 100;
    private static final int RESULTCODE = 101;
    private CallBackIntent CallBackIntent;

    public PlayServiceAIDL iBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseAt = this;
        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        AppManager.getAppManager().addActivity(baseAt);

        //初始化loadview
        mContentContainer = (FrameLayout) rootView;
        mLoadView = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_loadview, null);
        loadView = (LoadView) mLoadView.findViewById(R.id.loadview);
        dynamicAddView(loadView, "loadColor", R.color.themeColor);
        dynamicAddView(loadView, "loadTextColor", R.color.main_text_color);
        loadView.setVisibility(View.GONE);  //默认隐藏


        //绑定服务
        Intent intent = new Intent();
        String pkg = "com.sf.sofarmusic";
        String cls = "com.sf.sofarmusic.play.PlayService";
        intent.setComponent(new ComponentName(pkg, cls));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * 在onCreate之后执行
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //将loadview放进布已经加载好的xml布局里
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mContentContainer.addView(mLoadView, layoutParams);
    }


    public void show() {
        loadView.setVisibility(View.VISIBLE);
    }


    public void dismiss() {
        loadView.setVisibility(View.GONE);
    }



    public void requestGet(final String url, Map<String, String> map, final StringCallback callback) {
        if (map == null) {
            map = new HashMap<>();   //map为空时，请求无效
        }
        NetWorkUtil.getInstance().requestGetAsyn(url, map, new StringCallback() {
            @Override
            public void OnSuccess(String str) {
                dismiss();
                LogUtil.d(TAG, "response:" + str);
                if (TextUtils.isEmpty(str) || !str.startsWith("{")) {
                    Toast.makeText(baseAt, "网络连接不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                    callback.OnError(str);
                }else {
                    callback.OnSuccess(str);
                }
            }

            @Override
            public void OnError(Object obj) {
                LogUtil.d(TAG, "error:" + obj.toString()+" url:"+url);
                dismiss();
                if (baseAt != null) {
                    Toast.makeText(baseAt, "网络连接不可用，请稍后再试", Toast.LENGTH_SHORT).show();
                }
                callback.OnError(obj);
            }
        });
    }

    public void requestGetNoError(final String url, Map<String, String> map, final StringCallback callback) {
        if (map == null) {
            map = new HashMap<>();   //map为空时，请求无效
        }
        NetWorkUtil.getInstance().requestGetAsyn(url, map, new StringCallback() {
            @Override
            public void OnSuccess(String str) {
                dismiss();
                LogUtil.d(TAG, "response:" + str);
                if (TextUtils.isEmpty(str) || !str.startsWith("{")) {
                    callback.OnError(str);
                }else {
                    callback.OnSuccess(str);
                }
            }

            @Override
            public void OnError(Object obj) {
                LogUtil.d(TAG, "error:" + obj.toString()+" url:"+url);
                dismiss();
                callback.OnError(obj);
            }
        });
    }


    public void requestPost(String url, Map<String, String> map, final StringCallback callback) {
        if (map == null) {
            map = new HashMap<>();   //map为空时，请求无效
        }
        NetWorkUtil.getInstance().requestPostAsyn(url, map, new StringCallback() {
            @Override
            public void OnSuccess(String str) {
                callback.OnSuccess(str);
            }

            @Override
            public void OnError(Object obj) {
                if (baseAt != null) {
                    Toast.makeText(baseAt, obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void requestPostJson(String url, Map<String, String> map, final StringCallback callback) {
        if (map == null) {
            map = new HashMap<>();   //map为空时，请求无效
        }
        NetWorkUtil.getInstance().requestPostJsonAsyn(url, map, new StringCallback() {
            @Override
            public void OnSuccess(String str) {
                callback.OnSuccess(str);
            }

            @Override
            public void OnError(Object obj) {
                if (baseAt != null) {
                    Toast.makeText(baseAt, obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void requestBitmap(String url, final BitmapCallback callback) {
        NetWorkUtil.getInstance().requestGetBitmapAsyn(url, new BitmapCallback() {
            @Override
            public void OnSuccess(Bitmap bitmap) {
                callback.OnSuccess(bitmap);
            }

            @Override
            public void OnError(Object obj) {
                if (baseAt != null) {
                    Toast.makeText(baseAt, obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void downloadFile(String url, final FileCallback callback) {
        NetWorkUtil.getInstance().downloadFile(url, new FileCallback(callback.mDir, callback.mFileName) {
            @Override
            public void OnSuccess(File file) {
                Toast.makeText(baseAt, "下载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(Object obj) {
                if (baseAt != null) {
                    Toast.makeText(baseAt, obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Progress(float progress) {
                callback.Progress(progress);
            }
        });
    }


    /**
     * 其他 Activity 继承 BaseActivity 调用requestPermissions 方法
     *
     * @param desc        首次申请权限被拒绝后再次申请给用户的描述提示
     * @param permissions 要申请的权限数组
     * @param requestCode 申请标记值
     * @param listener    实现的接口
     */
    @Override
    public void requestPermissions(String desc, String[] permissions, int requestCode, PermissionsResultListener listener) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        mRequestCode = requestCode;
        mListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkEachSelfPermission(permissions)) {// 检查是否声明了权限
                requestEachPermissions(desc, permissions, requestCode);
            } else {// 已经申请权限
                if (mListener != null) {
                    mListener.onPermissionGranted();
                }
            }
        } else {
            if (mListener != null) {
                mListener.onPermissionGranted();
            }
        }
    }

    /**
     * 申请权限前判断是否需要声明
     *
     * @param desc
     * @param permissions
     * @param requestCode
     */
    private void requestEachPermissions(String desc, String[] permissions, int requestCode) {
        if (shouldShowRequestPermissionRationale(permissions)) {// 需要再次声明
            showRationaleDialog(desc, permissions, requestCode);
        } else {
            ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
        }
    }

    /**
     * 弹出声明的 Dialog
     *
     * @param desc
     * @param permissions
     * @param requestCode
     */
    private void showRationaleDialog(String desc, final String[] permissions, final int requestCode) {
        AlertUtil.showTwoBtnDialog(this, desc, "取消", "确定", new AlertUtil.AlertCallback() {
            @Override
            public void onText(String str) {
                if ("确定".equals(str)) {
                    ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
                }
            }
        });
    }


    /**
     * 再次申请权限时，是否需要声明
     *
     * @param permissions
     * @return
     */
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检察每个权限是否申请
     *
     * @param permissions
     * @return true 需要申请权限,false 已申请权限
     */
    private boolean checkEachSelfPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 申请权限结果的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequestCode) {
            if (checkEachPermissionsGranted(grantResults)) {
                if (mListener != null) {
                    mListener.onPermissionGranted();
                }
            } else {// 用户拒绝申请权限
                if (mListener != null) {
                    mListener.onPermissionDenied();
                }
            }
        }
    }

    /**
     * 检查回调结果
     *
     * @param grantResults
     * @return
     */
    private boolean checkEachPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * activity请求回调
     * 继承自该activity的页面，统一调用该方法
     */
    @Override
    public void startActivityForResult(Intent intent, CallBackIntent callBackIntent) {
        this.CallBackIntent = callBackIntent;
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void setActivityResultCallback(Intent intent) {
        if (intent == null) {
            setResult(RESULTCODE, new Intent());
        } else {
            setResult(RESULTCODE, intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULTCODE == resultCode) {
            //跳转自己写的页面
            if (CallBackIntent != null) {
                CallBackIntent.onResult(data);
            }
        } else if (REQUESTCODE == requestCode && resultCode != 0) {
            //跳转系统页面
            if (CallBackIntent != null) {
                CallBackIntent.onResult(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppUtil.isAppOnForeground(this)) {
            //  ToastUtil.startShort(this,"SofarMusic正在后台运行");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        AppManager.getAppManager().removeActivity(baseAt);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(
                getIntent().getIntExtra(FINISH_ENTER_PAGE_ANIMATION, NO_ANIM),
                getIntent().getIntExtra(FINISH_EXIT_PAGE_ANIMATION, R.anim.activity_animation_out_to_right));
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinder = PlayServiceAIDL.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBinder = null;
        }
    };

}





