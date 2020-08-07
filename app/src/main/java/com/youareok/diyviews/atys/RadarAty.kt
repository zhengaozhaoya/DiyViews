package com.youareok.diyviews.atys

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.youareok.diyviews.atys.RadarAty.Companion.TAG


class RadarAty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = TAG

        setContentView(RadarView(this).apply {
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        })
    }


    companion object {
        const val TAG = "RadarAty"
    }
}

fun toString(matrix: Paint.FontMetrics): String {
    return "ascent:${matrix.ascent}, descent:${matrix.descent}, top:${matrix.top}, bottom:${matrix.bottom},  leading:${matrix.leading}"
}

class RadarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var count = 6
    var angle = 2 * Math.PI / 6f

    var radius = 0f
    var distance = 0f

    private val paint = Paint()
    private val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = Math.min(w.toFloat(), h.toFloat()) / 2 * 0.9f
        distance = radius / count
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.translate(width / 2f, height / 2f)

            paint.style = Paint.Style.STROKE
            paint.isAntiAlias = true

            drawPolygon(it)
            drawLines(it)
            drawTexts(it)
            drawRegion(it)
        }
    }

    private fun drawPolygon(it: Canvas) {
        for (i in 0 until count) {
            path.reset()
            path.moveTo((i + 1) * distance, 0f)

            for (j in 1 until count) {
                val x = (Math.cos(angle * j) * distance * (i + 1)).toFloat()
                val y = (Math.sin(angle * j) * distance * (i + 1)).toFloat()
                path.lineTo(x, y)
                Log.d(TAG, "drawPolygon, x:$x,y:$y")
            }

            path.close()
            it.drawPath(path, paint)
        }
    }

    private fun drawLines(it: Canvas) {
        for (i in 0 until count) {
            path.reset()
            path.moveTo(0f, 0f)
            val x = (count) * distance * Math.cos(i * angle)
            val y = (count) * distance * Math.sin(i * angle)
            path.lineTo(x.toFloat(), y.toFloat())
            Log.d(TAG, "drawLines, x:$x,y:$y")
            it.drawPath(path, paint)
        }
    }

    private fun drawTexts(it: Canvas) {
        val chars = CharArray(count) {
            'a' + it
        }
        paint.textSize = 23 * context.resources.displayMetrics.density
        val fontMetrics = paint.fontMetrics
        Log.d(
            TAG,
            "drawTexts, fontMetrics:${toString(fontMetrics)}, density:${context.resources.displayMetrics.density}"
        )
        val fontHeight = fontMetrics.descent - fontMetrics.ascent
        for (i in 0 until count) {
            val x = (count * distance + fontHeight / 2) * Math.cos(i * angle).toFloat()
            val y = (count * distance + fontHeight / 2) * Math.sin(i * angle).toFloat()
            when (angle * i) {
                in 0f..(Math.PI / 2f).toFloat() -> {
                    it.drawText(chars, i, 1, x, y, paint)
                }
                in Math.PI / 2f..Math.PI -> {
                    val dis = paint.measureText(chars, i, 1)
                    it.drawText(chars, i, 1, x - dis, y, paint)
                }
                in Math.PI..Math.PI * 2 / 3f -> {
                    val dis = paint.measureText(chars, i, 1)
                    it.drawText(chars, i, 1, x - dis, y, paint)
                }
                else -> {
                    it.drawText(chars, i, 1, x, y, paint)
                }
            }
        }
    }

    private fun drawRegion(it: Canvas) {
        val path = Path()
        val data = doubleArrayOf(1.5, 2.3, 3.4, 4.8, 5.2, 6.0)
        val maxValue = 6.0
        paint.style = Paint.Style.FILL
        for (i in 0 until count) {
            val percent: Double = data[i] / maxValue
            val x =
                (radius * Math.cos(angle * i) * percent).toFloat()
            val y =
                (radius * Math.sin(angle * i) * percent).toFloat()
            if (i == 0) {
                path.moveTo(x, 0f)
            } else {
                path.lineTo(x, y)
            }
            //绘制小圆点
            it.drawCircle(x, y, 10f, paint)
        }
        paint.setStyle(Paint.Style.STROKE)
        it.drawPath(path, paint)
        paint.setAlpha(127)
        //绘制填充区域
        //绘制填充区域
        paint.setStyle(Paint.Style.FILL_AND_STROKE)
        it.drawPath(path, paint)
        paint.alpha = 255
    }
}