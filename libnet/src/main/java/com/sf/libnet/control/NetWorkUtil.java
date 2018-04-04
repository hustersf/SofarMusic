package com.sf.libnet.control;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.sf.libnet.callback.BitmapCallback;
import com.sf.libnet.callback.FileCallback;
import com.sf.libnet.callback.StringCallback;
import com.sf.libnet.http.HttpConfig;
import com.sf.libnet.http.RetrofitHttpService;
import com.sf.libnet.http.RetrofitManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sufan on 17/3/3.
 */

public class NetWorkUtil {

    private static NetWorkUtil instance = null;
    private Handler mDelivery;

    private final String ERROR = "服务器通讯异常，请重试";


    private NetWorkUtil() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static NetWorkUtil getInstance() {
        if (instance == null) {
            synchronized (NetWorkUtil.class) {
                if (instance == null) {
                    instance = new NetWorkUtil();
                }
            }
        }
        return instance;
    }


    public void requestGetAsyn(String url, Map<String, String> params, final StringCallback callback) {
        RetrofitHttpService service = RetrofitManager.getInstance().create(RetrofitHttpService.class);
        Call<ResponseBody> call = service.executGet(url, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        callback.OnSuccess(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.OnError("异常信息:" + e.getMessage());
                    }
                } else {
                    callback.OnError("错误码:" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.OnError(t.getMessage());
                } else {
                    callback.OnError(ERROR);
                }

            }
        });

    }

    public void requestPostAsyn(String url, Map<String, String> params, final StringCallback callback) {
        RetrofitHttpService service = RetrofitManager.getInstance().create(RetrofitHttpService.class);
        Call<ResponseBody> call = service.executePost(url, params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        callback.OnSuccess(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.OnError("异常信息:" + e.getMessage());
                    }
                } else {
                    callback.OnError("错误码:" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.OnError(t.getMessage());
                } else {
                    callback.OnError(ERROR);
                }

            }
        });


    }

    public void requestPostJsonAsyn(String url, Map<String, String> params, final StringCallback callback) {
        RetrofitHttpService service = RetrofitManager.getInstance().create(RetrofitHttpService.class);
        MediaType JSON = MediaType.parse("application/json");
        JSONObject jsonObject = new JSONObject(params);
        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
        Call<ResponseBody> call = service.executeJsonPost(url, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        callback.OnSuccess(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.OnError("异常信息:" + e.getMessage());
                    }
                } else {
                    callback.OnError("错误码:" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.OnError(t.getMessage());
                } else {
                    callback.OnError(ERROR);
                }

            }
        });


    }

    public void requestGetBitmapAsyn(String url, final BitmapCallback callback) {
        RetrofitHttpService service = RetrofitManager.getInstance().create(RetrofitHttpService.class);
        Call<ResponseBody> call = service.loadBitmap(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.OnSuccess(BitmapFactory.decodeStream(response.body().byteStream()));
                } else {
                    callback.OnError("错误码:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.OnError(t.getMessage());
                } else {
                    callback.OnError(ERROR);
                }
            }
        });
    }

    public void downloadFile(String url, final FileCallback callback) {

        RetrofitHttpService service = RetrofitManager.getInstance().create(RetrofitHttpService.class);
        Call<ResponseBody> call = service.downloadFile(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveFile(response.body(), callback);
                        }
                    }).start();
                } else {
                    callback.OnError("错误码:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    callback.OnError(t.getMessage());
                } else {
                    callback.OnError(ERROR);
                }
            }
        });
    }

    //安卓内置证书（自签名证书）
    public void setCertificates(InputStream... certificates) {
        HttpConfig.certificates = certificates;
    }

    /**
     * 关闭验证DNS和certificate匹配
     */
    public void closeHostnameVerifier() {
        RetrofitManager.getInstance().closeHostnameVerifier();
    }



    public void isDebug(boolean flag) {
        HttpConfig.isDebug = flag;
    }

    public void isCache(boolean flag) {
        HttpConfig.isCache = flag;
    }

    public void setContext(Context context){
        HttpConfig.context=context;
    }

    // 保存文件
    private boolean saveFile(ResponseBody responseBody, final FileCallback callback) {
        String destFiledir = callback.mDir;
        String destFileName = callback.mFileName;


        InputStream is = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[1024];
        int len = -1;
        try {
            is = responseBody.byteStream();
            final long total = responseBody.contentLength();
            long sum = 0;
            File dir = new File(destFiledir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {

                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                mDelivery.post(new Runnable() {

                    @Override
                    public void run() {
                     //   Log.i("TAG", "finalSum:" + finalSum+"  total:"+total+"  p:"+(finalSum * 1.0f / total * 100));
                        callback.Progress(finalSum * 1.0f / total * 100);

                    }
                });
            }

            final File finalFile = file;
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    callback.OnSuccess(finalFile);
                }
            });
            return true;
        } catch (Exception e) {
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    callback.OnError("下载出现异常");
                }
            });
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

}
