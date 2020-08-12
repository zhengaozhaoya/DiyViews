package com.youareok.diyviews.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MatrixView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        Log.d("gzy", "onDraw, matrix:${matrix.toShortString()}") //matrix:[1.0, 0.0, 0.0][0.0, 1.0, 0.0][0.0, 0.0, 1.0]

        canvas?.let {

            Log.d("gzy","matrix from canvas:${it.matrix}")

            val rect = Rect(100, 100, 200, 200)
            it.drawRect(rect, Paint())

            it.save()
            val matrix = Matrix()

            matrix.preScale(0.5f, 0.5f, 150f, 150f)
            Log.d("gzy", "myMatrix:${matrix.toShortString()}")
            matrix.preRotate(10f, 150f, 150f)
            Log.d("gzy", "myMatrix:${matrix.toShortString()}")


            it.setMatrix(matrix)
            it.drawRect(rect, Paint().apply {
                color = Color.RED
            })
            it.restore()
        }
    }

}