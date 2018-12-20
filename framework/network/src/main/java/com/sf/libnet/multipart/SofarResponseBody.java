package com.sf.libnet.multipart;

import com.sf.utility.IOUtil;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 封装的用于解决没有 content-length 的问题.
 */
public class SofarResponseBody extends ResponseBody {

  private BufferedSource mSource;
  private long mContentLength;
  private MediaType mMediaType;

  public SofarResponseBody(ResponseBody responseBody) {
    mMediaType = responseBody.contentType();
    try {
      Buffer buffer = new Buffer();
      mSource = buffer.readFrom(responseBody.byteStream());
      mContentLength = buffer.size();
    } catch (Exception ignored) {} finally {
      IOUtil.closeQuietly(responseBody);
    }
  }

  @Override
  public MediaType contentType() {
    return mMediaType;
  }

  @Override
  public long contentLength() {
    return mContentLength;
  }

  @Override
  public BufferedSource source() {
    return mSource;
  }
}
