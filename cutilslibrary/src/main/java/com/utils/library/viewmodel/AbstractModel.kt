package com.utils.library.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.utils.library.BuildConfig
import com.utils.library.http.HttpData
import com.utils.library.utils.livedata.SingleLivedata
import kotlinx.coroutines.flow.MutableStateFlow

abstract class AbstractModel : ViewModel() {
    val apiExceptionEvent: SingleLivedata<Throwable> by lazy { SingleLivedata() }
    val errorCodeOptionEvent : SingleLivedata<Int> by lazy { SingleLivedata() }

    override fun onCleared() {
        super.onCleared()
        if (BuildConfig.DEBUG) Log.d(this.toString(), "onCleared")
    }
}
