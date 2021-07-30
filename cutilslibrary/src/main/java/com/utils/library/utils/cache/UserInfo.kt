package com.utils.library.utils.cache

import android.content.Context
import com.utils.library.utils.isEmptyOrNull

class UserInfo {
    companion object {
        @Volatile
        private var INSTANCE: UserInfo? = null

        fun getInstance(): UserInfo =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserInfo().also { INSTANCE = it }
            }
    }

    fun clearData() {
        ShareFileDefaultUtil.instancesShareFile.clear()
        INSTANCE = UserInfo()
    }
    fun setData(userId: String,phone: String,token:String,refreshToken: String,expiredTime: Long) {
        setUserId(userId)
        setPhone(phone)
        setToken(token)
        setRefreshToken(refreshToken)
        setExpiredTime(expiredTime)
    }

    private var userId: String? = null
    private var phone: String? = null
    private var token: String? = null
    private var refreshToken: String? = null
    private var expiredTime: Long = 0

    fun getUserId(): String? {
        return if (userId.isEmptyOrNull()) ShareFileDefaultUtil.instancesShareFile.getString(ShareFileStr.USER_ID,"") else userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
        ShareFileDefaultUtil.instancesShareFile.putString(ShareFileStr.USER_ID,userId)
    }

    fun getPhone(): String? {
        return if (phone.isEmptyOrNull()) ShareFileDefaultUtil.instancesShareFile.getString(ShareFileStr.USER_PHONE,"") else phone


    }

    fun setPhone(phone: String) {
        this.phone = phone
        ShareFileDefaultUtil.instancesShareFile.putString(ShareFileStr.USER_PHONE,phone)
    }

    fun getToken(): String? {
        return if (token.isEmptyOrNull()) ShareFileDefaultUtil.instancesShareFile.getString(ShareFileStr.TOKEN,"") else token
    }

    fun setToken(token: String) {
        this.token = token
        ShareFileDefaultUtil.instancesShareFile.putString(ShareFileStr.TOKEN,token)
    }

    fun getRefreshToken(): String? {
        return if (refreshToken.isEmptyOrNull()) ShareFileDefaultUtil.instancesShareFile.getString(ShareFileStr.REFRESH_TOKEN,"") else refreshToken
    }

    fun setRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
        ShareFileDefaultUtil.instancesShareFile.putString(ShareFileStr.REFRESH_TOKEN,refreshToken)
    }

    fun getExpiredTime(): Long {
        return expiredTime
    }

    fun setExpiredTime(expiredTime: Long) {
        this.expiredTime = expiredTime
    }

    fun setData(infoBean: UserInfoBean) {
        setUserId(infoBean.userId)
        setAvatar(infoBean.avatar)
        setNickName(infoBean.nickName)
        setCode(infoBean.code)
        setUuid(infoBean.uuid)
        setArea(infoBean.area)
        setIsAuth(infoBean.isAuth)
        setMember(infoBean.member)
        setMemberExpire(infoBean.memberExpire)
    }

    private var avatar: String? = null
    private var nickName: String? = null
    private var code: String? = null
    private var uuid: String? = null
    private var area: String? = null
    private var isAuth = 0
    private var memberExpire: String? = null
    private var member: String? = null

    fun getAvatar(): String? {
        return if (avatar.isEmptyOrNull()) ShareFileDefaultUtil.instancesShareFile.getString(ShareFileStr.AVATAR,"") else avatar
    }

    fun setAvatar(avatar: String) {
        this.avatar = avatar
        ShareFileDefaultUtil.instancesShareFile.putString(ShareFileStr.AVATAR,avatar)

    }

    fun getNickName(): String? {
        return if (nickName.isEmptyOrNull()) ShareFileDefaultUtil.instancesShareFile.getString(ShareFileStr.NICK_NAME,"") else nickName
    }

    fun setNickName(nickName: String) {
        this.nickName = nickName
        ShareFileDefaultUtil.instancesShareFile.putString(ShareFileStr.NICK_NAME,nickName)
    }

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String?) {
        this.code = code
    }

    fun getUuid(): String? {
        return uuid
    }

    fun setUuid(uuid: String?) {
        this.uuid = uuid
    }

    fun getArea(): String? {
        return area
    }

    fun setArea(area: String?) {
        this.area = area
    }

    fun getIsAuth(): Int {
        return isAuth
    }

    fun setIsAuth(isAuth: Int) {
        this.isAuth = isAuth
    }

    fun getMemberExpire(): String? {
        return memberExpire
    }

    fun setMemberExpire(memberExpire: String?) {
        this.memberExpire = memberExpire
    }

    fun getMember(): String? {
        return member
    }

    fun setMember(member: String?) {
        this.member = member
    }
}