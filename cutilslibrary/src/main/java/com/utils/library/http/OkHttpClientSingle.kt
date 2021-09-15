package com.shijieyun.kb.app.http

import com.utils.library.http.interceptor.ConvertJsonHttpInterceptor
import com.utils.library.http.interceptor.DebugInterceptor
import com.utils.library.http.interceptor.HeadersInterceptor
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * @author cuiyang
 * @date 15/12/22反应都没有
 * 别忘了初始化xlog要不什么
 */
class OkHttpClientSingle private constructor() {
    companion object {
        fun getOkhttpClient(debug: Boolean): OkHttpClient {
            //利用lazy实现延懒汉单例
            val okHttpClient: OkHttpClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                val okHttpBuilder = OkHttpClient.Builder()
                val dispatcher = Dispatcher()
                dispatcher.maxRequests = 1//最大一个并发
                okHttpBuilder.dispatcher(dispatcher)
                //cookie持久化
//        okHttpBuilder!!.cookieJar(
//            PersistentCookieJar(
//                SetCookieCache(), SharedPrefsCookiePersistor(
//            MyApplication.instance)
//            )
//        )
                okHttpBuilder.readTimeout(30, TimeUnit.SECONDS)
                okHttpBuilder.writeTimeout(30, TimeUnit.SECONDS)
                okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS)
                okHttpBuilder.retryOnConnectionFailure(false)
//            okHttpBuilder.addInterceptor(TokenInterceptor())
//                okHttpBuilder.addInterceptor(HeadersInterceptor())
                //okHttpBuilder.retryOnConnectionFailure(false);//设置出现错误是否进行重新连接。
                okHttpBuilder.addInterceptor(ConvertJsonHttpInterceptor())
                if (debug) {
                    okHttpBuilder.addInterceptor(DebugInterceptor())
                    //ssl证用
//            val ssl = HttpsUtilsSSL.getSslSocketFactory(arrayOf(MyApplication.instance.resources.openRawResource(
//                R.raw.charles_ssl_proxying_certificate)), null, null)
//            okHttpBuilder!!.sslSocketFactory(ssl.sSLSocketFactory, ssl.trustManager).build()
                }
//                okHttpBuilder.addInterceptor(BodyInterceptor())
                okHttpBuilder.build()
            }
            return okHttpClient
        }
    }
}
