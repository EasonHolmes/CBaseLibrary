package com.ethan.clibrary.model

import android.util.Log
import com.utils.library.viewmodel.AbstractModel

class MainModel : AbstractModel() {

    fun testFun(){
        Log.e(MainModel::class.java.name,"=======")

    }
}