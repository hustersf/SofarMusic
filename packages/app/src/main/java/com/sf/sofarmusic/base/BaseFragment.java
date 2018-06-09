package com.sf.sofarmusic.base;

import android.content.Context;

import com.sf.libskin.base.SkinBaseFragment;

/**
 * Created by sufan on 17/2/28.
 */

public class BaseFragment extends SkinBaseFragment {

    public  BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (BaseActivity) context;
    }

}
