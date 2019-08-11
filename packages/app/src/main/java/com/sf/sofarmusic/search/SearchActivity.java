package com.sf.sofarmusic.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.sf.base.BaseActivity;
import com.sf.sofarmusic.R;
import com.sf.utility.KeyBoardUtil;

public class SearchActivity extends BaseActivity {

  private SearchHotFragment hotFragment;
  private SearchResultFragment resultFragment;
  private SearchCategoryTabFragment categoryTabFragment;

  private TextView headBackTv;
  private EditText searchEt;
  private TextView clearTv;
  private Fragment currentFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.search_activity);

    initView();
    initFragment();
  }

  private void initView() {
    headBackTv = findViewById(R.id.head_back);
    headBackTv.setOnClickListener(v -> {
      finish();
    });

    clearTv = findViewById(R.id.clear_tv);
    clearTv.setOnClickListener(v -> {
      searchEt.setText("");
    });

    searchEt = findViewById(R.id.search_et);
    searchEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
          clearTv.setVisibility(View.GONE);
          switchContent(currentFragment, hotFragment);
        } else {
          clearTv.setVisibility(View.VISIBLE);
          switchContent(currentFragment, resultFragment);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    searchEt.setOnEditorActionListener((v, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        KeyBoardUtil.hideKeyBoard(this);
        doSearch();
        return true;
      }
      return false;
    });
  }

  private void initFragment() {
    hotFragment = new SearchHotFragment();
    resultFragment = new SearchResultFragment();
    categoryTabFragment = new SearchCategoryTabFragment();

    currentFragment = hotFragment;
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, hotFragment)
        .commitAllowingStateLoss();
  }


  public void switchContent(Fragment from, Fragment to) {
    if (from == to) {
      return;
    }
    if (!to.isAdded()) { // 先判断是否被add过
      getSupportFragmentManager().beginTransaction()
          .hide(from).add(R.id.fragment_container, to)
          .commit();
    } else {
      getSupportFragmentManager().beginTransaction()
          .hide(from).show(to)
          .commit();
    }
    currentFragment = to;
  }

  private void doSearch() {
    switchContent(currentFragment, categoryTabFragment);
  }

  @Override
  public void finish() {
    if (currentFragment instanceof SearchHotFragment) {
      super.finish();
    } else {
      switchContent(currentFragment, hotFragment);
    }
  }
}
