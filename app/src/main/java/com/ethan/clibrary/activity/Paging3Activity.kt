package com.ethan.clibrary.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethan.clibrary.databinding.ItemMainBinding
import com.ethan.clibrary.entity.response.RetrofitFlowResponse
import com.ethan.clibrary.utils.CustomPageDataSource
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout

/**
 * Created by Ethan Cui on 2022/11/23
 */
class Paging3Activity : AppCompatActivity() {
    private val recyclerView by lazy {
        DDRecyclerViewLayout(this).apply {
            layoutManager = LinearLayoutManager(this@Paging3Activity)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
    }
    private val pagAdapter by lazy {
        PagAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(recyclerView)
        recyclerView.adapter = pagAdapter
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false,
                initialLoadSize = 5
            ), pagingSourceFactory = {
                CustomPageDataSource()
            }).flow//绑定和配置

        lifecycleScope.launchWhenCreated {
            pager.collect() {
                pagAdapter.submitData(it)//进行加载
            }
        }
        pagAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    Log.e("ethan","LoadState.NotLoading")
                }
                is LoadState.Loading -> {
                    Log.e("ethan","LoadState.Loading")
                }
                is LoadState.Error -> {
                    Log.e("ethan","LoadState.Error")
                }
            }
        }
    }


    class PagAdapter() :
        PagingDataAdapter<RetrofitFlowResponse, PagAdapter.ViewHolder>(ShoeDiffCallback()) {

        class ViewHolder(private val binding: ItemMainBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun getBinding() = binding
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.getBinding().itemTxt.text = getItem(position)?.name
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                ItemMainBinding.inflate(
                    LayoutInflater.from(parent.context),
                    null,
                    false
                )
            )
        }

        class ShoeDiffCallback : DiffUtil.ItemCallback<RetrofitFlowResponse>() {
            override fun areItemsTheSame(
                oldItem: RetrofitFlowResponse,
                newItem: RetrofitFlowResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: RetrofitFlowResponse,
                newItem: RetrofitFlowResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}