package com.utils.library.ui

import android.content.Intent
import android.os.*
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.utils.library.utils.errordialogMessageByServerErrorStatusCode
import com.utils.library.utils.livedata.SingleLivedata
import com.utils.library.utils.refreshError
import com.utils.library.viewmodel.AbstractModel
import com.widget.library.R
import com.widget.library.dialog_pop.SimpleProgressDialog
import com.widget.library.refresh.listener.OnCRefreshListener
import com.widget.library.refresh.listener.onCLoadMoreListener
import com.widget.library.utils.StatusBarUtil
import io.reactivex.rxjava3.disposables.Disposable

/**
 * 为防止 Glide会出现You cannot start a load for a destroyed activity页面关闭recyclerview不再滑动 使用Lifecycle写在DDRecyclerviewLyoaut中在onStop生命周期
 *
 *
 */
abstract class AbstractBaseActivity<VB : ViewBinding, VM : AbstractModel> :
    AppCompatActivity(),
    View.OnClickListener, BaseContract.BaseView,
    OnCRefreshListener, onCLoadMoreListener {
    /**
     * 传入参数, String 类型， startActivity 启动带入下一个界面的父的启动类的名称
     */
    private val EXTRA_PARENT_ACTIVITY_CLASS_NAME = "Base_Extra_ParentActivityClassName"
    protected val PAGESIZE: Int = 20

    val mHandler = Handler(Looper.getMainLooper()) { message -> false }

    //simple_loading_dialog的show、diss在ActivityFragmentKtx.kt扩展类中，减少activityhelper代码
    var simple_loading_dialog: SimpleProgressDialog? = null

    //用来标示是下拉刷新还是上拉加载 true为下拉刷新
    protected var isRefresh = true


    private val mToolbar: Toolbar by lazy(LazyThreadSafetyMode.NONE) { findViewById(R.id.mToolBar_frame) }
    private val mToolbarLeftIcon: ImageView by lazy(LazyThreadSafetyMode.NONE) { findViewById(R.id.toolbar_left_img) }
    private val mToolbarRightIcon: ImageView by lazy(LazyThreadSafetyMode.NONE) { findViewById(R.id.toolbar_right_img) }
    private val mToolbarTitle: TextView by lazy(LazyThreadSafetyMode.NONE) { findViewById(R.id.toolbar_title_txt) }
    protected var disposable: Disposable? = null
    val mViewmodel: VM by lazy { ViewModelProvider(this)[getViewModel()] }//viewmodel oncreate中初始化

    //    lateinit var binding: B//在onCreate中初始化
    val binding: VB by lazy { setBindinglayout() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatuBarTheme()
        setContentView(binding.root)
        observeViewmodelEvent()
        onCreated(savedInstanceState)
    }

    private fun observeViewmodelEvent() {
        mViewmodel.apiExceptionEvent.observe(this, Observer {
            this.refreshError(it)
        })
        mViewmodel.errorCodeOptionEvent.observe(this, Observer {
            //某些code需要处理的内容
        })
    }

    /***绑定viewbinding */
    protected abstract fun setBindinglayout(): VB

    /***初始化成功后 */
    protected abstract fun onCreated(savedInstanceState: Bundle?)

    /***这里是适配为不同的View 装载不同Viewmodel */
    protected abstract fun getViewModel(): Class<VM>

    /**
     * 设置statubar样式 如果要设置activity透明这个需要理新实现否则就不会透明了
     */
//    protected open fun setStatuBarTheme() {
//        StatusBarUtil.setColorForSwipeBack(this, resources.getColor(R.color.app_color), 50)
    // 经测试在代码里直接声明透明状态栏更有效 这个设置会在一些机子上有半透明效果
    //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//    }

    protected fun setStatusbarThemeColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(
                this,
                ResourcesCompat.getColor(resources, R.color.app_statusBar_color, null),
                1
            )
            StatusBarUtil.setLightMode(this)
        } else {
            StatusBarUtil.setColor(this, resources.getColor(R.color.app_statusBar_color), 1)
        }
    }

    /**
     * 设置statubar样式 如果要设置activity透明这个需要理新实现否则就不会透明了
     */
    protected open fun setStatuBarTheme() {
        setStatusbarThemeColor()
    }

    /**
     *
     *  LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT：默认情况下，全屏窗口不会使用到刘海区域，非全屏窗口可正常使用刘海区域。

    LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS：窗口声明使用刘海区域

    LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER：窗口声明不使用刘海区域

     */
    protected open fun translucentStatusBar(needTranslucentStatus: Boolean = true) {
        //刘海就顶上去
        if (needTranslucentStatus) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= 28) {
                val lp = window.attributes
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window.attributes = lp
            }
        } else {
            setStatuBarTheme()
        }

    }

    protected fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    protected open fun translucentNavigationBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

    protected fun setStatusbarThemeColor(colorResource: Int = 0) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(
                this,
                ResourcesCompat.getColor(
                    resources,
                    if (colorResource != 0) colorResource else R.color.app_statusBar_color,
                    null
                ),
                1
            )
            StatusBarUtil.setLightMode(this)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setColor(
                this,
                ResourcesCompat.getColor(
                    resources,
                    if (colorResource != 0) colorResource else R.color.app_statusBar_color,
                    null
                ),
                1
            )
        }
    }

    override fun onRefresh() {
        isRefresh = true
    }

    override fun onLoadMore() {
        isRefresh = false
    }

    protected fun initToolbar(
        titleResourceId: Int = 0,
        titleResouceStr: String = "",
        isNeedBack: Boolean = true
    ) {
            mToolbarTitle.text = if (titleResourceId > 0) resources.getString(titleResourceId) else titleResouceStr
            if (isNeedBack)mToolbarLeftIcon.setOnClickListener { finish() }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
//        if (outState != null) {
        val FRAGMENTS_TAG = "Android:support:fragments"
        //清除保存的fragemnt信息因为长时间后台可能回收了activity但fragment没有被回收
        //再打开时会新建activity算恢复但本质是一个新的。这时fragment的getactivity就是null因为get的是之前的
        outState.remove(FRAGMENTS_TAG)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        unsubscribe(disposable)
    }

    protected fun unsubscribe(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    protected fun setViewClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    override fun startActivity(intent: Intent?) {
        intent?.putExtra(EXTRA_PARENT_ACTIVITY_CLASS_NAME, this.javaClass.name)
        super.startActivity(intent)
    }


    override fun onClick(v: View) {

    }
}
