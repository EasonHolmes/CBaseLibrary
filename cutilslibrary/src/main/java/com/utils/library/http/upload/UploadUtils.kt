package com.shijieyun.kb.app.http.upload

import android.util.Log
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


open class UploadUtils {

    // 单文件上传
      fun uploadFile() {
        // Map is used to multipart the file using okhttp3.RequestBody
        val file: File = File("mediaPath")

        // Parsing any Media type file
        val requestBody: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val filename: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)
//        val getResponse: ApiConfig = AppConfig.getRetrofit().create(ApiConfig::class.java)
//        val call: Call<ServerResponse> = getResponse.uploadFile(fileToUpload, filename)
//        call.enqueue(object : Callback<ServerResponse?>() {
//            fun onResponse(call: Call<ServerResponse?>?, response: Response<ServerResponse?>) {
//                val serverResponse: ServerResponse = response.body()
//                if (serverResponse != null) {
//                    if (serverResponse.getSuccess()) {
//                        Toast.makeText(
//                            getApplicationContext(),
//                            serverResponse.getMessage(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(
//                            getApplicationContext(),
//                            serverResponse.getMessage(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    assert(serverResponse != null)
//                    Log.v("Response", serverResponse.toString())
//                }
//                progressDialog.dismiss()
//            }
//
//            fun onFailure(call: Call<ServerResponse?>?, t: Throwable?) {}
//        })
    }

    // 多文件上传
      fun uploadMultipleFiles() {
        // Map is used to multipart the file using okhttp3.RequestBody
        val file: File = File("mediaPath")
        val file1: File = File("mediaPath1")

        // Parsing any Media type file
        val requestBody1: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val requestBody2: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload1: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody1)
        val fileToUpload2: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody2)
//        val getResponse: ApiConfig = AppConfig.getRetrofit().create(ApiConfig::class.java)
//        val call: Call<ServerResponse> = getResponse.uploadMulFile(fileToUpload1, fileToUpload2)
//        call.enqueue(object : Callback<ServerResponse?>() {
//            fun onResponse(call: Call<ServerResponse?>?, response: Response<ServerResponse?>) {
//                val serverResponse: ServerResponse = response.body()
//                if (serverResponse != null) {
//                    if (serverResponse.getSuccess()) {
//                        Toast.makeText(
//                            getApplicationContext(),
//                            serverResponse.getMessage(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(
//                            getApplicationContext(),
//                            serverResponse.getMessage(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    assert(serverResponse != null)
//                    Log.v("Response", serverResponse.toString())
//                }
//                progressDialog.dismiss()
//            }
//
//            fun onFailure(call: Call<ServerResponse?>?, t: Throwable?) {}
//        })
    }

    //带进度上传requestBody
    //https://github.com/Musfick/retrofit-kotlin-upload-progress
    fun upload(file : File) {
        val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

        bodyBuilder.addFormDataPart("title", "Post Title")
        bodyBuilder.addFormDataPart("description", "Post Description")
        val requestBody: RequestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)

        bodyBuilder.addFormDataPart(
            "image",
            file.name,
            requestBody
        )
        bodyBuilder.addFormDataPart(
            "video",
            file.name,
            requestBody)

        val requestBodys = bodyBuilder.build()
        val requestBodyWithProgress = ReqBodyWithProgress(requestBodys) { progress ->
            //If you use logging interceptor this will trigger twice
            Log.d("Upload Progress:", "" + progress)
        }
//        val call = service.createPost(requestBodyWithProgress)
    }
}