package com.ethan.clibrary.model

import androidx.lifecycle.viewModelScope
import com.ethan.clibrary.entity.response.RetrofitFlowResponse
import com.ethan.clibrary.http.apiService
import com.ethan.clibrary.http.catchError
import com.ethan.clibrary.http.flowRequest
import com.utils.library.utils.CCLogUtils
import com.utils.library.viewmodel.AbstractModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class RetrofitFlowActModel : AbstractModel() {

    val retrofitFlowResponseFlow = MutableStateFlow(mutableListOf(RetrofitFlowResponse()))

    fun retrofitFlow() {
        viewModelScope.launch {
            flowRequest {
                retrofitFLowTest()
            }.catchError {
                CCLogUtils.e(RetrofitFlowActModel::class.java, "catch====="+Thread.currentThread().name)

            }
            .collect {
                    CCLogUtils.e(RetrofitFlowActModel::class.java, "collect====="+Thread.currentThread().name)
                    retrofitFlowResponseFlow.value = it.data!!
            }
        }
    }

    override fun onCleared() {
        CCLogUtils.e(RetrofitFlowActModel::class.java,"onCleared")
        super.onCleared()

    }
}