package com.ethan.clibrary.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener

/**
 * Created by Ethan Cui on 2022/11/2
 */
class TranslationYFrameLayout : FrameLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    var index = 0
    fun setTextview(number: Array<String>) {
        layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 40)
        val textview = TextViewMarquee(context)
        textview.text = number[index]
        index +=1
        textview.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        addView(textview)
        val objectAnimatorX = ObjectAnimator.ofFloat(textview, "translationY", 50f, 0f)
        val objectAnimatorY2 = ObjectAnimator.ofFloat(textview, "translationY", 0f, -50f)
        objectAnimatorX.duration = 500
        objectAnimatorY2.duration = 500
        objectAnimatorY2.startDelay = 2000
        objectAnimatorY2.addListener(onEnd = {
            textview.text = number[index]
            index += 1
            if (index >= number.size)
                index = 0
            objectAnimatorX.start()
        })
        objectAnimatorX.addListener(onStart = {
        }, onEnd = {
            objectAnimatorY2.start()
        })
        objectAnimatorX.start()
    }
}