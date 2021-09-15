package com.ethan.clibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ethan.clibrary.databinding.ItemMainBinding
import com.utils.library.ui.adapter.SingleTypeListAdapter
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout

class MainAdapter(ddRecyclerViewLayout: DDRecyclerViewLayout) : SingleTypeListAdapter<String, ItemMainBinding>(ddRecyclerViewLayout) {
    override fun bindingLyoaut(parent: ViewGroup): ItemMainBinding {
       return ItemMainBinding.inflate(LayoutInflater.from(parent.context),null,false)
    }

    override fun onItemBinding(binding: ItemMainBinding, item: String, position: Int) {
        binding.itemTxt.text = item
    }

}