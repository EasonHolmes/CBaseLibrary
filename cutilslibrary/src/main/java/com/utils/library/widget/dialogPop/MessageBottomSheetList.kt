package com.utils.library.widget.dialogPop

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.utils.library.ui.adapter.MessageBSAdapter
import com.widget.library.R
import com.widget.library.dialog_pop.StrongBottomSheetDialog
import com.widget.library.refresh.familiarrecyclerview.FamiliarRecyclerView
import com.widget.library.refresh.recyclerview.DDRecyclerViewLayout


@SuppressLint("UseCompatLoadingForDrawables")
class MessageBottomSheetList(
    context: Context,
    listener: FamiliarRecyclerView.OnItemClickListener,
    headerTitleText:String="",footerButtonText:String=""
) : View.OnClickListener {
    @Suppress("JoinDeclarationAndAssignment")
    private val mBottomSheetDialog: StrongBottomSheetDialog
    private lateinit var recyclerView: DDRecyclerViewLayout
    private val adapter by lazy(LazyThreadSafetyMode.NONE) { MessageBSAdapter(recyclerView) }

    init {
        //BottomSheet设置圆角1.设置style 2.设置background使用drawable设置圆角
        // 注意：水波纹点击效果需要放在最上层（点击的控件之上不能有其他控件设置背景色，否则会覆盖掉），圆角就不能给item设置背景色，最多水波纹，否则圆角会失效，想要背景色在drawable里设置
        mBottomSheetDialog = StrongBottomSheetDialog(context,R.style.BottomSheetDialog)
        //mBottomSheetDialog.setMaxHeight(1600);//这个不能设置否则低版本的手机会显示不全
//        mBottomSheetDialog.setPeekHeight(1600)
        recyclerView = DDRecyclerViewLayout(context)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        if (headerTitleText.isNotEmpty())
            recyclerView.addHeaderView(getHeader(context,headerTitleText))
        recyclerView.addFooterView(getFooter(context,footerButtonText))
        recyclerView.setOnItemClickListener(listener)
        mBottomSheetDialog.setContentView(recyclerView)
        //recyclerview在dismiss后adapter的notifichangeData就无效了每次显示需要重置,这里不会有数据改变就不需要了
//        mBottomSheetDialog.setOnShowListener {
            recyclerView.adapter = adapter
//        }

        recyclerView.background = recyclerView.context.resources.getDrawable(R.drawable.radius_bottomsheet,null)
        mBottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
        mBottomSheetDialog.show()
    }

    fun setNewDatas(mutableList: MutableList<String>) {
        adapter.setNewDatas(mutableList)
    }

    fun getBottomAdapter(): MessageBSAdapter = adapter

    private fun getFooter(context: Context,txt: String): View {
        val footerView =
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet_footer, recyclerView, false)
        val footTxt = footerView.findViewById<TextView>(R.id.txt_footer)
        footTxt.setOnClickListener(this)
        footTxt.text = txt
        return footerView
    }

    private fun getHeader(context: Context,txt:String): View {
        val headerView =
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet_header, recyclerView, false)
        headerView.findViewById<TextView>(R.id.txt_header).text = txt
        return headerView
    }

    override fun onClick(view: View) {
        dismiss()
    }

    fun isShowing(): Boolean = mBottomSheetDialog.isShowing

    fun dismiss() {
        mBottomSheetDialog.dismiss()
    }

    fun show() {
        mBottomSheetDialog.show()
    }
}
