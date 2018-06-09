package com.sf.sofarmusic.demo.md;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sofarmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/7/4.
 */

public class TypeFragment extends Fragment {

    private View view;
    private RecyclerView type_rv;
    private TypeListAdapter mAdapter;
    private List<String> mDatas;

    private String mType;

    public static Fragment newInstance(String type) {
        TypeFragment fragment = new TypeFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_type, container, false);
        initView();
        initData();
        return view;
    }

    private void initView() {
        type_rv = (RecyclerView) view.findViewById(R.id.type_rv);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        type_rv.setLayoutManager(layoutManager);

    }

    private void initData() {
        if (getArguments() != null) {
            mType = getArguments().getString("type");

            mDatas=new ArrayList<>();
            for (int i = 0; i < 35; i++) {
                mDatas.add(mType + (i + 1));
            }
        }

        mAdapter=new TypeListAdapter(getActivity(),mDatas);
        type_rv.setAdapter(mAdapter);

    }
}
