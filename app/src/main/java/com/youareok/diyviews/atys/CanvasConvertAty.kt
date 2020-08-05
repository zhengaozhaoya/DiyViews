package com.youareok.diyviews.atys

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

class CanvasConvertAty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = TAG

        setContentView(CanvasConvertView(this).apply {
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        })
    }


    companion object {
        const val TAG = "CanvasConvertAty"
    }
}

class CanvasConvertView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val SIZE = 30
    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)

            paint.strokeWidth = 10f
            paint.setColor(Color.BLACK)
            paint.style = Paint.Style.STROKE

            it.drawCircle(0f, 0f, width / 2f, paint)
            it.drawCircle(0f, 0f, width / 2f - SIZE, paint)

            it.drawLine(0f,0f,0f,30f,paint)

            for (i in 0..35) {
                it.drawLine(0f, -width / 2f, 0f, -width / 2f + SIZE, paint)

                it.rotate(10f)
            }


            paint.strokeWidth = 3f
            val rectF = RectF(-150f,-150f,150f,150f)
            it.drawArc(rectF,-10f,40f,true,paint)

            it.skew(0.3f,0f)
            paint.setColor(Color.BLUE)
            it.drawArc(rectF,-10f,40f,true,paint)

        }
    }

}
