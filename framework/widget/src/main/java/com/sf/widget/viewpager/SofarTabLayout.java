package com.sf.widget.viewpager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.util.Pools;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * {@link android.support.design.widget.TabLayout}
 */
public class SofarTabLayout extends HorizontalScrollView {

  private static final Pools.Pool<Tab> sTabPool = new Pools.SynchronizedPool<>(16);
  private ArrayList<Tab> mTabs = new ArrayList<>();

  // Pool we use as a simple RecyclerBin
  private final Pools.Pool<TabView> mTabViewPool = new Pools.SimplePool<>(12);

  private final SlidingTabStrip mTabStrip;

  int mMode;
  int mTabGravity;

  /**
   * Scrollable tabs display a subset of tabs at any given moment, and can contain longer tab
   * labels and a larger number of tabs. They are best used for browsing contexts in touch
   * interfaces when users don’t need to directly compare the tab labels.
   *
   * @see #setTabMode(int)
   * @see #getTabMode()
   */
  public static final int MODE_SCROLLABLE = 0;

  /**
   * Fixed tabs display all tabs concurrently and are best used with content that benefits from
   * quick pivots between tabs. The maximum number of tabs is limited by the view’s width.
   * Fixed tabs have equal width, based on the widest tab label.
   *
   * @see #setTabMode(int)
   * @see #getTabMode()
   */
  public static final int MODE_FIXED = 1;

  @IntDef(value = {MODE_SCROLLABLE, MODE_FIXED})
  @Retention(RetentionPolicy.SOURCE)
  public @interface Mode {}

  /**
   * Gravity used to fill the {@link TabLayout} as much as possible. This option only takes effect
   * when used with {@link #MODE_FIXED}.
   *
   * @see #setTabGravity(int)
   * @see #getTabGravity()
   */
  public static final int GRAVITY_FILL = 0;

  /**
   * Gravity used to lay out the tabs in the center of the {@link TabLayout}.
   *
   * @see #setTabGravity(int)
   * @see #getTabGravity()
   */
  public static final int GRAVITY_CENTER = 1;

  @IntDef(flag = true, value = {GRAVITY_FILL, GRAVITY_CENTER})
  @Retention(RetentionPolicy.SOURCE)
  public @interface TabGravity {}


  public SofarTabLayout(Context context) {
    this(context, null);
  }

  public SofarTabLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SofarTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    mTabStrip = new SlidingTabStrip(context);
    addView(mTabStrip);
  }

  /**
   * create and return a new {@link Tab}
   */
  public Tab newTab() {
    Tab tab = sTabPool.acquire();
    if (tab == null) {
      tab = new Tab();
    }
    tab.mParent = this;
    tab.mView = createTabView(tab);
    return tab;
  }


  private TabView createTabView(@NonNull Tab tab) {
    TabView tabView = mTabViewPool.acquire();
    if (tabView == null) {
      tabView = new TabView(getContext());
    }
    return tabView;
  }

  /**
   * Add a tab to this layout. The tab will be added at the end of the list.
   * If this is the first tab to be added it will become the selected tab.
   */
  public void addTab(Tab tab) {
    addTab(tab, mTabs.isEmpty());
  }

  /**
   * Add a tab to this layout. The tab will be inserted at <code>position</code>.
   * If this is the first tab to be added it will become the selected tab.
   */
  public void addTab(Tab tab, int position) {
    addTab(tab, position, mTabs.isEmpty());
  }

  /**
   * Add a tab to this layout. The tab will be added at the end of the list.
   */
  public void addTab(Tab tab, boolean selected) {
    addTab(tab, mTabs.size(), selected);
  }

  /**
   * Add a tab to this layout. The tab will be inserted at <code>position</code>
   */
  public void addTab(Tab tab, int position, boolean selected) {
    if (tab.mParent != this) {
      throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    configureTab(tab, position);
    addTabView(tab);

    if (selected) {
      tab.select();
    }
  }

  /**
   * 更新各个tab的position
   */
  private void configureTab(Tab tab, int position) {
    tab.setPosition(position);
    mTabs.add(position, tab);

    // 更新新插入tab后面tab的位置
    for (int i = position + 1; i < mTabs.size(); i++) {
      mTabs.get(i).setPosition(i);
    }
  }

  private void addTabView(Tab tab) {
    TabView tabView = tab.mView;
    mTabStrip.addView(tabView, tab.getPosition(), createLayoutParamsForTabs());
  }

  private LinearLayout.LayoutParams createLayoutParamsForTabs() {
    final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
    updateTabViewLayoutParams(lp);
    return lp;

  }

  private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
    if (mMode == MODE_FIXED && mTabGravity == GRAVITY_FILL) {
      lp.width = 0;
      lp.weight = 1;
    } else {
      lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
      lp.weight = 0;
    }

  }

  void selectTab(Tab tab) {
    selectTab(tab, true);
  }

  void selectTab(final Tab tab, boolean updateIndicator) {

  }

  public void setTabMode(@Mode int mode) {
    if (mMode != mode) {
      mMode = mode;
      applyModeAndGravity();
    }
  }

  @Mode
  public int getTabMode(){
    return mMode;
  }

  public void setTabGravity(@TabGravity int tabGravity) {
    if (mTabGravity != tabGravity) {
      mTabGravity = tabGravity;
      applyModeAndGravity();
    }
  }

  @TabGravity
  public int getTabGravity(){
    return mTabGravity;
  }

  private void applyModeAndGravity() {

  }


  /**
   * tab container
   */
  private class SlidingTabStrip extends LinearLayout {

    public SlidingTabStrip(Context context) {
      super(context);
      setOrientation(HORIZONTAL);
    }
  }

  /**
   * a tab view
   */
  class TabView extends LinearLayout {

    public TabView(Context context) {
      super(context);
      setOrientation(VERTICAL);
    }

    final void update() {

    }
  }

  /**
   * tab info
   */
  public static final class Tab {

    private int mPosition = -1;
    private Drawable mIcon;
    private CharSequence mText;
    private View mCustomView;

    SofarTabLayout mParent;
    TabView mView;

    public void select() {
      if (mParent == null) {
        throw new IllegalArgumentException("Tab not attached to a TabLayout");
      }
      mParent.selectTab(this);
    }



    public int getPosition() {
      return mPosition;
    }

    void setPosition(int position) {
      mPosition = position;
    }

    @Nullable
    public CharSequence getText() {
      return mText;
    }

    @NonNull
    public Tab setText(CharSequence text) {
      mText = text;
      updateView();
      return this;
    }

    @NonNull
    public Tab setText(@StringRes int resId) {
      if (mParent == null) {
        throw new IllegalArgumentException("Tab not attached to a TabLayout");
      }
      return setText(mParent.getResources().getText(resId));
    }

    @Nullable
    public Drawable getIcon() {
      return mIcon;
    }

    @NonNull
    public Tab setIcon(Drawable icon) {
      mIcon = icon;
      updateView();
      return this;
    }

    @NonNull
    public Tab setIcon(@DrawableRes int resId) {
      if (mParent == null) {
        throw new IllegalArgumentException("Tab not attached to a TabLayout");
      }
      return setIcon(AppCompatResources.getDrawable(mParent.getContext(), resId));
    }


    @Nullable
    public View getCustomView() {
      return mCustomView;
    }

    @NonNull
    public Tab setCustomView(@Nullable View view) {
      mCustomView = view;
      updateView();
      return this;
    }

    public Tab setCustomView(@LayoutRes int resId) {
      final LayoutInflater inflater = LayoutInflater.from(mView.getContext());
      return setCustomView(inflater.inflate(resId, mView, false));
    }


    /**
     * 刷新tabview
     */
    void updateView() {
      if (mView != null) {
        mView.update();
      }
    }

  }
}
