package com.ethan.clibrary

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethan.clibrary.databinding.ActivityMainBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.utils.library.helper.ActivityStackManager
import com.utils.library.ui.AbstractBaseActivity
import com.utils.library.utils.CCLogUtils
import com.widget.library.anim.coordinator_anim.CommonBehavior
import com.widget.library.refresh.familiarrecyclerview.baservadapter.FamiliarEasyAdapter
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient

class MainActivity : AbstractBaseActivity<ActivityMainBinding, MainModel>() {
    private val mData =
        mutableListOf<StringEntity>(
            StringEntity("ddddd"), StringEntity("ddd"), StringEntity("ff"),
            StringEntity("ddddd"), StringEntity("ddd"), StringEntity("ff"),
            StringEntity("ddddd"), StringEntity("ddd"), StringEntity("ff"),
        )

    override fun setBindinglayout(): ActivityMainBinding {
       return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        val adapter = MainAdapter(binding.rv)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
        adapter.setNewDatas(mData)
        mViewmodel.testFun()

//        val layoutManager = binding.rv.getLayoutManager() as LinearLayoutManager
//        val firstPosition = layoutManager.findFirstVisibleItemPosition()
//        val lastPosition = layoutManager.findLastVisibleItemPosition()
//        val rvRect = Rect()
//        binding.rv.getGlobalVisibleRect(rvRect)
//        for (i in firstPosition..lastPosition) {
//            val rowRect = Rect()
//            layoutManager.findViewByPosition(i)!!.getGlobalVisibleRect(rowRect)
//            var percentFirst: Int
//            percentFirst = if (rowRect.bottom >= rvRect.bottom) {
//                val visibleHeightFirst: Int = rvRect.bottom - rowRect.top
//                visibleHeightFirst * 100 / layoutManager.findViewByPosition(i)!!.height
//            } else {
//                val visibleHeightFirst: Int = rowRect.bottom - rvRect.top
//                visibleHeightFirst * 100 / layoutManager.findViewByPosition(i)!!.height
//            }
//            if (percentFirst > 100) percentFirst = 100
//            mData.get(i).s = percentFirst.toString()
//            adapter.notifyItemChanged(i)
//        }
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = binding.rv.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                val lastPosition = layoutManager.findLastVisibleItemPosition()

                val globalVisibleRect = Rect()
                binding.rv.getGlobalVisibleRect(globalVisibleRect)
                for (pos in firstPosition..lastPosition) {
                    val view = layoutManager.findViewByPosition(pos)
                    if (view != null) {
                        val percentage = getVisibleHeightPercentage(view)
                        adapter.getItem(pos).s = percentage.toString()
                        adapter.notifyItemChanged(pos)
                        Log.e(MainActivity::class.java.name,"percentage===="+percentage.toString())
                    }
                }
            }

            //Method to calculate how much of the view is visible
            private fun getVisibleHeightPercentage(view: View): Double {

                val itemRect = Rect()
                val isParentViewEmpty = view.getLocalVisibleRect(itemRect)

                // Find the height of the item.
                val visibleHeight = itemRect.height().toDouble()
                val height = view.getMeasuredHeight()

                val viewVisibleHeightPercentage = visibleHeight / height * 100

                if(isParentViewEmpty){
                    Log.e(MainActivity::class.java.name,"viewVisibleHeightPercentage==="+viewVisibleHeightPercentage.toString())
                    return viewVisibleHeightPercentage

                }else{
                    return 0.0
                }
            }
        })
    }

    override fun getViewModel(): Class<MainModel> {
        return MainModel::class.java
    }


}