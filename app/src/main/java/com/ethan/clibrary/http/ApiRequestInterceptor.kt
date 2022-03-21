package com.ethan.clibrary.http

import com.ethan.clibrary.model.RetrofitFlowActModel
import com.utils.library.http.HttpData
import com.utils.library.http.handlerApiException
import com.utils.library.utils.CCLogUtils
import com.utils.library.viewmodel.AbstractModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
//参考https://juejin.cn/post/6963555072814874661
suspend fun <T> AbstractModel.flowRequest(
    request: suspend ApiService.() -> HttpData<T>?
): Flow<HttpData<T>> {
    return flow {
        val response = request(apiService) ?: throw IllegalArgumentException("数据非法，获取响应数据为空")
        response.throwAPIException();
        emit(response)
    }.map {
        if (!it.isSuccess && it.message.isNotEmpty())
            errorCodeOptionEvent.value = it.code
        it.isSuccess
        it
    }.flowOn(Dispatchers.IO)
//        .catch {
//            apiExceptionEvent.value = it
//            CCLogUtils.e(NetViewAbstractModelModel::class.java,"catch=====" + Thread.currentThread().name)
//        }
        .onCompletion { cause ->//成功请求cause是null
            CCLogUtils.e(AbstractModel::class.java,"onCompletion=====" + Thread.currentThread().name)
            CCLogUtils.e(AbstractModel::class.java, "onCompletion=====" + cause.toString())
        }
}
fun <T> Flow<T>.catchError(bloc: Throwable.() -> Unit) = catch { cause -> bloc(cause) }

suspend fun <T> Flow<T>.next(bloc: suspend T.() -> Unit): Unit = catch { }.collect { bloc(it) }