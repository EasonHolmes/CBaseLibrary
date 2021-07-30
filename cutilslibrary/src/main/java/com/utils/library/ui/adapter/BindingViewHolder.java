package com.utils.library.ui.adapter;


import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * @author cuiyang on 19/6/26
 */
public class BindingViewHolder<T extends ViewBinding>
        extends RecyclerView.ViewHolder {

    private T mBinding;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }
}
