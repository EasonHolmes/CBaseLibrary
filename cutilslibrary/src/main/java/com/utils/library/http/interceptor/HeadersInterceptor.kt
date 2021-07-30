package com.utils.library.http.interceptor

import com.elvishew.xlog.XLog
import com.utils.library.BuildConfig
import com.utils.library.utils.cache.ShareFileDefaultUtil.Companion.instancesShareFile
import com.utils.library.utils.cache.ShareFileStr
import com.utils.library.utils.cache.UserInfo
import com.utils.library.utils.isEmptyOrNull
import com.utils.library.utils.isNotEmptyStr
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Created by cuiyang on 2018/3/22.
 */
open class HeadersInterceptor : Interceptor {
    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //为空暂时还不需要token
        val localCacheToken = UserInfo.getInstance().getToken()
        if (localCacheToken.isEmptyOrNull())
            return chain.proceed(chain.request())

        //有token就加进去
        val original = chain.request()
        val request = original.newBuilder()
            .url(original.url)
            .header("token",
              localCacheToken!!
            )
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .build()
        if (BuildConfig.DEBUG) {
            XLog.e("HeadersInterceptor===token===$localCacheToken"+"HeadersInterceptorUrl==="+request.url)
            XLog.e("HeadersInterceptor===tokenRequest==${request.headers}"+"HeadersInterceptorUrl===="+request.url)
        }
        return chain.proceed(request)
    }
}