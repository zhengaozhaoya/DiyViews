package com.youareok.diyviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View

class YinYangFish @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)

            val p1 = Path()
            val p2 = Path()
            val p3 = Path()
            val p4 = Path()

            p1.addCircle(0f, 0f, 200f, Path.Direction.CW)
            canvas.drawPath(p1, Paint().apply {
                style = Paint.Style.STROKE
            })

            p2.addRect(0f, -200f, 200f, 200f, Path.Direction.CW)
            p3.addCircle(0f, -0100f, 100f, Path.Direction.CW)
            p4.addCircle(0f, 100f, 100f, Path.Direction.CW)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                p1.op(p2, Path.Op.DIFFERENCE)
                p1.op(p3, Path.Op.UNION)
                p1.op(p4, Path.Op.DIFFERENCE)
            }

            canvas.drawPath(p1, Paint())

            canvas.drawCircle(0f, -100f, 30f, Paint().apply {
                color = Color.WHITE
            })
            canvas.drawCircle(0f, 100f, 30f, Paint().apply {
                color = Color.BLACK
            })
        }
    }

}