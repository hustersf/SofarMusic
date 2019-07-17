package com.sf.base.network.retrofit.page;

import com.sf.base.network.page.PageList;
import com.sf.base.network.page.PageListObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 *
 * @param <PAGE> 页面Response数据集
 * @param <MODEL> 页面元数据
 */
public abstract class RetrofitPageList<PAGE, MODEL> implements PageList<PAGE, MODEL> {

  private Observable<PAGE> mObservable;
  private Disposable mDisposable;

  private List<PageListObserver> mPageListObservers;
  private List<MODEL> mItems;
  private PAGE mLatestPage;

  private boolean mLoading = false;
  private boolean mInvalidated;
  private boolean mHasMore;

  public RetrofitPageList() {
    mPageListObservers = new ArrayList<>();
    mItems = new ArrayList<>();
  }

  protected abstract Observable<PAGE> onCreateRequest();

  private Consumer<PAGE> mNextConsumer = new Consumer<PAGE>() {
    @Override
    public void accept(PAGE page) {
      onLoadComplete(page);
    }
  };

  private Consumer<Throwable> mErrorConsumer = new Consumer<Throwable>() {
    @Override
    public void accept(Throwable throwable) throws Exception {
      notifyError(throwable);
    }
  };

  /**
   * 触发数据请求
   */
  @Override
  public void refresh() {
    invalidate();
    load();
  }

  @Override
  public void registerObserver(PageListObserver observer) {
    mPageListObservers.add(observer);
  }

  @Override
  public void unRegisterObserver(PageListObserver observer) {
    mPageListObservers.remove(observer);
  }

  @Override
  public List<MODEL> getItems() {
    return mItems;
  }

  /**
   * 从Response中读取是否还有下一页.
   *
   * @param response 当前请求的response.
   * @return true还有下一页，否则false.
   */
  protected abstract boolean getHasMoreFromResponse(PAGE response);

  @Override
  public PAGE getPageResponse() {
    return mLatestPage;
  }

  @Override
  public boolean isEmpty() {
    return mItems.size() == 0;
  }

  @Override
  public final void load() {
    if (mLoading) {
      return;
    }

    mLoading = true;
    mObservable = onCreateRequest();

    requestNetwork();
  }

  private void requestNetwork() {
    notifyStartLoading();
    mDisposable = mObservable.subscribe(mNextConsumer, mErrorConsumer);
  }

  private void onLoadComplete(PAGE page) {
    onLoadItemFromResponse(page, mItems);
    mLatestPage = page;
    mHasMore = getHasMoreFromResponse(page);
    mLoading = false;
    mInvalidated = false;
    notifyFinishLoading();
  }

  /**
   * 通知数据开始加载
   */
  private void notifyStartLoading() {
    for (PageListObserver observer : mPageListObservers) {
      observer.onStartLoading(isFirstPage(), isUsingCache());
    }
  }

  /**
   * 通知数据加载完成
   */
  private void notifyFinishLoading() {
    for (PageListObserver observer : mPageListObservers) {
      observer.onFinishLoading(isFirstPage(), isUsingCache());
    }
  }

  /**
   * 通知数据加载出现错误
   */
  private void notifyError(Throwable throwable) {
    for (PageListObserver observer : mPageListObservers) {
      observer.onError(isFirstPage(), throwable);
    }
    mLoading = false;
    mInvalidated = false;
  }

  public final void invalidate() {
    mInvalidated = true;
  }

  /**
   * 是否第一页数据
   */
  public boolean isFirstPage() {
    return mLatestPage == null || mInvalidated;
  }

  /**
   * 是否使用缓存数据
   */
  public boolean isUsingCache() {
    return false;
  }


  public abstract void onLoadItemFromResponse(PAGE page, List<MODEL> items);


  @Override
  public boolean hasMore() {
    return mHasMore;
  }

  public final void setHasMore(boolean hasMore) {
    mHasMore = hasMore;
  }
}
