package com.yandex.mobius360quest.animation

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

class YaAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var yaAnimationDrawable: YaAnimationDrawable? = null

    fun loadAnimation(filename: String) {
        yaAnimationDrawable?.stop()
        yaAnimationDrawable?.callback = null

        yaAnimationDrawable = YaAnimationDrawable.inflate(context, filename).apply {
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        yaAnimationDrawable?.let {
            setMeasuredDimension(it.intrinsicWidth, it.intrinsicHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        yaAnimationDrawable?.setBounds(0, 0, width, height)
        yaAnimationDrawable?.draw(canvas)
    }
}
