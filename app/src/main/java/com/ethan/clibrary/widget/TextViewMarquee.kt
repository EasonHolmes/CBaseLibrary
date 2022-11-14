package com.ethan.clibrary.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

class TextViewMarquee : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(
        context, attrs
    ) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
    }

    /**
     * 需要重写返回为true
     */
    override fun isFocused() = true


}
