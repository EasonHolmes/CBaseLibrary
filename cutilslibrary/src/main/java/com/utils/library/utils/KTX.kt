@file:Suppress("IfThenToElvis")

package com.utils.library.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BaseInterpolator
import android.view.animation.DecelerateInterpolator
import java.math.BigDecimal


const val DIRECTION_LEFT = 0
const val DIRECTION_CENTER = 1
const val DIRECTION_RIGHT = 2

/**
 * 使用扩展函数：某类的扩展工具
 * Fragment.ktxGetColor(){} 给Fragment添加ktxGetColor方法，用法：在Fragment中直接使用ktxGetColor()
 */
fun View.scaleAnimation(duration: Long = 300, baseInterpolator: BaseInterpolator? = null) {
    val objectAnimatorX = ObjectAnimator.ofFloat(this, "scaleX", 1.2f, 0.7f, 1f)
    val objectAnimatorY = ObjectAnimator.ofFloat(this, "scaleY", 1.2f, 0.7f, 1f)
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(objectAnimatorX, objectAnimatorY)
    animatorSet.duration = duration
    animatorSet.interpolator =
        if (baseInterpolator != null) baseInterpolator else AccelerateDecelerateInterpolator()
    animatorSet.start()
}

fun View.alphaAnimation(
    visibili: Boolean = false,
    duration: Long = 500,
    baseInterpolator: BaseInterpolator? = null
) {
    val objectAnimatorAlpha =
        ObjectAnimator.ofFloat(this, "alpha", if (visibili) 1f else 0f, if (visibili) 0f else 1f)
    val animatorSet = AnimatorSet()
    animatorSet.duration = duration
    animatorSet.interpolator =
        if (baseInterpolator != null) baseInterpolator else DecelerateInterpolator()
    animatorSet.play(objectAnimatorAlpha)
    animatorSet.start()
    this.visibility= View.VISIBLE
    objectAnimatorAlpha.doOnEnd {   if (visibili) this.visibility =  View.GONE }
}


fun View.createCircularReveal(
    direction: Int,
    duration: Long = 600,
    startDelay: Long = 0
): Animator? {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        val animator = when (direction) {
            DIRECTION_LEFT -> ViewAnimationUtils.createCircularReveal(
                this,
                0, 0,
                5f, Math.hypot(this.width.toDouble(), this.height.toDouble()).toFloat()
            )
            DIRECTION_CENTER -> ViewAnimationUtils.createCircularReveal(
                this,
                this.width / 2, this.height / 2,
                5f, Math.hypot(this.width.toDouble(), this.height.toDouble()).toFloat()
            )
            else -> ViewAnimationUtils.createCircularReveal(
                this,
                this.width, 0,
                5f, Math.hypot(this.width.toDouble(), this.height.toDouble()).toFloat()
            )
        }
        animator.duration = duration
        animator.startDelay = startDelay
        animator.interpolator = AccelerateInterpolator()
        animator.start()
        this.visibility = View.VISIBLE
        return animator
    }
    return null
}

inline fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val edit = edit()
    //编译时与下面一样
    action(edit)
//    action.invoke(edit)
    edit.apply()
}

@SuppressLint("ApplySharedPref")
inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}

/**
 * double保留两位小数
 */
fun Double.toBigDecimalFormat() = BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
