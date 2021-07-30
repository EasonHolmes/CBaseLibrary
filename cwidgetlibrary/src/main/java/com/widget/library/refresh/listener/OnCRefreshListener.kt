package com.widget.library.refresh.listener

import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener


/**
 * Created by cuiyang on 2017/7/24.
 */

interface OnCRefreshListener : OnRefreshListener {
//    override fun onRefresh(refreshlayout: RefreshLayout?) {
//        onRefresh()
//    }
    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefresh()
    }

    fun onRefresh()
}
