package com.utils.library.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.utils.library.BuildConfig

abstract class AbstractModel : ViewModel(){
    override fun onCleared() {
        super.onCleared()
        if (BuildConfig.DEBUG) Log.e(this.toString(),"onCleared")
    }
}
