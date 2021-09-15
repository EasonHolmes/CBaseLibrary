package com.ethan.clibrary.activity

import android.os.Bundle
import androidx.lifecycle.*
import com.ethan.clibrary.databinding.ActivityRetrofitFlowBinding
import com.ethan.clibrary.model.RetrofitFlowActModel
import com.utils.library.ui.AbstractBaseActivity
import kotlinx.coroutines.flow.collect

/**
 * 如上图所示：
1.使用launch是不安全的，在应用在后台时也会接收数据更新，可能会导致应用崩溃
2.使用launchWhenStarted或launchWhenResumed会好一些，在后台时不会接收数据更新，但是，上游数据流会在应用后台运行期间保持活跃，因此可能浪费一定的资源

这么说来，我们使用WhileSubscribed进行的配置岂不是无效了吗？订阅者一直存在，只有页面关闭时才会取消订阅
官方推荐repeatOnLifecycle来构建协程
在某个特定的状态满足时启动协程，并且在生命周期所有者退出该状态时停止协程,如下图所示。
当这个Fragment处于STARTED状态时会开始收集流，并且在RESUMED状态时保持收集，最终在Fragment进入STOPPED状态时结束收集过程。
结合使用repeatOnLifecycle API和WhileSubscribed,可以帮助您的应用妥善利用设备资源的同时，发挥最佳性能
 */
class RetrofitFlowActivity :
    AbstractBaseActivity<ActivityRetrofitFlowBinding, RetrofitFlowActModel>() {
    override fun setBindinglayout(): ActivityRetrofitFlowBinding {
        return ActivityRetrofitFlowBinding.inflate(layoutInflater)
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        mViewmodel.retrofitFlow()
        lifecycleScope.launchWhenCreated {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                mViewmodel.retrofitFlowResponseFlow.collect{
                   binding.txtResponse.text = it.toString()
                }
            }
        }
    }

    override fun getViewModel(): Class<RetrofitFlowActModel> {
        return RetrofitFlowActModel::class.java
    }
}