package com.ethan.clibrary.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ethan.clibrary.entity.response.RetrofitFlowResponse
import com.ethan.clibrary.http.apiService
import kotlinx.coroutines.delay
import java.util.Random

/**
 * Created by Ethan Cui on 2022/11/23
 */
private const val SHOE_START_INDEX = 0

class CustomPageDataSource : PagingSource<Int, RetrofitFlowResponse>() {
    private val dd = mutableListOf(
    RetrofitFlowResponse(name = "ethan"+Random().nextInt(50), id = Random().nextInt(100)+100),
    RetrofitFlowResponse(name = "ethan"+Random().nextInt(50), id = Random().nextInt(100)+100),
    RetrofitFlowResponse(name = "ethan"+Random().nextInt(50), id = Random().nextInt(100)+100),
    RetrofitFlowResponse(name = "ethan"+Random().nextInt(50), id = Random().nextInt(100)+100),
    RetrofitFlowResponse(name = "ethan"+Random().nextInt(50), id = Random().nextInt(100)+100)
    )
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RetrofitFlowResponse> {
        val pos = params.key ?: SHOE_START_INDEX
        val startIndex = pos * params.loadSize + 1
        val endIndex = (pos + 1) * params.loadSize
        return try {
            delay(1000)
            var datas : MutableList<RetrofitFlowResponse> ?=null
            if (pos!=20){
                datas = dd
            }
//           val datas =  apiService.retrofitFLowTest().datas
            // 返回你的分页结果，并填入前一页的 key 和后一页的 key
            LoadResult.Page(datas!!,if (pos <= SHOE_START_INDEX) null else pos - 1,
                if (datas == null) null else pos + 1)//这里加载处理数据
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, RetrofitFlowResponse>): Int? {
        return null
    }
}