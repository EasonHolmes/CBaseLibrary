package com.utils.library.utils.cache

import android.content.Context
import android.content.SharedPreferences
import com.utils.library.utils.edit

class ShareFileDefaultUtil private constructor() {
    private lateinit var preferences: SharedPreferences
    fun init(context: Context) {
        preferences = context.getSharedPreferences("app_sharepreferences",Context.MODE_PRIVATE)
    }

    /**
     * 从SharePreferences中获取字符串
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getString(key: String, defValue: String=""): String? {
        return preferences.getString(key, defValue)
    }

    /**
     * 从SharePreferences中获取整数
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getInt(key: String, defValue: Int=0): Int {
        return preferences.getInt(key, defValue)
    }

    /**
     * 从SharePreferences中获取长整数
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getLong(key: String, defValue: Long= 0L): Long {
        return preferences.getLong(key, defValue)
    }

    /**
     * 从SharePreferences中获取符点数
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getFloat(key: String, defValue: Float= 0f): Float {
        return preferences.getFloat(key, defValue)
    }

    /**
     * 从SharePreferences中获取布尔值
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getBoolean(key: String, defValue: Boolean= false): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    /**
     * 设置字符串到ShareProferences
     *
     * @param key
     * @param value
     */
    fun putString(key: String, value: String) {
        preferences.edit(commit = true) { putString(key,value) }
    }

    /**
     * 设置整数到ShareProferences
     *
     * @param key
     * @param value
     */
    fun putInt(key: String, value: Int) {
        preferences.edit(commit = true) { putInt(key,value) }

    }

    /**
     * 设置长整数到ShareProferences
     *
     * @param key
     * @param value
     */
    fun putLong(key: String, value: Long) {
        preferences.edit(commit = true) { putLong(key,value) }
    }

    /**
     * 设置符点数到ShareProferences
     *
     * @param key
     * @param value
     */
    fun putFloat(key: String, value: Float) {
        preferences.edit(commit = true) { putFloat(key,value) }
    }

    /**
     * 设置布尔值到ShareProferences
     *
     * @param key
     * @param value
     */
    fun putBoolean(key: String, value: Boolean) {
        preferences.edit(commit = true) { putBoolean(key,value) }
    }

    /**
     * 清除SharePreferences中的数据
     */
    fun clear() {
        preferences.edit { clear() }
    }

    companion object {
        val instancesShareFile: ShareFileDefaultUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ShareFileDefaultUtil()
        }
    }
}