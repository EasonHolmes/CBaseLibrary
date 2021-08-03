package com.utils.library.utils

import android.util.Log

import com.utils.library.BuildConfig


/**
 * Created by cuiyang on 16/6/2.
 */
object CCLogUtils {

    fun v(cls: Class<*>, msg: String="") {
        if (BuildConfig.DEBUG) {
            Log.v(cls.name, msg)
        }
    }

    fun e(cls: Class<*>, msg: String= "") {
        if (BuildConfig.DEBUG) {
            Log.e(cls.name, msg)
        }
    }

    fun d(cls: Class<*>, msg: String= "") {
        if (BuildConfig.DEBUG) {
            Log.d(cls.name, msg)
        }
    }

    fun throwable(cls: Class<*>, errorMsg: String= "") {
        if (BuildConfig.DEBUG) {
            Log.e(cls.name, "throwable==$errorMsg")
        }
    }

//    fun printStackTrace(e: Exception) {
//        if (BuildConfig.DEBUG) {
//            Log.e(KbLogUtils::class.java.name, "Exception==" + e.toString())
//        }
//    }
//
//    fun i(cls: Class<*>, msg: String= "") {
//        if (BuildConfig.DEBUG) {
//            Log.e(cls.name, msg)
//        }
//    }

}
