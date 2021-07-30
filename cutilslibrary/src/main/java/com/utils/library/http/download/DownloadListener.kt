package com.utils.library.http.download

import java.io.File

/**
 * 下载进度回调
 */
interface DownloadListener {
    /**
     * 开始下载
     */
    fun onStartDownload()

    /**
     * 下载进度
     * @param progress 进度数值，1-100
     */
    fun onProgress(progress: Int)

    /**
     * 下载完成
     * @param file 文件
     */
    fun onFinishDownload(file: File?)

    /**
     * 下载失败
     * @param ex 异常信息
     */
    fun onFail(ex: Throwable?)
}