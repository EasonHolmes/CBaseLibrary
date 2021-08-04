package com.ethan.clibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.utils.library.helper.ActivityStackManager
import com.utils.library.utils.CCLogUtils
import com.widget.library.anim.coordinator_anim.CommonBehavior
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CCLogUtils
        ActivityStackManager.getInstance().application
    }
}