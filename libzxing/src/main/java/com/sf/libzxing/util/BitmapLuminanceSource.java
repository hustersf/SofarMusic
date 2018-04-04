package com.sf.libzxing.util;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

public class BitmapLuminanceSource extends LuminanceSource {

	private byte[] bitmapPixels;

	  public BitmapLuminanceSource(Bitmap bitmap)
	  {
	    super(bitmap.getWidth(), bitmap.getHeight());

	    int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
	    this.bitmapPixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
	    bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());

	    for (int i = 0; i < data.length; i++)
	      this.bitmapPixels[i] = ((byte)data[i]);
	  }

	  public byte[] getMatrix()
	  {
	    return this.bitmapPixels;
	  }

	  public byte[] getRow(int y, byte[] row)
	  {
	    System.arraycopy(this.bitmapPixels, y * getWidth(), row, 0, getWidth());
	    return row;
	  }

}
