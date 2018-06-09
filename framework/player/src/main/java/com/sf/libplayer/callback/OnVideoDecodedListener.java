package com.sf.libplayer.callback;

/**
 * Created by sufan on 2018/4/29.
 */

public interface OnVideoDecodedListener {
    void onFrameDecoded(byte[] decoded);
}
