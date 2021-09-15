package com.utils.library.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent

/**
 * Created by cuiyang on 16/5/11.
 */
interface BaseContract {

    interface BaseView {

        /**
         * 程序错误返回
         *
         * @param error
         */
//        fun refreshError(error: Throwable?)

    }

    interface BasePresenter {
        // d
        @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
        fun dettach()
    }
}
