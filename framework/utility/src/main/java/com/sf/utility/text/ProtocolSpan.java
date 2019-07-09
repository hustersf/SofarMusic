package com.sf.utility.text;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * TextView需设置setMovementMethod(LinkMovementMethod.getInstance())，否则点击无效
 */
public class ProtocolSpan extends ClickableSpan {

  private boolean mUnderline;
  private int mLinkColor;
  private OnProtocolClickListener mListener;

  public ProtocolSpan() {
    mLinkColor = 0xFFFF5800;
  }

  public void setUnderlineText(boolean underline) {
    mUnderline = underline;
  }

  public void setLinkColor(int color) {
    mLinkColor = color;
  }

  @Override
  public void updateDrawState(TextPaint ds) {
    ds.linkColor = mLinkColor;
    ds.setFakeBoldText(true);
    super.updateDrawState(ds);
    ds.setUnderlineText(mUnderline);
  }

  @Override
  public void onClick(View widget) {
    if (mListener != null) {
      mListener.onClick();
    }
  }

  public void setOnProtocolClickListener(OnProtocolClickListener listener) {
    mListener = listener;
  }

  public interface OnProtocolClickListener {
    void onClick();
  }
}
