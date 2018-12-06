package com.sf.base.mvp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;

/**
 * mvp中的p层，负责解耦m和v层
 *
 * 举个例子，比如我有一个界面(CameraActivity)，布局为camera.xml
 * 如何使用了？
 * CameraPresenter mPresenter = new CameraPresenter(); //继承自Presenter
 * mPresenter.create(view); //view为inflater.inflate(R.layout.camera.xml)
 * mPresenter.bind(model, this); //model为数据
 * 然后在CameraPresenter中重写onBind方法，就可以将数据设置到对应的view上了
 *
 * 当然了，如果一个页面的逻辑很复杂，可以在CameraPresenter中添加子Presenter
 * 将逻辑进一步分离add(SELF_ID, mChildPresenters[i]);
 *
 */
public class Presenter<T> {

  public static final int SELF_ID = 0;

  private View mView; // 持有v层的引用
  private T mModel; // 持有m层的引用

  private Object mCallerContext;
  private SparseArray<View> mHolder;

  private List<Pair<Presenter<T>, Integer>> mPresenters;
  private Presenter mParent;

  public Presenter() {
    mPresenters = new ArrayList<>();
  }

  // 将p与v层关联
  public final void create(View view) {
    if (isCreated()) {
      throw new IllegalStateException("Presenter只能被初始化一次.");
    }

    mView = view;
    mHolder = new SparseArray<>();

    for (Pair<Presenter<T>, Integer> pair : mPresenters) {
      onCreateInternal(pair.second, pair.first);
    }

    onCreate();
  }

  private void onCreateInternal(int elementId, Presenter<T> presenter) {
    View elementView = elementId == SELF_ID ? mView : findViewById(elementId);
    if (elementView != null) {
      presenter.mParent = this;
      presenter.create(elementView);
    }
  }

  protected void onCreate() {}

  public final void destroy() {
    for (Pair<Presenter<T>, Integer> pair : mPresenters) {
      Presenter<T> presenter = pair.first;
      if (presenter.isCreated()) {
        presenter.destroy();
      }
    }
    onDestroy();
    mModel = null;
    mCallerContext = null;
    mParent = null;
  }

  protected void onDestroy() {}


  public final void resume() {
    for (Pair<Presenter<T>, Integer> pair : mPresenters) {
      Presenter<T> presenter = pair.first;
      if (presenter.isCreated()) {
        presenter.resume();
      }
    }
    onResume();
  }

  protected void onResume() {}

  public final void pause() {
    for (Pair<Presenter<T>, Integer> pair : mPresenters) {
      Presenter<T> presenter = pair.first;
      if (presenter.isCreated()) {
        presenter.pause();
      }
    }
    onPause();
  }

  protected void onPause() {}


  // 将p于m层关联
  public final void bind(T model, Object callerContext) {
    throwIfNotCreated();

    mModel = model;
    mCallerContext = callerContext;

    for (Pair<Presenter<T>, Integer> pair : mPresenters) {
      Presenter<T> presenter = pair.first;
      if (!presenter.isCreated()) {
        // 解决异步添加View的case, 例如这样一个场景:
        // Presenter中添加了add.(R.id.player, new PlayerPresenter());
        // 这个是个player的Presenter, 但是player这个ID是在用户点击之后才会添加到ViewGroup中的,
        // 此时因为没有初始化会导致bind失败.
        // 这类case因为onCreate不强制要求ID有, 因此在bind的时候应该按需给予补充的onCreate.
        onCreateInternal(pair.second, presenter);
      }

      if (presenter.isCreated()) {
        presenter.bind(model, callerContext);
      }
    }

    onBind(model, callerContext);
  }

  protected void onBind(T model, Object callerContext) {}

  // 用于添加子Presenter
  public final void add(int id, Presenter<T> presenter) {
    mPresenters.add(new Pair<>(presenter, id));
    if (isCreated()) {
      // 新增的Presenter, 如果当前的host已经attach了, 则立刻给新节点attach
      onCreateInternal(id, presenter);
    }
  }


  public boolean isCreated() {
    return mView != null;
  }

  public View getView() {
    return mView;
  }

  public T getModel() {
    return mModel;
  }

  public Object getCallerContext() {
    return mCallerContext;
  }

  public Context getContext() {
    return mView.getContext();
  }

  private <V extends View> V findViewById(int id) {
    throwIfNotCreated();
    View result = mHolder.get(id);
    if (result != null) {
      return (V) result;
    }
    if (mView != null) {
      result = mView.findViewById(id);
    }
    mHolder.put(id, result);
    return (V) result;
  }


  private void throwIfNotCreated() {
    if (!isCreated()) {
      throw new IllegalArgumentException("This method should not be invoke before create.");
    }
  }
}
