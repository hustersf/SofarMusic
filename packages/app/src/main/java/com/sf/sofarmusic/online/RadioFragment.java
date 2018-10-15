package com.sf.sofarmusic.online;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sofarmusic.R;
import com.sf.base.BaseFragment;

/**
 * Created by sufan on 16/11/9.
 * 电台
 */

public class RadioFragment extends BaseFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_radio, container, false);
        return view;
    }
}
