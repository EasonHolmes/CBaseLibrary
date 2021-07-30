package com.utils.library.ui


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * @author cuiyang
 * @date 16/6/6
 *
 *
 * getCompositeDisposable().add(ApiClient.getApiService()
 * .getBidTypeList()
 * .map(RxHelper.RxMapJsonobjectData)
 * .map(str -> GsonSingle.getInstance().fromJson(str, BidTypeListEntity.class))
 * .compose(RxSchedulersHelper.io_main())
 * .compose(mView.bindToLifecycle())
 * .subscribe(entity -> mView.getBidTypeListUpdateRv(entity),
 * throwable -> mView.refreshError(throwable.getMessage())));
 *
 *
 *
 *
 * 转换jsonarray
 * //        val type = object : TypeToken<ArrayList></ArrayList><T>>() {}.type
 * //        var s  = GsonSingle.getInstance().fromJson<List></List><T>>("",type);
</T></T> */
/**
 * 如果有多个P2使用同一个P1的情况在P1不要确定泛型在P2中的构造方法中强转
 */
abstract class AbstractBasePresenter<V : BaseContract.BaseView, M : AbstractModel> (mView: V) :
    LifecycleObserver, BaseContract.BasePresenter {
    /**用于保存和清空Disposable*/
    private var disposables: CompositeDisposable? = null
    /**弱引用队列，用于清除引用V的弱引用对象 http://blog.csdn.net/u012332679/article/details/57489179*/
    //private var referenceQueue = ReferenceQueue<V>()
    /**使用弱引用先包装一层*/
    private val weakReferenceView = WeakReference<V>(mView)
    private var mModel: M? = null

    val mViewModel: M
        get() {
            if (mModel == null) {
                mModel = initModel()
            }
            return mModel!!
        }

    protected val mView: V?
        get() {
            return weakReferenceView.get()
        }

    protected val compositeDisposable: CompositeDisposable
        get() {
            if (disposables == null) {
                disposables = CompositeDisposable()
            }
            return disposables!!
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun dettach() {
        if (disposables != null) {
            disposables!!.clear()
            disposables = null
        }
        weakReferenceView.clear()
        mModel = null

//        referenceQueue.poll()
//        LogUtils.e(AbstractBasePresenter::class.java, "p:out")
//        LogUtils.e(AbstractBasePresenter::class.java, (weakReferenceView.get() == null).toString())
    }

//    protected fun PPostHttp(httpData: HttpData<Any>): PostRequest {
//        return if (mView != null && mView is AppCompatActivity) {
//            EasyHttp.post(weakReferenceView.get() as AppCompatActivity)
//        }else{
//            EasyHttp.post(weakReferenceView.get() as Fragment)
//        }
//    }
//
//    protected fun PGettHttp(httpData: HttpData<Any>): GetRequest {
//        return if (mView != null && mView is AppCompatActivity) {
//            EasyHttp.get(weakReferenceView.get() as AppCompatActivity)
//        }else{
//            EasyHttp.get(weakReferenceView.get() as Fragment)
//        }
//    }

    abstract fun initModel(): M

}