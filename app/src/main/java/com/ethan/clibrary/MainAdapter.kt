package com.ethan.clibrary

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ethan.clibrary.databinding.ItemMainBinding
import com.utils.library.ui.adapter.SingleTypeListAdapter
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout

class MainAdapter(ddRecyclerViewLayout: DDRecyclerViewLayout) : SingleTypeListAdapter<StringEntity, ItemMainBinding>(ddRecyclerViewLayout) {
    override fun bindingLyoaut(parent: ViewGroup): ItemMainBinding {
       return ItemMainBinding.inflate(LayoutInflater.from(parent.context),null,false)
    }

    override fun onItemBinding(binding: ItemMainBinding, item: StringEntity, position: Int) {
        binding.itemTxt.text = item.s
    }

}