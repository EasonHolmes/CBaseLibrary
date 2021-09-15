package com.utils.library.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.widget.library.databinding.ItemMessageBottomsheetBinding
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout


class MessageBSAdapter(ddRecyclerViewLayout: DDRecyclerViewLayout) : SingleTypeListAdapter<String, ItemMessageBottomsheetBinding>(ddRecyclerViewLayout){

    override fun bindingLyoaut(parent: ViewGroup): ItemMessageBottomsheetBinding {
        return ItemMessageBottomsheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onItemBinding(
        binding: ItemMessageBottomsheetBinding,
        item: String,
        position: Int
    ) {
        binding.txtContent.text = item
    }

}

