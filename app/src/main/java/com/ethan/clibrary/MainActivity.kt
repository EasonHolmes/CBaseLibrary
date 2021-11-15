package com.ethan.clibrary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethan.clibrary.activity.MotionLayoutActivity
import com.ethan.clibrary.activity.RetrofitFlowActivity
import com.ethan.clibrary.databinding.ActivityMainBinding
import com.ethan.clibrary.model.MainModel
import com.utils.library.ui.AbstractBaseActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable.interval
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import java.io.File
import java.util.*
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AbstractBaseActivity<ActivityMainBinding, MainModel>() {
    private val mData =
        mutableListOf<String>(
            "retrofit协程",
            "Motionlayout"
        )

    override fun setBindinglayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        //强制关闭黑夜模式，style里不要使用datNight主题
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val adapter = MainAdapter(binding.rv)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
        adapter.setNewDatas(mData)
        mViewmodel.testFun()
        binding.rv.setOnItemClickListener { familiarRecyclerView, view, position ->
            val intent = Intent()
            when (position) {
                0 -> {
                    intent.setClass(this@MainActivity, RetrofitFlowActivity::class.java)
                }
                1->{
                    intent.setClass(this@MainActivity,MotionLayoutActivity::class.java)
                }
            }
            startActivity(intent)
        }
        rvScrollViewVisibily(adapter)


    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }



    private fun rvScrollViewVisibily(adapter: MainAdapter) {
//        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val layoutManager = binding.rv.layoutManager as LinearLayoutManager
//                val firstPosition = layoutManager.findFirstVisibleItemPosition()
//                val lastPosition = layoutManager.findLastVisibleItemPosition()
//
//                val globalVisibleRect = Rect()
//                binding.rv.getGlobalVisibleRect(globalVisibleRect)
//                for (pos in firstPosition..lastPosition) {
//                    val view = layoutManager.findViewByPosition(pos)
//                    if (view != null) {
//                        val percentage = getVisibleHeightPercentage(view)
//                        adapter.getItem(pos).s = percentage.toString()
//                        adapter.notifyItemChanged(pos)
//                        Log.e(
//                            MainActivity::class.java.name,
//                            "percentage====" + percentage.toString()
//                        )
//                    }
//                }
//            }
//
//            //Method to calculate how much of the view is visible
//            private fun getVisibleHeightPercentage(view: View): Double {
//
//                val itemRect = Rect()
//                val isParentViewEmpty = view.getLocalVisibleRect(itemRect)
//
//                // Find the height of the item.
//                val visibleHeight = itemRect.height().toDouble()
//                val height = view.getMeasuredHeight()
//
//                val viewVisibleHeightPercentage = visibleHeight / height * 100
//
//                if (isParentViewEmpty) {
//                    Log.e(
//                        MainActivity::class.java.name,
//                        "viewVisibleHeightPercentage===" + viewVisibleHeightPercentage.toString()
//                    )
//                    return viewVisibleHeightPercentage
//
//                } else {
//                    return 0.0
//                }
//            }
//        })
    }

    override fun getViewModel(): Class<MainModel> {
        return MainModel::class.java
    }


}