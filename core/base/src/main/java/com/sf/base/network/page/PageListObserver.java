package com.sf.base.network.page;

/**
 * 监听数据请求过程
 */
public interface PageListObserver {

    /**
     * 开始请求数据
     * @param firstPage 本次加载的数据是否是第一页
     * @param cache     是否使用的是缓存数据
     */
    void onStartLoading(boolean firstPage,boolean cache);

    /**
     * 数据请求成功
     * @param firstPage 本次加载的数据是否是第一页
     * @param cache     是否使用的是缓存数据
     */
    void onFinishLoading(boolean firstPage,boolean cache);

    /**
     * 数据请求发生错误
     * @param firstPage 本次加载的数据是否是第一页
     * @param throwable 异常信息
     */
    void onError(boolean firstPage,Throwable throwable);
}
