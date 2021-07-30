package com.utils.library.http.download

import androidx.annotation.NonNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 处理数据
 */
internal class DownloadInterceptor internal constructor(private val downloadListener: DownloadListener) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        return response.newBuilder().body(DownloadResponseBody(response.body, downloadListener)).build()
    }
}