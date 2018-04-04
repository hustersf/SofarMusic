package com.sf.sofarmusic.demo.list.sort;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.enity.BankItem;
import com.sf.sofarmusic.util.FontUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sufan on 17/6/18.
 */

public class BankListActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private SideBarView sideBar;
    private RecyclerView bank_rv;
    private TextView letter_tv;

    private List<BankItem> mBankList;
    private BankListAdapter mAdapter;
    private String[] banks = new String[]{"未知银行", "光大银行", "广发银行", "杭州银行", "华夏银行",
            "民生银行", "平安银行", "浦发银行", "上海银行", "兴业银行", "招商银行", "中信银行"};
    private String[] imageNames = new String[]{"xxx", "gdyh", "gfyh", "hzyh", "hxyh",
            "msyh", "payh", "pfyh", "shyh", "xyyh", "zsyh", "zxyh"};

    private String[] hotBanks = new String[]{"交通银行", "农业银行", "中国银行", "工商银行", "建设银行"};
    private String[] hotImageNames = new String[]{"jtyh", "nyyh", "zgyh", "gsyh", "jsyh"};


    private PinyinComparator pComparator;
    private CharacterParser cParser;


    private boolean mMove;
    private int mPosition;  //点击位置


    //搜索查询相关
    private EditText search_et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_bank_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        sideBar = (SideBarView) findViewById(R.id.sidebar);
        bank_rv = (RecyclerView) findViewById(R.id.bank_rv);
        letter_tv = (TextView) findViewById(R.id.letter_tv);

        search_et = (EditText) findViewById(R.id.search_et);

        bank_rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        mBankList = new ArrayList<>();
        // 填充银行的数据
        cParser = CharacterParser.getInstance();
        pComparator = new PinyinComparator();
        filledBankData();
        Collections.sort(mBankList, pComparator); // 排序

        filledHotBankData();

        mAdapter = new BankListAdapter(this, mBankList);
        bank_rv.setAdapter(mAdapter);
    }

    @Override
    public void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("可模糊查询的排序列表");

        head_right.setVisibility(View.GONE);

    }
    @Override
    public void initEvent() {

        sideBar.setonTouchChangeListener(new SideBarView.onTouchChangeListener() {

            @Override
            public void setLetter(String letter) {
                letter_tv.setText(letter);
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    // bank_rv.smoothScrollToPosition(position);  // 使当前位置在视野内
                    mPosition = position;
                    scroll2Top(position);
                }

            }
        });
        sideBar.setonTouchListener(new SideBarView.onTouchListener() {

            @Override
            public void setVisibility(int visibility) {
                letter_tv.setVisibility(visibility);

            }
        });

        bank_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mMove) {
                    mMove = false;
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int n = mPosition - manager.findFirstVisibleItemPosition();
                    if (n >= 0 && n < bank_rv.getChildCount()) {
                        bank_rv.scrollBy(0, bank_rv.getChildAt(n).getTop()); //滚动到顶部
                    }
                }
            }
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    //填充热门银行
    private void filledHotBankData() {
        //添加热门银行
        for (int i = hotBanks.length - 1; i >= 0; i--) {
            BankItem item = new BankItem();
            item.isHot = true;
            item.name = hotBanks[i];
            item.imgName = "demo_" + hotImageNames[i];
            if (i == 0) {
                item.isShowHotTitle = true;
            }

            String pinyin = cParser.getSelling(hotBanks[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                item.letter = sortString;
            } else {
                item.letter = "#";
            }
            mBankList.add(0, item);
        }

    }

    // 根据banks[] 填充banklist
    private void filledBankData() {
        for (int i = 0; i < banks.length; i++) {
            BankItem item = new BankItem();
            item.name = banks[i];
            item.imgName = "demo_" + imageNames[i];

            String pinyin = cParser.getSelling(banks[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                item.letter = sortString;
            } else {
                item.letter = "#";
            }
            mBankList.add(item);
        }
    }


    private void filterData(String filterStr) {
        List<BankItem> filterData = new ArrayList<>();
        if ("".equals(filterStr)) {
            filterData = mBankList;
        } else {
            filterData.clear();
            for (BankItem item : mBankList) {
                String name = item.name;
                if (name.indexOf(filterStr) != -1
                        || cParser.getSelling(name).startsWith(filterStr)) {
                    filterData.add(item);
                }
            }
        }
        mAdapter.updateList(filterData);
    }


    //配合addOnScrollListener的监听使得指定位置滚动到最顶部
    private void scroll2Top(int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) bank_rv.getLayoutManager();
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        int lastPosition = layoutManager.findLastVisibleItemPosition();

        if (position <= firstPosition) {
            bank_rv.smoothScrollToPosition(position);
        } else if (position <= lastPosition) {
            int top = bank_rv.getChildAt(position - firstPosition).getTop();
            bank_rv.scrollBy(0, top);
        } else {
            bank_rv.scrollToPosition(position);    //先让当前view滚动到列表内
            mMove = true;
        }
    }
}
