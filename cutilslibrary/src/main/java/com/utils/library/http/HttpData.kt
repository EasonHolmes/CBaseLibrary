package com.utils.library.http

import androidx.lifecycle.LiveData


/**
 * Created by cuiyang on 16/6/3.
 */
open class HttpData<T>  {
    var code = 0
    val data: T? = null
    var message: String = ""
    var itemType = 0
    val isSuccess: Boolean
        get() = code == 0

    constructor() {}

    constructor(code: Int, message: String) {
        this.code = code
        this.message = message
    }

    @Throws(ApiException::class)
    fun throwAPIException() {
        if (!isSuccess) {
            throw ApiException(code, message)
        }
    }
}