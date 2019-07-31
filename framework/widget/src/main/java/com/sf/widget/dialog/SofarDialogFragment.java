package com.sf.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.utility.DensityUtil;
import com.sf.widget.R;

/**
 * 在切换系统语言时，Activity会重建
 * 
 * 但是之前的DialogFragment会恢复，导致会有两个弹窗，需要主动在onDestroy中调用dismiss
 */
public class SofarDialogFragment extends BaseDialogFragment {

  private CharSequence mTitle;
  private CharSequence mMessage;
  private CharSequence mPositiveText;
  private CharSequence mNegativeText;
  private boolean mCancelable = true;
  private OnClickListener mNegativeClickListener;
  private OnClickListener mPositiveClickListener;
  private DialogInterface.OnDismissListener mOnDismissListener;
  private DialogInterface.OnCancelListener mOnCancelListener;


  private TextView mTitleTv;
  private TextView mContentTv;
  private TextView mPositiveTv;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    setCancelable(mCancelable);
    setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Dialog_Translucent);
    super.onCreate(savedInstanceState);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return super.onCreateDialog(savedInstanceState);
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.dialog_sofar, container, false);
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (mOnDismissListener != null) {
      mOnDismissListener.onDismiss(dialog);
    }
  }


  @Override
  public void onCancel(DialogInterface dialog) {
    super.onCancel(dialog);
    if (mOnCancelListener != null) {
      mOnCancelListener.onCancel(dialog);
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mTitleTv = view.findViewById(R.id.tv_title);
    mContentTv = view.findViewById(R.id.tv_content);
    mPositiveTv = view.findViewById(R.id.btn_positive);

    initView();
    initEvent();
  }

  private void initView() {
    if (TextUtils.isEmpty(mTitle)) {
      mTitleTv.setVisibility(View.GONE);
    } else {
      mTitleTv.setText(mTitle);
    }

    if (TextUtils.isEmpty(mMessage)) {
      mContentTv.setVisibility(View.GONE);
    } else {
      mContentTv.setText(mMessage);
      mContentTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    if (TextUtils.isEmpty(mPositiveText)) {
      mPositiveTv.setVisibility(View.GONE);
    } else {
      mPositiveTv.setText(mPositiveText);
    }
  }

  private void initEvent() {
    mPositiveTv.setOnClickListener(v -> {
      dismiss();
      if (mPositiveClickListener != null) {
        mPositiveClickListener.onClick(this);
      }
    });
  }


  @Override
  public void onStart() {
    super.onStart();
    // 在此设置宽高才生效
    getDialog().getWindow().setLayout(DensityUtil.dp2px(getActivity(), 270),
        ViewGroup.LayoutParams.WRAP_CONTENT);
  }

  public interface OnClickListener {
    void onClick(SofarDialogFragment dialog);
  }

  public static final class Builder {

    private final Context mContext;
    private CharSequence mTitle;
    private CharSequence mMessage;
    private CharSequence mPositiveText;
    private CharSequence mNegativeText;
    private boolean mCancelable = true;
    private OnClickListener mNegativeClickListener;
    private OnClickListener mPositiveClickListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnCancelListener mOnCancelListener;

    public Builder(Context context) {
      mContext = context;
    }

    public Builder setTitle(@StringRes int titleId) {
      return setTitle(mContext.getText(titleId));
    }

    public Builder setTitle(CharSequence title) {
      mTitle = title;
      return this;
    }

    public Builder setMessage(@StringRes int messageId) {
      return setMessage(mContext.getText(messageId));
    }

    public Builder setMessage(CharSequence message) {
      mMessage = message;
      return this;
    }

    public Builder setPositiveButton(int textId, final OnClickListener listener) {
      return setPositiveButton(mContext.getText(textId), listener);
    }

    public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
      mPositiveText = text;
      mPositiveClickListener = listener;
      return this;
    }

    public Builder setNegativeButton(int textId, final OnClickListener listener) {
      return setNegativeButton(mContext.getText(textId), listener);
    }

    public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
      mNegativeText = text;
      mNegativeClickListener = listener;
      return this;
    }

    public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
      this.mOnDismissListener = onDismissListener;
      return this;
    }

    public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
      this.mOnCancelListener = onCancelListener;
      return this;
    }

    public Builder setCancelable(boolean cancelable) {
      this.mCancelable = cancelable;
      return this;
    }

    public SofarDialogFragment build() {
      SofarDialogFragment dialog = new SofarDialogFragment();
      dialog.mTitle = mTitle;
      dialog.mMessage = mMessage;
      dialog.mPositiveText = mPositiveText;
      dialog.mNegativeText = mNegativeText;
      dialog.mPositiveClickListener = mPositiveClickListener;
      dialog.mNegativeClickListener = mNegativeClickListener;
      dialog.mOnDismissListener = mOnDismissListener;
      dialog.mOnCancelListener = mOnCancelListener;
      dialog.mCancelable = mCancelable;
      return dialog;
    }
  }

}
