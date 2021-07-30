package com.utils.library.http

import androidx.lifecycle.LiveData


/**
 * Created by cuiyang on 16/6/3.
 */
open class HttpData<T> : LiveData<HttpData<T>> {
    var code = 0
    val data: T? = null
    var message: String? = null
    var itemType = 0
    val isSuccess: Boolean
        get() = code == 0

    constructor() {}
    constructor(code: Int, message: String?) {
        this.code = code
        this.message = message
    }
}