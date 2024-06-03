package com.yandex.mobius360quest.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable

class YaAnimationDrawable(private val yaAnimation: YaAnimation) : Drawable(), Animatable {

    private val animatorSet = AnimatorSet()

    private val paint = Paint()
    private val strokePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
    }

    init {
        val animators = mutableListOf<Animator>()
        yaAnimation.shapes.forEach { shape ->
            shape.animation.forEach { anim ->
                val animator = when (anim) {
                    is Move -> {
                        val pvhX = PropertyValuesHolder.ofFloat("centerX", anim.destX)
                        val pvhY = PropertyValuesHolder.ofFloat("centerY", anim.destY)
                        ObjectAnimator.ofPropertyValuesHolder(shape, pvhX, pvhY)
                    }

                    is Rotate -> {
                        ObjectAnimator.ofFloat(shape, "angle", shape.angle + anim.angle)
                    }

                    is Scale -> {
                        ObjectAnimator.ofFloat(shape, "scale", anim.destScale)
                    }
                }
                animator.apply {
                    addUpdateListener {
                        invalidateSelf()
                    }
                    setDuration(anim.duration.toLong())
                    if (anim.cycle) {
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                    }
                    animators.add(this)
                }
            }
        }
        animatorSet.playTogether(animators)
    }

    override fun getIntrinsicWidth(): Int {
        return yaAnimation.canvasWidth
    }

    override fun getIntrinsicHeight(): Int {
        return yaAnimation.canvasHeight
    }

    override fun draw(canvas: Canvas) {
        yaAnimation.shapes.forEach { shape ->
            paint.color = shape.color
            canvas.rotate(shape.angle, shape.centerX, shape.centerY)
            canvas.scale(shape.scale, shape.scale, shape.centerX, shape.centerY)
            when (shape) {
                is Rectangle -> canvas.drawRect(shape, paint)
                is Circle -> canvas.drawCircle(shape, paint, strokePaint)
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("This method is no longer used in graphics optimizations")
    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun start() {
        animatorSet.start()
    }

    override fun stop() {
        animatorSet.cancel()
    }

    override fun isRunning(): Boolean {
        return animatorSet.isRunning
    }

    private fun Canvas.drawRect(rect: Rectangle, paint: Paint) {
        drawRect(
            rect.centerX - rect.width / 2,
            rect.centerY - rect.height / 2,
            rect.centerX + rect.width / 2,
            rect.centerY + rect.height / 2,
            paint
        )
    }

    private fun Canvas.drawCircle(circle: Circle, paint: Paint, strokePaint:Paint) {
        drawCircle(circle.centerX, circle.centerY, circle.radius, paint)
        drawCircle(circle.centerX, circle.centerY, circle.radius, strokePaint)
    }

    companion object {
        fun inflate(context: Context, filename: String): YaAnimationDrawable {
            return YaAnimationDrawable(YaAnimationParser.parseAnimation(context, filename))
        }
    }
}
