package com.utils.library.http.download

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * 用于线程传递ui的
 */
internal class DownloadHandler {
    /**
     * handle ,处理ui方面的onProgress
     */
    private var mHandler: Handler? = null
    private var mDownloadListener: DownloadListener? = null

    /**
     * 初始化handler
     */
    fun initHandler(downloadListener: DownloadListener) {
        if (mHandler != null) {
            return
        }
        mDownloadListener = downloadListener
        // 同步锁此类
        synchronized(DownloadHandler::class.java) {
            if (mHandler == null) {
                mHandler = object : Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message) {
                        super.handleMessage(msg)
                        if (msg == null) {
                            return
                        }
                        if (msg.what == WHAT_UPDATE) {
                            val updateData = msg.data ?: return
                            val progress = updateData.getInt(PROGRESS)
                            downloadListener.onProgress(progress)
                        }
                    }
                }
            }
        }
    }

    /**
     * 传递进度给ui
     *
     * @param progress 进度，100为满
     */
    fun onProgress(progress: Int) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mDownloadListener!!.onProgress(progress)
            return
        }
        val message = mHandler!!.obtainMessage()
        message.what = WHAT_UPDATE
        val data = Bundle()
        data.putInt(PROGRESS, progress)
        message.data = data
        mHandler!!.sendMessage(message)
    }

    companion object {
        /**
         * 更新进度
         */
        private const val WHAT_UPDATE = 0x01
        private const val PROGRESS = "progress"
    }
}