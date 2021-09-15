package com.utils.library.http

import com.shijieyun.kb.app.http.OkHttpClientSingle.Companion.getOkhttpClient
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.utils.library.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.RequestBody
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaTypeOrNull

/**
 * Created by cuiyang on 16/6/1.
 */
private var CHANGE_BASE_URL: String? = null

fun getRequestBody(json: String?): RequestBody {
    return RequestBody.create(
        "Content-Type: application/json; charset=utf-8".toMediaTypeOrNull(),
        json!!
    )
}
//用到的method再反射缓存 true为在一开始就全部反射缓存
val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .client(getOkhttpClient(BuildConfig.DEBUG))
        .baseUrl((if (CHANGE_BASE_URL != null && CHANGE_BASE_URL!!.length > 1) CHANGE_BASE_URL else "https://www.wanandroid.com/")!!)
        .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(rxJavaCallAdapterFactory) //用到的method再反射缓存 true为在一开始就全部反射缓存
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .validateEagerly(false)
        .build()
}
