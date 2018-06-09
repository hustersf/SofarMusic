package com.sf.libnet.callback;

import android.graphics.Bitmap;

public interface BitmapCallback {

	public void OnSuccess(Bitmap bitmap);

	public void OnError(Object obj);

}
