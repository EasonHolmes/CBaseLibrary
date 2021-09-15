package com.utils.library.utils

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Build
import android.transition.Explode
import android.transition.Slide
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.utils.library.BuildConfig
import com.utils.library.R
import com.utils.library.http.GsonSingle
import com.utils.library.http.HttpData
import com.utils.library.ui.AbstractBaseActivity
import com.utils.library.ui.AbstractBaseFragment
import com.widget.library.dialog_pop.SimpleProgressDialog
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException


/**
 * 使用扩展函数：ActivityFragment类的扩展工具,尽可可让activityHelper减少代码
 * Fragment.ktxGetColor(){} 给Fragment添加ktxGetColor方法，用法：在Fragment中直接使用ktxGetColor()
 */
fun AbstractBaseActivity<*, *>.snackBar(content: String) {
    Snackbar.make(this.binding.root, content, Snackbar.LENGTH_SHORT).show()
}

fun AbstractBaseActivity<*, *>.toastContent(content: String) {
    Toast.makeText(this.binding.root.context, content, Toast.LENGTH_SHORT).show()
}

fun AbstractBaseFragment<*, *>.toastContent(content: String) {
    Toast.makeText(this._binding?.root?.context, content, Toast.LENGTH_SHORT).show()
}

fun AbstractBaseActivity<*, *>.ktxGetDrawable(drawableInt: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, drawableInt, null)
}

fun AbstractBaseActivity<*, *>.showProgressLoadDialog(msg: String = "") {
    lifecycleScope.launchWhenResumed {
        if (simple_loading_dialog != null) {
            simple_loading_dialog?.dismiss()
            simple_loading_dialog = null
        }
        if (!this@showProgressLoadDialog.isFinishing) {
            if (simple_loading_dialog == null) {
                simple_loading_dialog =
                    SimpleProgressDialog(this@showProgressLoadDialog, R.style.SimpleProgressDialog)
            }
            if (msg.isNotEmptyStr()) {
                simple_loading_dialog!!.setMessage(msg)
            }
            simple_loading_dialog!!.setCancelable(true)
            simple_loading_dialog!!.setCanceledOnTouchOutside(false)
            simple_loading_dialog!!.setCancelMessage(null)
            simple_loading_dialog!!.show()
        }
    }
}

fun AbstractBaseActivity<*, *>.dismissSimpleLoadDialog() {
    lifecycleScope.launchWhenResumed {
        if (simple_loading_dialog != null && simple_loading_dialog!!.isShowing) {
            simple_loading_dialog!!.dismiss()
        }
        simple_loading_dialog = null
    }
}

fun AbstractBaseActivity<*, *>.showSimpleDialogMessage(
    message: String?,
    yesClicklistener: DialogInterface.OnClickListener? = null
) {
    dismissSimpleLoadDialog()
    if (message.isEmptyOrNull()) {
        return
    }
    lifecycleScope.launchWhenResumed {
        val b = AlertDialog.Builder(this@showSimpleDialogMessage)
        b.setMessage(message)
        b.setCancelable(false)
        if (yesClicklistener != null) {
            b.setPositiveButton(R.string.text_yes, yesClicklistener)
            b.setNegativeButton(R.string.text_cancel) { dialog, _ -> dialog.cancel() }
        } else {
            b.setPositiveButton(R.string.text_know) { dialog, _ -> dialog.dismiss() }
        }
        if (!isFinishing) {
            b.show().setCanceledOnTouchOutside(false)
        }
    }
}

fun AbstractBaseFragment<*, *>.showProgressLoadDialog() {
    mActivity?.showProgressLoadDialog()
}

fun AbstractBaseFragment<*, *>.dismissSimpleLoadDialog() {
    mActivity?.dismissSimpleLoadDialog()
}

fun AbstractBaseFragment<*, *>.showSimpleDialogMessage(
    message: String?,
    yesClicklistener: DialogInterface.OnClickListener?
) {
    mActivity?.showSimpleDialogMessage(message, yesClicklistener)
}

//fun AbstractBaseActivity<*,*>.startActivityLollipop(intent: Intent, vararg sharedElements: Pair<View, String>) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        intent.putExtra(FinalStrings.EXTRA_PARENT_ACTIVITY_CLASS_NAME, this.javaClass.name)
//        val optionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        this, *sharedElements)
//        this.startActivity(intent, optionsCompat.toBundle())
//    } else {
//        this.startActivity(intent)
//    }
//}

//fun AbstractBaseFragment<*,*>.startActivityLollipop(intent: Intent, vararg sharedElements: Pair<View, String>) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        intent.putExtra(this.EXTRA_PARENT_FRAGMENT_CLASS_NAME, this.javaClass.name)
//        val optionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        this.getCurrentActivity(), *sharedElements)
//        this.getCurrentActivity().startActivity(intent, optionsCompat.toBundle())
//    } else {
//        this.startActivity(intent)
//    }
//}


@SuppressLint("RtlHardcoded")
fun AbstractBaseActivity<*, *>.animaActivityLollipopSlide() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val slide = Slide(Gravity.RIGHT)
        window.enterTransition = slide
        window.returnTransition = slide
    }
}

fun AbstractBaseActivity<*, *>.animaActivityLollipopExplode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val explode = Explode()
        window.enterTransition = explode
        window.returnTransition = explode
    }
}

fun AbstractBaseActivity<*, *>.errordialogMessageByServerErrorStatusCode(error: HttpException?) {
    this.dismissSimpleLoadDialog()
    if (error?.response() != null && error.response()!!.errorBody() != null) {
        val errorJson = error.response()!!.errorBody()!!.string()
        if (errorJson.isNotEmptyStr() && errorJson.subSequence(0, 1) == "{") {
            converErrorjson(errorJson)
        } else {
            toastContent("访问出错请重试$errorJson")
        }
    } else {
        toastContent("访问出错请重试")
    }
}

/**
 *
 */
private fun AbstractBaseActivity<*, *>.converErrorjson(errorJson: String?) {
    val jsonObject = JSONObject(errorJson)
    val errorCode = jsonObject.optInt("code", -1)
    when (errorCode) {
//        401 -> {//need Login onActivityResult:REQUEST_CODE_LOGIN
//            currentActivity.getSharedPreferences(SharedPrefsCookiePersistor.COOKIEFILE_NAME, Context.MODE_PRIVATE).edit().clear().apply()
//            currentActivity.startActivityForResult(Intent(currentActivity, LoginActivity::class.java), REQUEST_CODE_LOGIN)
//        }
//        500 -> {
//            dialogMessage("服务器发生错误")
//        }
        else -> {
            if (BuildConfig.DEBUG) {
                val errorMessage = jsonObject.optString("message", "")
                val errorCode = "code:$errorCode"
                lifecycleScope.launchWhenResumed {
                    val b = AlertDialog.Builder(this@converErrorjson)
                    b.setMessage(errorMessage)
                    b.setTitle(errorCode)
                    b.setCancelable(false)
                    b.setPositiveButton(
                        R.string.text_know
                    ) { dialog, _ -> dialog?.dismiss() }
                    if (!isFinishing) {
                        b.show().setCanceledOnTouchOutside(false)
                    }
                }
            } else {
                val errorMessage = jsonObject.optString("message", "")
                toastContent(errorMessage)
            }
        }
    }
}

/**
 * 访问失败http状态码非200情况
 *
 * @param error
 */
fun AbstractBaseActivity<*, *>.refreshError(error: Throwable?) {
//        if (BuildConfig.DEBUG && error?.message != null) {
//            toastContent(binding, error.message!!)
//        }
//        dismissSimpleLoadDialog()

    if (error is retrofit2.HttpException) {
//            val jsonObject = JSONObject(error.response().errorBody()?.string())
//            val errorContent = jsonObject.optString("message")
//            if (errorContent.isNotEmptyStr())
        showSimpleDialogMessage(error.message())
    } else if (error != null && error is HttpException && error.response() != null) {
        errordialogMessageByServerErrorStatusCode(error)
    } else {
        val errorStr = error?.message
        if (errorStr != null && errorStr.isNotEmpty()) {
            if (errorStr.contains("404")) {
//                    mActivityHelper.dialogMessage("未找到请求地址404\n" + error)
            } else if (errorStr.contains("500")) {
//                    mActivityHelper.dialogMessage("请求地址访问错误500\n" + error)
            } else if (errorStr.contains("SocketTimeoutException")) {
//                    mActivityHelper.dialogMessage("连接超时请重试SocketTimeoutException\n" + error)
            } else if (errorStr.contains("no address") ||
                errorStr.contains("Failed to connect to") ||
                errorStr.contains("No address") ||
                errorStr.contains("Unable to resolve host")
            ) {
                showSimpleDialogMessage("网络连接失败:请检查网络")
            } else {
                showSimpleDialogMessage("数据解析错误" + error.message)
            }
        }
    }
}

/**
 * 访问失败http状态码非200情况
 *
 * @param error
 */
fun AbstractBaseFragment<*, *>.refreshError(error: Throwable?) {
    mActivity?.refreshError(error)
}


fun AbstractBaseActivity<*, *>.blockServerResult(result: HttpData<*>, block: () -> Unit) {
    dismissSimpleLoadDialog()
    if (result.code == 0) {
        block()
    } else {
        converErrorjson(GsonSingle.getInstance().toJson(result))
    }
}

fun AbstractBaseFragment<*, *>.blockServerResult(result: HttpData<*>, block: () -> Unit) {
    dismissSimpleLoadDialog()
    if (result.code == 0) {
        block()
    } else {
        this.mActivity?.converErrorjson(GsonSingle.getInstance().toJson(result))
    }
}