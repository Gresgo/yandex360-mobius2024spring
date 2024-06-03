package com.yandex.mobius360quest.animation

import android.content.Context
import android.graphics.Color
import java.io.BufferedReader
import java.io.InputStreamReader

object YaAnimationParser {

    fun parseAnimation(context: Context, fileName: String): YaAnimation {
        return BufferedReader(InputStreamReader(context.assets.open(fileName))).use { reader ->
            val firstLine = reader.readLine().split(" ")
            val canvasWidth = firstLine[0].toInt()
            val canvasHeight = firstLine[1].toInt()
            val shapesCount = reader.readLine().toInt()
            val shapeList = MutableList(shapesCount) { i ->
                val shapeLine = reader.readLine().split(" ")
                val shape = when (val shapeType = shapeLine[0]) {
                    "rectangle" -> {
                        Rectangle(
                            centerX = shapeLine[1].toFloat(),
                            centerY = shapeLine[2].toFloat(),
                            width = shapeLine[3].toFloat(),
                            height = shapeLine[4].toFloat(),
                            angle = shapeLine[5].toFloat(),
                            color = parseColor(shapeLine[6]),
                            animation = emptyList()
                        )
                    }

                    "circle" -> {
                        Circle(
                            centerX = shapeLine[1].toFloat(),
                            centerY = shapeLine[2].toFloat(),
                            radius = shapeLine[3].toFloat(),
                            color = parseColor(shapeLine[4]),
                            animation = emptyList()
                        )
                    }

                    else -> throw IllegalStateException("Unknown shape type: $shapeType")
                }
                val animCount = reader.readLine().toInt()
                val animationList = MutableList(animCount) {
                    val animLine = reader.readLine().split(" ")
                    val anim = when (val animType = animLine[0]) {
                        "move" -> {
                            Move(
                                destX = animLine[1].toFloat(),
                                destY = animLine[2].toFloat(),
                                duration = animLine[3].toInt(),
                                cycle = animLine[4] == "cycle "
                            )
                        }

                        "rotate" -> {
                            Rotate(
                                angle = animLine[1].toFloat(),
                                duration = animLine[2].toInt(),
                                cycle = animLine[3] == "cycle "
                            )
                        }

                        "scale" -> {
                            Scale(
                                destScale = animLine[1].toFloat(),
                                duration = animLine[2].toInt(),
                                cycle = animLine[3] == "cycle "
                            )
                        }

                        else -> throw IllegalStateException("Unknown animation type: $animType")
                    }
                    anim
                }
                shape.apply { animation = animationList }
            }
            YaAnimation(canvasWidth, canvasHeight, shapeList)
        }
    }

    private fun parseColor(color: String): Int {
        return when (color) {
            "black" -> Color.BLACK
            "red" -> Color.RED
            "white" -> Color.WHITE
            "yellow" -> Color.YELLOW
            else -> throw IllegalStateException("Unknown color: $color")
        }
    }

}
