package com.utils.library.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.widget.library.R
import io.reactivex.rxjava3.disposables.Disposable


/**
 * Created by cuiyang on 16/6/6.
 * 生命周期顺序请注意 1setUserVisibleHint 2onCreateView 3 onCreateViewed
 * 为防止 Glide会出现You cannot start a load for a destroyed activity页面关闭recyclerview不再滑动 使用Lifecycle写在DDRecyclerviewLyoaut中在onStop生命周期
 */
abstract class AbstractBaseFragment<B : ViewBinding, T : BaseContract.BasePresenter, V : AbstractModel> :
    Fragment(),
    BaseContract.BaseView, View.OnClickListener {

    private val EXTRA_PARENT_FRAGMENT_CLASS_NAME = "Base_Extra_ParentFragmentClassName"
//    protected val lastFragmentName: String by lazy(LazyThreadSafetyMode.NONE) { activity.intent.getStringExtra(EXTRA_PARENT_FRAGMENT_CLASS_NAME) }

    protected val PAGESIZE: Int = 20
    private var mToolbar: Toolbar? = null
    private var mToolbarLeftIcon: ImageView? = null
    private var mToolbarRightIcon: ImageView? = null
    private var mToolbarTitle: TextView? = null
    var mActivity: AbstractBaseActivity<*, *, *>? = null
    protected var mContext: Context? = null
    protected var disposable: Disposable? = null
    protected lateinit var presenter: T//在oncreate中初始化P在Ondestory中释放V
    var _binding: B? = null
    protected val binding get() = _binding!!
    lateinit var viewmodel: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        mActivity = activity as AbstractBaseActivity<*, *, *>
        presenter = initPresenter()
        viewmodel = getViewModel()
        lifecycle.addObserver(presenter as AbstractBasePresenter<*, *>)
        onFragmentViewCreated(view, savedInstanceState)
    }

    protected abstract fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): B

    /**
     *
     */
    protected abstract fun onFragmentViewCreated(view: View?, savedInstanceState: Bundle?)

    /**
     * 初始化Presenter
     */
    protected abstract fun initPresenter(): T

    protected abstract fun getViewModel(): V

    /**
     * disposable

     * @param disposable
     */
    protected fun unsubscribe(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribe(disposable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Fragment 的存在时间比其视图长。请务必在 Fragment 的 onDestroyView() 方法中清除对绑定类实例的所有引用。
        _binding = null
    }


    protected fun initToolbar(rootView: View?, titleResourceId: Int) {
        initToolbar(rootView, resources.getString(titleResourceId))
    }

    protected fun initToolbar(v: View?, title: String?) {
        mToolbar = v?.findViewById(R.id.mToolBar_frame) as Toolbar
        mToolbarTitle = v.findViewById(R.id.toolbar_title_txt) as TextView
        mToolbarTitle?.text = title
//        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
//        return mToolbar
    }

    protected fun getLeftToolbarIcon(v: View?): ImageView =
        v?.findViewById(R.id.toolbar_left_img) as ImageView

    protected fun getRightToolbarIcon(v: View?): ImageView {
        val view = v?.findViewById(R.id.toolbar_right_img) as ImageView
        view.visibility = View.VISIBLE
        return view
    }
    protected fun getToolbar():Toolbar?{
        return mToolbar
    }

    /**
     * 访问失败未有连接

     * @param error
     */
    override fun refreshError(error: Throwable?) {
        mActivity?.refreshError(error)
    }


    protected fun setViewClickListener(vararg views: View) {
        for (view in views) {
            view.setOnClickListener(this)
        }
    }

    protected fun setAllViewCLickListener() {
        val viewGroup = binding.root as ViewGroup
        for (i in 0 until viewGroup.childCount) {
            if (viewGroup.getChildAt(i) is View) {
                viewGroup.getChildAt(i).setOnClickListener(this)
            }
        }
    }

    override fun startActivity(intent: Intent?) {
        intent?.putExtra(EXTRA_PARENT_FRAGMENT_CLASS_NAME, this.javaClass.name)
        super.startActivity(intent)
    }

    override fun onDetach() {
//        removeCallbacks();
        mActivity = null
        super.onDetach()
    }

    /**
     * 销毁当前 Fragment 所在的 Activity
     */
    open fun finish() {
        if (mActivity != null && mActivity?.isFinishing == true) {
            mActivity!!.finish()
        }
    }

    /**
     * Fragment 返回键被按下时回调
     */
    open fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 默认不拦截按键事件，回传给 Activity
        return false
    }

    override fun onClick(v: View) {

    }

}
