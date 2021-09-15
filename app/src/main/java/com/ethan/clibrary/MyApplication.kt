package com.ethan.clibrary

import android.app.Application
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initXlog()
    }

    /**
     * Initialize XLog.
     */
    private fun initXlog() {
        val config = LogConfiguration.Builder()
            .logLevel(if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE)
            .jsonFormatter(DefaultJsonFormatter())
            .tag("ethan")
            .build()
        XLog.init(config)
    }
}