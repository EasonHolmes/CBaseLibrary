package com.utils.library.http.download

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * 处理返回数据
 */
internal class DownloadResponseBody internal constructor(
    private val responseBody: ResponseBody?,
    private val mDownloadListener: DownloadListener
) : ResponseBody() {
    private val mDownloadHandler = DownloadHandler()

    /**
     * BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
     */
    private var bufferedSource: BufferedSource? = null
    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength()!!
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody?.source())?.buffer()
        }
        return bufferedSource as BufferedSource
    }

    /**
     * 处理数据
     * @param source 数据源
     * @return 返回处理后的数据源
     */
    private fun source(source: Source?): Source? {
        source?.let {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L

                @Throws(IOException::class)
                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    if (bytesRead != -1L) {
                        // 回调进度ui
                        mDownloadHandler.onProgress((totalBytesRead * 100 / responseBody?.contentLength()!!).toInt())
                    }
                    return bytesRead
                }
            }
        }
        return null
    }

    init {
        mDownloadHandler.initHandler(mDownloadListener)
    }
}