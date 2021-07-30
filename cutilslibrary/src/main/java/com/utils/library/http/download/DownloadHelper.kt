package com.utils.library.http.download

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * 简单的下载工具类，不带断点续传
 * 需断点续传使用rxdownload https://github.com/ssseasonnn/RxDownload
 */
class DownloadHelper(
    /**
     * 下载进度、完成、失败等的回调事件
     */
    private val mDownloadListener: DownloadListener
) {
    /**
     * 网络工具retrofit
     */
    private val retrofit: Retrofit

    /**
     * 清除线程需要用到的
     */
    private var disposable: Disposable? = null

    /**
     * 进行文件下载
     * @param url 网址
     * @param destDir 文件目录
     * @param fileName 文件名称
     */
    fun downloadFile(url: String, destDir: String, fileName: String) {
        dispose()
        mDownloadListener.onStartDownload()
        retrofit.create(DownloadService::class.java)
            .download(url) // 请求网络 在调度者的io线程
            .subscribeOn(Schedulers.io()) // 指定线程保存文件
            .observeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map<File>(Function { responseBody ->
                saveFile(
                    responseBody.byteStream(),
                    destDir,
                    fileName
                )
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseDownloadObserver<File?>() {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }
                override fun onDownloadSuccess(file: File?) {
                    mDownloadListener.onFinishDownload(file)
                }

                override fun onDownloadError(e: Throwable?) {
                    mDownloadListener.onFail(e)
                }
            })
    }

    /**
     * 销毁
     */
    fun dispose() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    /**
     * 将文件写入本地
     *
     * @param destFileDir  目标文件夹
     * @param destFileName 目标文件名
     * @return 写入完成的文件
     * @throws IOException IO异常
     */
    @Throws(IOException::class)
    private fun saveFile(`is`: InputStream?, destFileDir: String, destFileName: String): File {
        val buf = ByteArray(2048)
        var len: Int
        var fos: FileOutputStream? = null
        return try {
            val dir = File(destFileDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, destFileName)
            if (file.exists()) {
                file.delete()
            }
            fos = FileOutputStream(file)
            while (`is`!!.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
            }
            fos.flush()
            file
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        /**
         * 超时15s
         */
        private const val DEFAULT_TIMEOUT = 15
    }

    /**
     * 构造函数初始化
     *
     * @param listener 回调函数
     */
    init {
        val mInterceptor = DownloadInterceptor(mDownloadListener)
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(mInterceptor)
            .retryOnConnectionFailure(true)
            .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.baseurl.com/")
            .client(httpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

    }
}