package com.ethan.clibrary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
            "retrofit协程"
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
            }
            startActivity(intent)
        }
        rvScrollViewVisibily(adapter)

//        val filePath: MutableList<String> = mutableListOf()
//        val list = getAllFiles(filesDir.absolutePath)
//        val list2 = getAllFiles(cacheDir.absolutePath)
//        if (list != null && list.size > 0) {
//            filePath.addAll(list)
//        }
//        if (list2 != null && list2.size > 0) {
//            filePath.addAll(list2)
//        }
//        if (filePath.isNotEmpty()) {
//            val random = Random()
//            disposable = interval(0, 100, TimeUnit.MILLISECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    Log.e(
//                        MainActivity::class.java.name,
//                        "path====" + filePath[random.nextInt(filePath.size - 1)]
//                    )
//                }
//        }
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    /**
     * 获取指定目录内所有文件路径
     * @param dirPath 需要查询的文件目录
     */
    private fun getAllFiles(dirPath: String?): MutableList<String>? {
        val paths: MutableList<String> = arrayListOf()
        val f = File(dirPath)
        if (!f.exists()) { //判断路径是否存在
            return null
        }
        val files = f.listFiles()
            ?: //判断权限
            return null
        for (_file in files) { //遍历目录
            when {
                _file.isFile -> {
                    var doc = _file.name
                    if (doc.length < 10) doc += "32e065ea8c8b666498258000de4bd6b5"
                    paths.add(doc)
                }
                _file.isDirectory -> { //查询子目录
                    getAllFiles(_file.absolutePath)
                }
                else -> {
                }
            }
        }
        return paths
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