package com.utils.library.http

class  ApiException  (val code:Int?, private val msg:String):Exception(msg) {

}
