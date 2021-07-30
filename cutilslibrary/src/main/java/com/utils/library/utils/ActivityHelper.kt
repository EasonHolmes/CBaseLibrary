package com.utils.library.utils

import android.os.Handler
import com.utils.library.ui.AbstractBaseActivity


class ActivityHelper(protected val currentActivity: AbstractBaseActivity<*, *,*>) {
    val mCurrentActivity = currentActivity
    protected val mHandler: Handler = mCurrentActivity.mHandler

    /**
     * 取消加载中弹出框
     */
    @Synchronized
    fun dismissSimpleLoadDialog() {
//        mHandler.post {
//            if (simple_loading_dialog != null && simple_loading_dialog!!.isShowing) {
//                simple_loading_dialog!!.dismiss()
//            }
//            simple_loading_dialog = null
//        }
    }


//    fun toast(text: String) {
//        if (text.isEmpty()) {
//            return
//        }
//        mCurrentActivity.runOnUiThread {
//            dismissSimpleLoadDialog()
//            mCurrentActivity.snackBar(text)
//        }
//    }
}
