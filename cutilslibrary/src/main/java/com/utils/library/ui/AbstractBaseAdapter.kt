package com.utils.library.ui

import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.widget.library.R
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout



/**
 * Created by cuiyang on 16/6/3.
 * /**/**
 * 不需要上拉下拉时refreshLayout可传null
 * 这里只是控制在数据过于少和数据量恢复后停止和恢复上拉下拉加载
 */
abstract class AbstractBaseAdapter<T>(private val ddRecyclerViewLayout: DDRecyclerViewLayout) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData = mutableListOf<T>()
    private val mDecelerateInterpolator = DecelerateInterpolator(2f)
    private val mDuration = 500
    private val mDelay = 30
    private var mLastPosition = -1
    private var isFirstOnly = true
    private var refresh = true
    var recyclerviewEmptyTxt = ""
    val data: MutableList<T>
        get() = mData

    fun addDatas(data: MutableList<T>?) {
        setNewDatas(data, false)
    }

    /**
     * data默认为空不为null，使用时不需要做data是否为空逻辑方法已处理好。
     * 如果只是简单的加载数据，直接使用，
     * 如果有对数据处理有特殊需求自行处理
     */
    fun setNewDatas(data: MutableList<T>?, refresh: Boolean = true, needEmptyTip: Boolean = true) {
        this.refresh = refresh
        if (!refresh && data != null) {
            val p = this.mData.size
            this.mData.addAll(data)
            //p 更改起始位置  data.size已更改的数目
            //notifyItemRangeChanged(p, data.size)
            notifyItemRangeInserted(p, data.size)
        } else {//下拉刷新
            if (data != null) {
                this.mData.clear()
                this.mData.addAll(data)
//                if (mData.size > 2) {
//                    notifyItemRangeChanged(0, mData.size)
//                } else {
                notifyDataSetChanged()
//                }
                //当上拉加载到底无数据自动禁掉上拉加载后，如果再下拉刷新判断如果设置了上拉加载就再打开
                if (ddRecyclerViewLayout.getLoadmoreListener() != null && mData.size > 0) {
                    ddRecyclerViewLayout.setEnableLoadeMore(true)
                    ddRecyclerViewLayout.setFooterEmptyViewTip(boolean = false, isRefresh = true)
                }
            }
        }
        setLoadMore(data, needEmptyTip)
    }

    fun clearData(animation: Boolean = false) {
        mData.clear()
        if (mData.size > 0) {
            val size = mData.size
            if (animation) {
                notifyItemRangeRemoved(0, size)
            }
        }
    }


    /**
     * 当没有数据时不再上拉加载
     */
    private fun setLoadMore(data: MutableList<T>?, needEmptyTip: Boolean = true) {
        if (mData.isEmpty() && needEmptyTip) {
            if (recyclerviewEmptyTxt.isNotEmpty())
                ddRecyclerViewLayout.setEmptyViewSetTxt(txtResrouce = recyclerviewEmptyTxt)
            else
                ddRecyclerViewLayout.setEmptyViewSetTxt(txtResrouceId = R.string.list_tip_empty)
        }
        if ((data == null || data.isEmpty()) && ddRecyclerViewLayout.getRefreshListener() != null) {
            ddRecyclerViewLayout.setEnableLoadeMore(false)
            ddRecyclerViewLayout.setFooterEmptyViewTip(true)
        }
        if (mData.isEmpty()) {
            ddRecyclerViewLayout.setFooterEmptyViewTip(false)
        }
    }
    fun addDatas(data: T, position: Int = 0) {
        if (mData.size > 0) {
            this.mData.add(position, data)
            notifyItemInserted(position)
        } else {
            this.mData.add(data)
            notifyDataSetChanged()
        }
    }

    fun removeOneData(position: Int) {
        this.mData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): T {
        return mData[position]
    }

    override fun getItemCount(): Int {
        return mData.size
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
//                setItemLayoutResource(), parent, false)
//        return BindingViewHolder(binding)
//    }

    //    @Override
    //    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    //        BindingViewHolder viewHolder = (BindingViewHolder) holder;
    //        ItemNewPurchaseBinding binding = (ItemNewPurchaseBinding) viewHolder.getBinding();
    //    }


//    /**
//     * item动画
//
//     * @param anim
//     * *
//     * @param holder
//     */
//    protected fun OpenAnimation(anim: AdapterAnim, holder: RecyclerView.ViewHolder) {
//        //第一次进入或者没有被显示过的item才会显示动画.否则显示过的就不再次显示为了性能着想
//        if (!isFirstOnly || holder.layoutPosition > mLastPosition) {
//            when (anim) {
//                AdapterAnim.SCALE -> {
//                    val scaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.6f, 1f)
//                    val scaleY = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.6f, 1f)
//                    startAnim(scaleX, scaleY)
//                }
//                AdapterAnim.TRANSLATE -> {
//                    val translateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", 300f, 1f)
//                    startAnim(translateY)
//                }
//            }
//            mLastPosition = holder.layoutPosition//存储已经显示过的最大position,最后一个显示过的动画位置
//        }
//    }
//
//    /**
//     * set anim to start when loading
//     */
//    protected fun startAnim(vararg animators: Animator) {
//        for (anim in animators) {
//            anim.setDuration(mDuration.toLong()).startDelay = mDelay.toLong()
//            // * mLastPosition
//            anim.start()
//            anim.interpolator = mDecelerateInterpolator
//        }
//    }

//    /**
//     * 是否每个item只显示一次动画.默认为true
//
//     * @param firstOnly
//     */
//    protected fun setFirstOnly(firstOnly: Boolean) {
//        this.isFirstOnly = firstOnly
//    }

//    public enum class AdapterAnim(val type: Int) {
//        SCALE(0),
//        TRANSLATE(1)
//    }
}
