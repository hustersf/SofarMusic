package com.sf.demo.md;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sf.demo.R;

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
        final StaggeredGridLayoutManager
          layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        type_rv.setLayoutManager(layoutManager);

        type_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int[] lastPos = layoutManager.findLastVisibleItemPositions(null);
                Log.d("TAG","last:"+findMax(lastPos));
            }
        });

    }

    int findMax(@NonNull int[] array){
        if (array == null || array.length == 0) {
          //  throw new Exception("array can't be empty");
        }
        int result = array[0];
        for (int num : array) {
            if (num > result) {
                result = num;
            }
        }
        return result;
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
