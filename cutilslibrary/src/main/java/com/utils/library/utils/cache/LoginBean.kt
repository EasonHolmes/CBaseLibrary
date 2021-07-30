package com.utils.library.utils.cache

data class LoginBean(
    var userId: String = "",
    var phone: String = "",
    var token: String = "",
    var refreshToken: String ="",
    var expiredTime: Long = 0,
    var uid: String = ""
)