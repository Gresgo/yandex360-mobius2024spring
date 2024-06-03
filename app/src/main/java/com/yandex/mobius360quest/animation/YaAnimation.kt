package com.yandex.mobius360quest.animation

import android.graphics.Color
import androidx.annotation.ColorInt

class YaAnimation(
    val canvasWidth: Int,
    val canvasHeight: Int,
    val shapes: List<Shape>
)

sealed class Shape(
    var centerX: Float = 0f,
    var centerY: Float = 0f,
    var angle: Float = 0f,
    var scale: Float = 1f,
    @ColorInt var color: Int = Color.BLACK,
    var animation: List<ShapeAnimation> = emptyList()
)

class Rectangle(
    val width: Float,
    val height: Float,
    centerX: Float,
    centerY: Float,
    angle: Float,
    @ColorInt color: Int,
    animation: List<ShapeAnimation>
) : Shape(centerX, centerY, angle, 1f, color, animation)

class Circle(
    val radius: Float,
    centerX: Float,
    centerY: Float,
    @ColorInt color: Int,
    animation: List<ShapeAnimation>
) : Shape(centerX, centerY, 0f, 1f, color, animation)

sealed class ShapeAnimation(
    val duration: Int,
    val cycle: Boolean
)

class Move(
    val destX: Float,
    val destY: Float,
    duration: Int,
    cycle: Boolean
) : ShapeAnimation(duration, cycle)

class Rotate(
    val angle: Float,
    duration: Int,
    cycle: Boolean
) : ShapeAnimation(duration, cycle)

class Scale(
    val destScale: Float,
    duration: Int,
    cycle: Boolean
) : ShapeAnimation(duration, cycle)
