package com.ethan.clibrary

import android.R.attr.path
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri.fromFile
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenCreated
import androidx.recyclerview.widget.LinearLayoutManager
import com.ethan.clibrary.activity.ComposeUIActivity
import com.ethan.clibrary.activity.MotionLayoutActivity
import com.ethan.clibrary.activity.PagActivity
import com.ethan.clibrary.activity.RetrofitFlowActivity
import com.ethan.clibrary.databinding.ActivityMainBinding
import com.ethan.clibrary.model.MainModel
import com.permissionx.guolindev.PermissionX
import com.utils.library.ui.AbstractBaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import java.io.File
import kotlin.math.log


class MainActivity : AbstractBaseActivity<ActivityMainBinding, MainModel>() {
    private val mData =
        mutableListOf<String>(
            "retrofit协程",
            "Motionlayout",
            "ComposeUI",
            "tencentPag"
        )

    override fun setBindinglayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private var hander: MoveHandler? = null

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
                3 -> {
                    intent.setClass(this, PagActivity::class.java)
                    intent.putExtra("API_TYPE",5)
                }
            }
            startActivity(intent)
        }
        rvScrollViewVisibily(adapter)
        binding.fflayout.setTextview(arrayOf("1", "1", "1", "1"))

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

//        Log.e("ethan", "44444")
//        hander = MoveHandler(binding.btn, binding.parentView)
//        hander?.sendEmptyMessage(1)
//        val url  = "https://files.norlinked.com/products/apk/原动力计步-1.0.1.1.apk"

        //单下载默认内部存储
//        val disposable = url.download()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onNext = { progress ->
//                    //download progress
//                    binding.btn.text = "${progress.downloadSizeStr()}/${progress.totalSizeStr()}"
//                    Log.e("ethan",progress.toString())
//                },
//                onComplete = {
//                    //download complete
//                    binding.btn.text = "Open"
////                                installApk(url.file())
//                },
//                onError = {
//                    //download failed
//                    Log.e("ethan",it.message.toString())
//                    binding.btn.text = "Retry"
//                }
//            )

//        PermissionX.init(this)
//            .permissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
////            权限需要提示框
////            .onExplainRequestReason { scope, deniedList ->
////                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
////            }
//            .request { allGranted, grantedList, deniedList ->
//                if (allGranted) {
//                    Task(url = url, savePath = "/update").download()
////                    val disposable = url.download()
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeBy(
//                            onNext = { progress ->
//                                //download progress
//                                binding.btn.text = "${progress.downloadSizeStr()}/${progress.totalSizeStr()}"
//                                Log.e("ethan",progress.toString())
//                            },
//                            onComplete = {
//                                //download complete
//                                binding.btn.text = "Open"
////                                installApk(url.file())
//                            },
//                            onError = {
//                                //download failed
//                                Log.e("ethan",it.message.toString())
//                                binding.btn.text = "Retry"
//                            }
//                        )
//                } else {
//                }
//            }

    }

    fun Context.installApk(file: File) {
        val intent = Intent(ACTION_VIEW)
        val authority = "$packageName.file.provider"
        val uri = if (SDK_INT >= N) {
            getUriForFile(this, authority, file)
        } else {
            fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    class MoveHandler(private val targetView: View, private val parentView: RelativeLayout) :
        Handler() {
        // 移动方向和距离
        private var decX = 1
        private var decY = 1

        // 坐标
        private var moveX = 0
        private var moveY = 0
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (moveX == 0 || moveY == 0) {
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