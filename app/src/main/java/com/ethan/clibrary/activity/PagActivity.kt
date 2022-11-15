package com.ethan.clibrary.activity

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.ethan.clibrary.R
import org.libpag.PAGComposition
import org.libpag.PAGFile
import org.libpag.PAGImage
import org.libpag.PAGPlayer
import org.libpag.PAGSurface
import org.libpag.PAGTimeStretchMode
import org.libpag.PAGView
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer


/**
 * Created by Ethan Cui on 2022/11/11
 */
class PagActivity : AppCompatActivity(R.layout.activity_pag) {
    private val TAG = "APIsDetailActivity"
    private var pagFile: PAGFile? = null
    private val exportButton: Button by lazy { findViewById<View>(R.id.export) as Button }

    // video export
    private val MIME_TYPE = "video/avc" // H.264 Advanced Video Coding

    private val FRAME_RATE = 30
    private val IFRAME_INTERVAL = 10 // 10 seconds between I-frames

    private var mEncoder: MediaCodec? = null
    private var mMuxer: MediaMuxer? = null
    private var mTrackIndex = 0
    private var mMuxerStarted = false
    private var mBufferInfo: MediaCodec.BufferInfo? = null
    private val VERBOSE = true
    private val mBitRate = 8000000
    private var pagPlayer: PAGPlayer? = null
    private var pagComposition: PAGComposition? = null

    private val OUTPUT_DIR: File = Environment.getExternalStorageDirectory()

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )
    val pagView :PAGView by lazy { findViewById<PAGView>(R.id.pagView) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pag)
        exportButton.setVisibility(View.INVISIBLE)
        initPAGView()
    }

    private fun initPAGView() {
        val intent = intent ?: return
        var pagFile1: PAGFile? = null
        val index = intent.getIntExtra("API_TYPE", 0)
        pagView.stop()
        when (index) {
            // Basic usage
            0 -> {
                pagFile1 = PAGFile.Load(assets, "data_video.pag")
                pagView.composition = pagFile1
                exportButton.setVisibility(View.VISIBLE)
            }
            // 替换pag里的文字
            1 -> {
                pagFile1 = PAGFile.Load(assets, "test2.pag")
                testEditText(pagFile1, pagView)
                pagView.composition = pagFile1
            }
            //替换pag里的图片
            2 -> {
                pagFile1 = PAGFile.Load(assets, "replacement.pag")
                testReplaceImage(pagFile1, pagView)
                pagView.composition = pagFile1
            }
            //使用layer加载多个pag
            3 -> {
                val manager = this.windowManager
                val outMetrics = DisplayMetrics()
                manager.defaultDisplay.getMetrics(outMetrics)
                val width = outMetrics.widthPixels
                val height = outMetrics.heightPixels
                pagComposition = PAGComposition.Make(width, height)
                val itemWidth = (width / 5).toFloat()
                val itemHeight = 300f
                var i = 0
                while (i < 10) {
                    pagComposition?.addLayer(
                        getPagFile(
                            i / 5,
                            i % 5,
                            "$i.pag",
                            itemWidth,
                            itemHeight
                        )
                    )
                    i++
                }
                pagView.composition = pagComposition
            }
            5 -> {
                val pagFile = PAGFile.Load(assets, "test2.pag")
                val pagFile2 = PAGFile.Load(assets, "test.pag")
                pagComposition = PAGComposition.Make(pagFile.width(), pagFile.height())
                // 播放1~3s
                pagFile.setTimeStretchMode(PAGTimeStretchMode.None)
                pagFile.setStartTime(-1000000)
                pagFile.setDuration(3000000)
                pagComposition?.addLayer(pagFile)
                pagComposition?.addLayer(pagFile2)
                pagView.composition = pagComposition
            }
            else -> {}
        }
        pagView.setRepeatCount(0)
        pagView.play()
    }

    private fun createPAGImage(): PAGImage? {
        val assetManager = assets
        var stream: InputStream? = null
        try {
            stream = assetManager.open("test.png")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val bitmap = BitmapFactory.decodeStream(stream) ?: return null
        return PAGImage.FromBitmap(bitmap)
    }

    /**
     * Test replace image.
     */
    fun testReplaceImage(pagFile: PAGFile?, pagView: PAGView?) {
        if (pagFile == null || pagView == null || pagFile.numImages() <= 0) return
        pagFile.replaceImage(0, createPAGImage())
    }

    /**
     * Test edit text.
     */
    fun testEditText(pagFile: PAGFile?, pagView: PAGView?) {
        if (pagFile == null || pagView == null || pagFile.numTexts() <= 0) return
        val textData = pagFile.getTextData(0)
        textData.text = "replacement test"
        pagFile.replaceText(0, textData)
    }

    private fun verifyStoragePermissions(activity: Activity) {
        try {
            //检测是否有写的权限
            val permission: Int = ActivityCompat.checkSelfPermission(
                activity,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPagFile(
        row: Int,
        colume: Int,
        name: String,
        itemWidth: Float,
        itemHeight: Float
    ): PAGFile? {
        val pagFile = PAGFile.Load(assets, name)
        val matrix = Matrix()
        val scaleX = itemWidth * 1.0f / pagFile.width()
        matrix.preScale(scaleX, scaleX)
        matrix.postTranslate(itemWidth * colume, row * itemHeight)
        pagFile.setMatrix(matrix)
        pagFile.setDuration(10000000)
        return pagFile
    }

    fun export(view: View?) {
        verifyStoragePermissions(this)
        pagExportToMP4()
    }

    // video export
    private fun pagExportToMP4() {
        try {
            prepareEncoder()
            val totalFrames = (pagFile!!.duration() * pagFile!!.frameRate() / 1000000) as Int
            for (i in 0 until totalFrames) {
                // Feed any pending encoder output into the muxer.
                drainEncoder(false)
                generateSurfaceFrame(i)
            }
            drainEncoder(true)
        } finally {
            releaseEncoder()
        }
    }

    private fun prepareEncoder() {
        pagFile = PAGFile.Load(assets, "replacement.pag")
        mBufferInfo = MediaCodec.BufferInfo()
        var width: Int = pagFile!!.width()
        var height: Int = pagFile!!.height()
        if (width % 2 == 1) {
            width--
        }
        if (height % 2 == 1) {
            height--
        }
        val format = MediaFormat.createVideoFormat(MIME_TYPE, width, height)
        format.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
        )
        format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL)
        try {
            mEncoder = MediaCodec.createEncoderByType(MIME_TYPE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mEncoder!!.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        if (pagPlayer == null) {
            val pagSurface = PAGSurface.FromSurface(mEncoder!!.createInputSurface())
            pagPlayer = PAGPlayer()
            pagPlayer!!.setSurface(pagSurface)
            pagPlayer!!.setComposition(pagFile)
            pagPlayer!!.setProgress(0.0)
        }
        mEncoder!!.start()
        val outputPath: String = File(
            OUTPUT_DIR,
            "test." + width + "x" + height + ".mp4"
        ).toString()
        try {
            mMuxer = MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        } catch (ioe: IOException) {
            throw RuntimeException("MediaMuxer creation failed", ioe)
        }
        mTrackIndex = -1
        mMuxerStarted = false
    }

    /**
     * Releases encoder resources.  May be called after partial / failed initialization.
     */
    private fun releaseEncoder() {
        if (mEncoder != null) {
            mEncoder!!.stop()
            mEncoder!!.release()
            mEncoder = null
        }
        if (mMuxer != null) {
            mMuxer!!.stop()
            mMuxer = null
        }
        pagPlayer = null
    }

    private fun drainEncoder(endOfStream: Boolean) {
        val TIMEOUT_USEC = (10000 * 60 / FRAME_RATE) as Int
        if (endOfStream) {
            mEncoder!!.signalEndOfInputStream()
        }
        var encoderOutputBuffers: Array<ByteBuffer?> = mEncoder!!.getOutputBuffers()
        while (true) {
            val encoderStatus: Int = mEncoder!!.dequeueOutputBuffer(mBufferInfo!!,
                TIMEOUT_USEC!!.toLong()
            )
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // no output available yet
                if (!endOfStream) {
                    break // out of while
                } else {
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                // not expected for an encoder
                encoderOutputBuffers = mEncoder!!.getOutputBuffers()
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // should happen before receiving buffers, and should only happen once
                if (mMuxerStarted) {
                    throw RuntimeException("format changed twice")
                }
                val newFormat: MediaFormat = mEncoder!!.getOutputFormat()

                // now that we have the Magic Goodies, start the muxer
                mTrackIndex = mMuxer!!.addTrack(newFormat)
                mMuxer!!.start()
                mMuxerStarted = true
            } else if (encoderStatus < 0) {
                // let's ignore it
            } else {
                val encodedData: ByteBuffer = encoderOutputBuffers[encoderStatus]
                    ?: throw RuntimeException(
                        "encoderOutputBuffer " + encoderStatus +
                                " was null"
                    )
                if (mBufferInfo!!.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG !== 0) {
                    // The codec config data was pulled out and fed to the muxer when we got
                    // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                    mBufferInfo!!.size = 0
                }
                if (mBufferInfo!!.size !== 0) {
                    if (!mMuxerStarted) {
                        throw RuntimeException("muxer hasn't started")
                    }
                    // adjust the ByteBuffer values to match BufferInfo (not needed?)
                    encodedData.position(mBufferInfo!!.offset)
                    encodedData.limit(mBufferInfo!!.offset + mBufferInfo!!.size)
                    mMuxer!!.writeSampleData(mTrackIndex, encodedData, mBufferInfo!!)
                }
                mEncoder!!.releaseOutputBuffer(encoderStatus, false)
                if (mBufferInfo!!.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM !== 0) {
                    break // out of while
                }
            }
        }
    }

    private fun generateSurfaceFrame(frameIndex: Int) {
        val totalFrames = (pagFile!!.duration() * pagFile!!.frameRate() / 1000000) as Int
        val progress = frameIndex % totalFrames * 1.0f / totalFrames
        pagPlayer!!.setProgress(progress!!.toDouble())
        pagPlayer!!.flush()
    }

    override fun onDestroy() {
        pagView.stop()
        super.onDestroy()

    }
}