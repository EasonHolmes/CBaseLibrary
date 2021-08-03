package com.utils.library.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.regex.Pattern

/**
 * Created by cuiyang on 16/6/3.
 */
object CCUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 当我们判断出当前设备打开允许模拟位置时，在判断一下手机系统的版本，若为Android M以及以上，就屏蔽不管。
     * 可能部分同学会问那么android M上的选择模拟位置信息应用有影响么？答案是否定的，由于我们的App没有添加允许模拟位置的权限，所以其根本不会出现在选择模拟位置应用列表，进而不会执行模拟位置的操作。
     * 所以最终的解决方案就是，检测设备是否开启了模拟位置选项，若开启了，则判断当前设备是否为Android M即以上，若是，则屏蔽不管，否则阻塞用户操作，引导用户关闭模拟位置选项。
     */
    fun isAllowMockLocation(mContext: Context): Boolean {
        var isOpen = Settings.Secure.getInt(mContext.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0

        /**
         * 该判断API是androidM以下的API,由于Android M中已经没有了关闭允许模拟位置的入口,所以这里一旦检测到开启了模拟位置,并且是android M以上,则
         * 默认设置为未有开启模拟位置
         */
        if (isOpen && Build.VERSION.SDK_INT > 22) {
            isOpen = false
        }
        return isOpen
    }
    /**
     * 判断是否为正确的手机号
     *
     * @param mobile
     * @return
     */
    fun isMobile(mobile: String): Boolean {
        return if (mobile.isEmptyOrNull()) false else Pattern.matches("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$", mobile)
    }
}