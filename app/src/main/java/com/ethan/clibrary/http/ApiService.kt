package com.ethan.clibrary.http

import com.ethan.clibrary.entity.response.RetrofitFlowResponse
import com.utils.library.http.HttpData
import com.utils.library.http.retrofit
import retrofit2.http.GET


val apiService: ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    retrofit.create(
        ApiService::class.java
    )
}

interface ApiService {
    /***
     * 公众号列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun retrofitFLowTest():HttpData<MutableList<RetrofitFlowResponse>>
}