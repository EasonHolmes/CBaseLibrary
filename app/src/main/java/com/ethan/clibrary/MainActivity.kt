package com.ethan.clibrary

import android.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethan.clibrary.activity.ComposeUIActivity
import com.ethan.clibrary.activity.MotionLayoutActivity
import com.ethan.clibrary.activity.RetrofitFlowActivity
import com.ethan.clibrary.databinding.ActivityMainBinding
import com.ethan.clibrary.model.MainModel
import com.luck.picture.lib.tools.ValueOf.toInt
import com.utils.library.ui.AbstractBaseActivity


class MainActivity : AbstractBaseActivity<ActivityMainBinding, MainModel>() {
    private val mData =
        mutableListOf<String>(
            "retrofit协程",
            "Motionlayout",
            "ComposeUI"
        )

    override fun setBindinglayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
    private var hander: MoveHandler?=null

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
                1 -> {
                    intent.setClass(this@MainActivity, MotionLayoutActivity::class.java)
                }
                2 -> {
                    intent.setClass(this@MainActivity, ComposeUIActivity::class.java)
                }
            }
            startActivity(intent)
        }
        rvScrollViewVisibily(adapter)
//        SubnetDevices.fromLocalAddress().findDevices(object : OnSubnetDeviceFound {
//            override fun onDeviceFound(device: Device?) {
//                // Stub: Found subnet device
//                device?.let {
//                    Log.e("ethan","device-"+it.mac)
//                }
//            }
//
//            override fun onFinished(devicesFound: ArrayList<Device?>?) {
//                // Stub: Finished scanning
//                devicesFound?.let {
//                    Log.e("ethan","finish=="+it.size.toString())
//                    devicesFound.forEach { device ->
//                        Log.e("ethan","finish222=="+device?.mac)
//                    }
//                }
//            }
//        })

        hander = MoveHandler(binding.btn,binding.parentView)
        hander?.sendEmptyMessage(1)
    }




    class MoveHandler(private val targetView:View,private val parentView:RelativeLayout) : Handler() {
        // 移动方向和距离
        private var decX = 1
        private var decY = 1

        // 坐标
        private var moveX  = 0
        private var moveY = 0
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (moveX == 0 || moveY == 0 ){
                moveX = targetView.x.toInt()
                moveY = targetView.y.toInt()
            }
            moveX += decX
            moveY += decY
            if (moveX + targetView.width >= parentView
                    .width || moveX < 0
            ) {
                decX = -decX
            }
            if (moveY + targetView.height >= parentView
                    .height || moveY < 0
            ) {
                decY = -decY
            }
            val lp = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            // 利用Margin改变小球的位置
            lp.setMargins(
                moveX,
                moveY, 0, 0
            )
            targetView.layoutParams = lp
            sendEmptyMessageDelayed(1, 10)
        }
    }

    override fun onDestroy() {
        hander?.removeCallbacksAndMessages(null)
        hander = null
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