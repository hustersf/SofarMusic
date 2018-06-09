package com.sf.sofarmusic.local;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.base.PlayerBaseActivity;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 16/12/2.
 */

public class ShowDetailActivity extends PlayerBaseActivity implements SingleAdapter.OnItemClickListener {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private RecyclerView show_rv;
    private SingleAdapter showAdapter;
    private List<PlayItem> mPlayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_detail);
        initHead();
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        // mPlayList = (List<PlayItem>) bundle.getSerializable("list");
        mPlayList = Constant.sPreList;

        showAdapter = new SingleAdapter(baseAt, mPlayList);
        show_rv.setAdapter(showAdapter);
        showAdapter.setOnItemClickListener(this);

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        show_rv = (RecyclerView) findViewById(R.id.show_rv);
        show_rv.setLayoutManager(new LinearLayoutManager(baseAt));

    }

    private void initEvent() {

    }

    private void initHead() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");

        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText(title);

        head_right.setText("管理");
        head_right.setVisibility(View.GONE);
        head_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.startShort(baseAt, "管理");
            }
        });

    }


    @Override
    public void OnSingleItem(int position) {

        //刷新底部播放栏数据
        Constant.sPlayList = new ArrayList<>();
        Constant.sPlayList.addAll(mPlayList);
        //   Constant.sPlayList = mPlayList;  这样是同一个数据源，

        //存进数据库
        PlayStatus.getInstance(this).setType(PlayStatus.LOCAL);
        PlayStatus.getInstance(this).setPosition(position > 0 ? position - 1 : 0);
        PlayList.getInstance(this).savePlayList(Constant.sPlayList);
        try {
            iBinder.play();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        updateBottom();

        //告诉LocalActivity界面改变数据
        setResult(100);
    }

    @Override
    protected void updateSongList() {
        super.updateSongList();
        //刷新本页面数据
        if (showAdapter != null)
            for (int i = 0; i < mPlayList.size(); i++) {
                if (mPlayList.get(i).isSelected) {
                    showAdapter.refreshList(i);
                    show_rv.scrollToPosition(i + 5);
                }
            }
    }
}
