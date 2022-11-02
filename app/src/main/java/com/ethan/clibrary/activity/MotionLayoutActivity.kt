package com.ethan.clibrary.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.Transition
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.ethan.clibrary.R

/**
 * Created by Ethan'Cui on 2021/9/29
 */
class MotionLayoutActivity : AppCompatActivity(R.layout.activity_motion_layout) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val imageview = findViewById<ImageView>(R.id.imageView2)
//        val anim = ObjectAnimator.ofFloat(imageview,"translationY",-100f,100f).apply {
//            duration = 1000L
//            repeatMode = ValueAnimator.REVERSE
//            repeatCount = ValueAnimator.INFINITE
//        }
//        anim.start()
//        findViewById<MotionLayout>(R.id.constraintlayout).setTransitionListener(object :
//            MotionLayout.TransitionListener {
//            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//                anim.cancel()
//            }
//
//            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//            }
//
//            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//            }
//
//            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
//            }
//
//        })
    }
}