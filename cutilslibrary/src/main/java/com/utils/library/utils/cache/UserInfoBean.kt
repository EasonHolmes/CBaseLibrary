package com.utils.library.utils.cache

data class UserInfoBean(
    // 用户id
    var userId: String = "",

    // 用户头像
    var avatar: String = "",

    // 用户昵称
    var nickName: String = "",

    // 靓号
    var code: String = "",

    // 邀请码
    var uuid: String = "",

    // 地区
    var area: String = "",

    // 是否认证
    var isAuth :Int = 0,

    // 省id
    var provinceId :Int = 0,

    // 市id
    var cityId :Int = 0,

    // 区id
    var countyId:Int  = 0,

    //是否是靓号 0:是 1:否
    var codeFlag:Int  = 0,

    // 会员到期日
    var memberExpire: String = "",

    // 会员标题
    var member: String = ""
) {
}