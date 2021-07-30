package com.utils.library.http.download

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 下载文件的url接口
 */
interface DownloadService {
    /**
     * 直接使用网址下载文件
     * @param url 网址
     * @return Observable
     */
    @GET
    @Streaming
    fun download(@Url url: String): Observable<ResponseBody>
}