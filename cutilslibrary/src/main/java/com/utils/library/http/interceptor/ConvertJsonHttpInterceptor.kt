package com.utils.library.http.interceptor

import com.utils.library.utils.isEmptyOrNull
import com.utils.library.utils.isNotEmptyStr
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.nio.charset.Charset


/**
 * Created by cuiyang on 2018/3/22.
 */
open class ConvertJsonHttpInterceptor : Interceptor {
    //    private var jsonFormatter: JsonFormatter = DefaultJsonFormatter()
    private val charset1 = Charset.forName("UTF-8")
    private var responseBody: ResponseBody? = null

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (request.method != "DELETE") {
            val response = chain.proceed(request)
            responseBody = response.body
            return setResponseBody(response)
        } else {
            //当delete操作无返回数据时chain.proceed(request)并且没有设置header(设置看UserAgentHttpInterceptor)会报错，所以在delete时如果成功会到catch中默认成功失败就会解析
            try {
                val response = chain.proceed(request)
                return setResponseBody(response)
            } catch (e: Exception) {
                return Response.Builder()
                    .request(chain.request())
                    .message("")
                    .code(204)
                    .body(responseBody)
                    .protocol(Protocol.HTTP_1_1)
                    .build()
            }
        }
    }

    private fun setResponseBody(response: Response): Response {
        val responseBody = response.body ?: return response
        val source = responseBody.source()
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer()

        var charset: Charset? = charset1
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset1)
        }

        if (responseBody.contentLength() != 0L && charset != null) {
            val json = buffer.clone().readString(charset)
            //开头为"{"即是json
            if ("{" == json.subSequence(0, 1)) {
//                val jsonObject = jsonConvert(json)
                val jsonObject = json
                //先清除原先内容
                buffer.clear()
                //将处理过的json放入
                buffer.writeString(jsonObject, charset1)
//                XLog.d(jsonFormatter.format(jsonObject.toString()))
            }
        }
        return response
    }

    /**
     * 对错误json进行处理
     */
    @Suppress("NAME_SHADOWING")
    private fun jsonConvert(json: String): String {
        return convertJson(json)
    }

    /**
     * 如果有错误返回进行包装
     */
    fun convertJson(json: String): String {
        if (json.isNotEmptyStr()) {
            val jsonObject = JSONObject(json)
            //服务端成功时没有code
            val code = jsonObject.optInt("code", 0)
            if (code != 0) {
                //自定义一个code去通知
                val cuszimeJsonObject = JSONObject()
                cuszimeJsonObject.put("code", code)
                if (jsonObject.getString("message").isEmptyOrNull())
                    cuszimeJsonObject.put("message", "服务器访问出错 no meesage")
                else cuszimeJsonObject.put("message", jsonObject.optString("message"))
                return cuszimeJsonObject.toString()
            }
//            else {
//                //对返回json包一层code用来做判断使用
//                val jsonObjectResult = JSONObject()
//                jsonObjectResult.put("code", 0)
//                jsonObject.keys().forEach {
//                    jsonObjectResult.put(it, jsonObject.get(it))
//                }
//                return jsonObjectResult
//            }
        }
        return json
    }

}