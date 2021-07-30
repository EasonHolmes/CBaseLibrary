package com.utils.library.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.regex.Pattern

/**
 * Created by cuiyang on 16/6/3.
 */
object KbUtils {
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
//    /**
//     * 根据毫秒
//     * 返回 天 小时 分钟 秒 毫秒 的数组
//     */
//    fun formatTime(ms: Long): Array<String> {
//        val ss = 1000
//        val mi = ss * 60
//        val hh = mi * 60
//        val dd = hh * 24
//
//        val day = ms / dd
//        val hour = (ms - day * dd) / hh
//        val minute = (ms - day * dd - hour * hh) / mi
//        val second = (ms - day * dd - hour * hh - minute * mi) / ss
//        val milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss
//
//        val strDay = if (day < 10) "0$day" else "" + day //天
//        val strHour = if (hour < 10) "0$hour" else "" + hour//小时
//        val strMinute = if (minute < 10) "0$minute" else "" + minute//分钟
//        val strSecond = if (second < 10) "0$second" else "" + second//秒
//        var strMilliSecond = if (milliSecond < 10) "0$milliSecond" else "" + milliSecond//毫秒
//        strMilliSecond = if (milliSecond < 100) "0$strMilliSecond" else "" + strMilliSecond
//
//        return arrayOf(strDay, strHour, strMinute, strSecond, strMilliSecond)
//    }
//
//    /**
//     * 比较两日期大小精确到天 oldTime旧日期  now现在日期 负数为oldTIme>nowTime 正数为oldTIme<nowTime 0为相同
//     */
//    fun datesBetween(oldTime: String, nowTime: String, sdf: SimpleDateFormat): Int {
//        val cal = Calendar.getInstance()
//        cal.time = sdf.parse(oldTime)
//        val time1 = cal.timeInMillis
//        cal.time = sdf.parse(nowTime)
//        val time2 = cal.timeInMillis
//        val betweenDays = (time2 - time1) / (1000 * 3600 * 24)
//        return Integer.parseInt(betweenDays.toString())
//    }
//
//    /**
//     * 比较两日期大小精确到秒 DATE1旧日期  DATE2现在日期 负数为oldTIme<nowTime 正数为oldTIme>nowTime 0为相同
//     */
//    @SuppressLint("SimpleDateFormat")
//    fun compareDate(DATE1: String, DATE2: String, df: SimpleDateFormat): Int {
//        try {
//            val dt1 = df.parse(DATE1)
//            val dt2 = df.parse(DATE2)
//            if (dt1.time > dt2.time) {
//                return 1
//            } else if (dt1.time < dt2.time) {
//                return -1
//            } else {
//                return 0
//            }
//        } catch (exception: Exception) {
//            exception.printStackTrace()
//        }
//        return 0
//    }
//
//    /**
//     * 两个时间相差距离多少天多少小时多少分多少秒
//     *
//     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
//     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
//     * @return String 返回值为：xx天xx小时xx分xx秒
//     */
//    fun getDistanceTime(str1: String, str2: String, df: SimpleDateFormat): Array<Long> {
//        val one: Date
//        val two: Date
//        var day: Long = 0
//        var hour: Long = 0
//        var min: Long = 0
//        var sec: Long = 0
//        try {
//            one = df.parse(str1)
//            two = df.parse(str2)
//            val time1 = one.time
//            val time2 = two.time
//            val diff: Long
//            if (time1 < time2) {
//                diff = time2 - time1
//            } else {
//                diff = time1 - time2
//            }
//            day = diff / (24 * 60 * 60 * 1000);
//            hour = diff / (60 * 60 * 1000) - day * 24
//            min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
//            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return arrayOf(day, hour, min, sec)
//        //        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
////        return if (hour > 0) hour.toString() + "小时" else if (min > 0) min.toString() + "分钟" else "1分钟"
//    }
//
//    /**
//     * 转换服务端格式
//     */
//    fun convertJson(json: String): JSONObject? {
//        if (json.isNotEmptyStr()) {
//            val jsonObject = JSONObject(json)
//            //服务端成功时没有code
//            val code = jsonObject.optInt("code", 0)
//            //非0说明有问题没有正常返回则直接生成一个code message的jsonobject不管他code message后还有什么只拿code 和message就好
//            if (code != 0) {
////                val errorJsonObject = JSONObject()
////                errorJsonObject.put("code", jsonObject.getInt("code"))
////                errorJsonObject.put("message", jsonObject.getString("message"))
//                //直接返回他原本的
//                return jsonObject
//            } else if (code != 0 && jsonObject.getString("message").isEmptyOrNull()) {
//                //自定义一个code去通知
//                val cuszimeJsonObject = JSONObject()
//                cuszimeJsonObject.put("code", -999)
//                return cuszimeJsonObject
//            } else {
//                //对返回json包一层code用来做判断使用
//                val jsonObjectResult = JSONObject()
//                jsonObjectResult.put("code", 0)
//                jsonObject.keys().forEach {
//                    jsonObjectResult.put(it, jsonObject.get(it))
//                }
//                return jsonObjectResult
//            }
//        }
//        return null
//    }
//
//    /**
//     * 过滤startActivityForResult的code值是否相等并成功
//     */
//    fun filterRequestCodeResultCode(info: RxActivityForResultInfo, requseCode: Int, equalsIntenData: Boolean): Boolean {
//        if (equalsIntenData)
//            return (info.requestCode == requseCode && info.resultCode == Activity.RESULT_OK && info.data != null)
//        else
//            return (info.requestCode == requseCode && info.resultCode == Activity.RESULT_OK)
//    }
//
//    fun convertThrowableHttpExceptionToJson(error: Throwable?): JSONObject? {
//        if (error != null && error is HttpException && error.response() != null && error.response().toString().isNotEmptyStr()) {
//            val jsonObject = JSONObject(error.response().errorBody()?.string())
//            return jsonObject
////            val errorCode = jsonObject.optInt("code", -1)
////            val errorMessage = jsonObject.optString("message", FinalStrings.EMPTYSTR)
//        } else if (error != null && error is HttpException) {
//            val jsonObject = JSONObject()
//            jsonObject.put("code", error.code())
//            return jsonObject
//        } else {
//            return null
//        }
//    }
//
//    fun bmpToByteArray(bmp: Bitmap): ByteArray {
//        val output = ByteArrayOutputStream()
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
//        val result = output.toByteArray()
//        if(!bmp.isRecycled){
//            bmp.recycle()
//        }
//        try {
//            output.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return result
//    }
//
//    fun payByWeichat(iwxapi: IWXAPI, appid: String, partnerid: String, prepayid: String,
//                     noncestr: String, timestamp: String, sign: String, weichatPayListener: WeichatPayListener) {
//        iwxapi.registerApp(ZConstants.weixinAppId)
//        val request = PayReq()
//        request.appId = appid
//        request.partnerId = partnerid
//        request.prepayId = prepayid
//        request.nonceStr = noncestr
//        request.timeStamp = timestamp
//        request.packageValue = "Sign=WXPay"
//        request.sign = sign
//        request.extData = "app data"
//        iwxapi.sendReq(request)
//        //这里统一键听 这里统一进行回调就不用到处Rxbus的接收了
//        RxBus.getInstance().addSubscription(this, RxBus.getInstance().tObservable(WeichatBean::class.java)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                weichatPayListener.weichatPayCallback(it.paySuccess)
//                RxBus.getInstance().unSubscribe(WeichatBean::class.java as Any)
//            }
//            ) {})
//    }
//
//
//    /**
//     * 把view保存成bitmap
//     * tip 如果imageview有使用glide去加载在android8.0中需要禁用硬件加速
//     *  GlideApp.with(this@PayedShareActivity)
//     *   .applyDefaultRequestOptions(RequestOptions().disallowHardwareConfig().placeholder(R.drawable.placeholder_avatar))
//     *   .load(user.avatar)
//     *  .into(binding.paedShareAccountImg)
//     */
//    fun loadBitmapFromView(v: View, whiteOrBlack: Boolean = false): Bitmap {
//        v.isDrawingCacheEnabled = true//设置可获取内存
//        v.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH//高cache效果
//        v.drawingCacheBackgroundColor = if (whiteOrBlack) Color.WHITE else Color.BLACK
//
//        val w = v.width
//        val h = v.height
//        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bmp)
//        canvas.drawColor(if (whiteOrBlack) Color.WHITE else Color.BLACK)
////        /** 如果不设置canvas画布为白色，则生成透明  */
//        v.draw(canvas)
////        //释放cache内存
//        v.destroyDrawingCache()
//
//        return bmp
//    }
//
//    /**
//     * 保存图片
//     */
//    fun saveImageToGallery(context: Context, bmp: Bitmap) {
//        // 系统相册路径
//        val galleryPath = (Environment.getExternalStorageDirectory().toString()
//                + File.separator + Environment.DIRECTORY_DCIM
//                + File.separator + "Camera" + File.separator)
//        //自定义路径
////        val appDir = File(Environment.getExternalStorageDirectory(), "Boohee")
//        val appDir = File(galleryPath)
//        if (!appDir.exists()) {
//            appDir.mkdir()
//        }
//        val fileName = System.currentTimeMillis().toString() + ".jpg"
//        val file = File(appDir, fileName)
//        try {
//            val fos = FileOutputStream(file)
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//            fos.flush()
//            fos.close()
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        try {
//            //把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.contentResolver,
//                file.absolutePath, fileName, null)
//            //通知扫描
//            MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), arrayOf("image/jpeg"), null)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        if (!bmp.isRecycled)
//            bmp.recycle()
//    }
}