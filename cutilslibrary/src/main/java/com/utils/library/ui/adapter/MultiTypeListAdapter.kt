package com.utils.library.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout
import com.utils.library.ui.AbstractBaseAdapter

/**
 * Created by cuiyang on 2018/3/28.
 *  可参考CourseFragmentAdapter
 * 使用：
 * 1.T泛型的实体类需要实现MultiTypeItem  参考CourseFeaturedEntity.ItemCourse
 *  例： inner class ItemCourse : MultiTypeListAdapter.MultiTypeItem {
 *       override fun getItemType(): Int = itemTypes
 *       var itemTypes = 0
 *
 *  itemTypes的值为item布局的layoutId
 *  例：it.result[index].itemTypes = if (itemCourse.isForMember) R.layout.item_course_type2 else R.layout.item_course
 *
 *2.布局判断使用 is
 * 例：叁考CourseFragmentAdapter
 *  override fun onItemBinding(item: CourseFeaturedEntity.ItemCourse, position: Int, binding: ViewDataBinding) {
 *  if (binding is ItemCourseBinding) {
 *      //do something
 *   }
 *  binding.itemCourseShareTxt.setOnClickListener { shareClickCallback.shareClick(item) }
 *  //do something
 *  }
 *
 *  !!!!!!!!!!!!使用viewbinding需要更改结构，目前不能直接使用
}
 */
abstract class MultiTypeListAdapter<T : MultiTypeListAdapter.MultiTypeItem>(ddRecyclerViewLayout: DDRecyclerViewLayout)
    : AbstractBaseAdapter<T>(ddRecyclerViewLayout) {

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder1: RecyclerView.ViewHolder, position: Int) {
        val holder = holder1 as BindingViewHolder<*>
        val item = getItem(position)
        onItemBinding(item, position, holder.binding)
    }

    abstract fun onItemBinding(item: T, position: Int, binding: ViewBinding)


    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemType()
    }

    public interface MultiTypeItem {
        fun getItemType(): Int

    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        //viewType直接使用getItemType传入layoutId
//        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
//                viewType, parent, false)
//        return BindingViewHolder(binding)
//    }
}