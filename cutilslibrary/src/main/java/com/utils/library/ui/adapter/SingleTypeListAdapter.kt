package com.utils.library.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout
import com.utils.library.ui.AbstractBaseAdapter

/**
 * Created by cuiyang
 * 单类型item adapter
 * 不需要上拉下拉时refreshLayout可传null
 * 这里只是控制在数据过于少和数据量恢复后停止和恢复上拉下拉加载
*/
abstract class SingleTypeListAdapter<T, B : ViewBinding>(ddRecyclerViewLayout: DDRecyclerViewLayout) : AbstractBaseAdapter<T>(ddRecyclerViewLayout) {
    private lateinit var _binding:B
    protected val binding get() = _binding


    override fun onBindViewHolder(holder1: RecyclerView.ViewHolder, position: Int) {
        val holder = holder1 as BindingViewHolder<*>
        val item = getItem(position)
        @Suppress("UNCHECKED_CAST")
        val binding = holder.binding as B
        onItemBinding(binding, item, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
//                setItemLayoutResource(), parent, false)
        _binding = bindingLyoaut(parent)
        return BindingViewHolder(binding)

    }
    protected abstract fun bindingLyoaut(parent: ViewGroup): B

    abstract fun onItemBinding(binding: B, item: T, position: Int)
}
