package com.youareok.diyviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class BezierView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var controlX = 0f
    private var controlY = 0f
    private val path = Path()
    private val paint = Paint()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            controlX = it.x - width / 2f
            controlY = it.y - height / 2f
            invalidate()
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)
            path.reset()
            val startX = -200f
            val startY = 0f
            val endX = 200f
            val endY = 0f

            paint.strokeWidth = 3 * resources.displayMetrics.density
            it.drawLine(startX, startY, endX, endY, paint)


            paint.style = Paint.Style.STROKE
            path.moveTo(startX, startY)
            path.quadTo(controlX, controlY, endX, endY)

            it.drawPath(path, paint)
        }
    }

}