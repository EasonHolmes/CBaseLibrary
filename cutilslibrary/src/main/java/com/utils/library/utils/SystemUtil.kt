package com.utils.library.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import java.lang.reflect.Method
import java.util.*

object SystemUtil {
    var oAID: String? = null
        private set
    var vAID: String? = null
        private set
    var aAID: String? = null
        private set

    /**
     * 返回IMEI
     *
     * @param slotId slotId为卡槽Id，它的值为 0、1；
     */
    fun getIMEI(context: Context, slotId: Int): String {
        return try {
            val manager: TelephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val method: Method =
                manager.javaClass.getMethod("getImei", Int::class.javaPrimitiveType)
            method.invoke(manager, slotId) as String
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 返回安卓设备ID
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getDeviceID(context: Context): String? {
        var deviceId: String? = ""
        val tel = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            deviceId = tel.deviceId
        } catch (e: Exception) {
        }
        if (deviceId.isEmptyOrNull()) {
            deviceId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }
        if (deviceId.isEmptyOrNull()) {
            deviceId = UUID.randomUUID().toString()
        }
        return deviceId
    }
    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val systemVersion: String
        get() = Build.VERSION.RELEASE

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    val systemModel: String
        get() = Build.MODEL

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    val deviceBrand: String
        get() = Build.BRAND

    /**
     * 获取运营商
     *
     * @param context
     * @return
     */
    fun getSimOperator(context: Context): String {
        return when (getCellularOperatorType(context)) {
            0 -> "其它"
            1 -> "中国移动"
            2 -> "中国联通"
            3 -> "中国电信"
            -1 -> "无sim卡"
            -2 -> "数据流量未打开"
            else -> "未获取到"
        }
    }

    /**
     * 获取设备蜂窝网络运营商
     *
     * @return ["中国电信CTCC":3]["中国联通CUCC:2]["中国移动CMCC":1]["other":0]["无sim卡":-1]["数据流量未打开":-2]
     */
    fun getCellularOperatorType(context: Context): Int {
        var opeType = -1
        // No sim
        if (!hasSim(context)) {
            return opeType
        }
        // Mobile data disabled
        if (!isMobileDataEnabled(context)) {
            opeType = -2
            return opeType
        }
        // Check cellular operator
        val tm: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator: String = tm.getSimOperator()
        // 中国联通
        opeType = if ("46001" == operator || "46006" == operator || "46009" == operator) {
            2
            // 中国移动
        } else if ("46000" == operator || "46002" == operator || "46004" == operator || "46007" == operator) {
            1
            // 中国电信
        } else if ("46003" == operator || "46005" == operator || "46011" == operator) {
            3
        } else {
            0
        }
        return opeType
    }

    /**
     * 判断数据流量开关是否打开
     *
     * @param context
     * @return
     */
    fun isMobileDataEnabled(context: Context): Boolean {
        return try {
            val method: Method =
                ConnectivityManager::class.java.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            method.invoke(connectivityManager) as Boolean
        } catch (t: Throwable) {
            false
        }
    }

    /**
     * 检查手机是否有sim卡
     */
    fun hasSim(context: Context): Boolean {
        val tm: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator: String = tm.getSimOperator()
        return !operator.isEmptyOrNull()
    }

    /**
     * 获取手机分辨率
     *
     * @param context
     */
    fun getResolutionRatio(context: Context): String {
        val dm: DisplayMetrics = context.resources.displayMetrics
        val screenWidth: Int = dm.widthPixels
        val screenHeight: Int = dm.heightPixels
        return screenWidth.toString() + "x" + screenHeight
    }
}