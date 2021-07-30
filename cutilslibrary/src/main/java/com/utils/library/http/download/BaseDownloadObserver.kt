package com.utils.library.http.download

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 用于只暴露success和error
 * @param <T>
</T> */
abstract class BaseDownloadObserver<T> : Observer<T> {
    /**
     * 开始网络操作前的步骤
     * @param disposable 用于释放
     */
    abstract override fun onSubscribe(disposable: Disposable)
    override fun onNext(t: T) {
        onDownloadSuccess(t)
    }

    override fun onError(e: Throwable) {
        onDownloadError(e)
    }

    override fun onComplete() {}

    /**
     * 下载成功
     * @param t 实体
     */
    protected abstract fun onDownloadSuccess(t: T)

    /**
     * 下载异常
     * @param e 实体
     */
    protected abstract fun onDownloadError(e: Throwable?)
}