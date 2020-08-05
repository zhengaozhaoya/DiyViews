package com.youareok.diyviews.atys

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.youareok.diyviews.R
import com.youareok.diyviews.atys.BasicGraphicsAty.Companion.TAG
import java.util.*

class BasicGraphicsAty : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_graphics)
        supportActionBar?.title = TAG
    }


    companion object {
        const val TAG = "BasicGraphicsAty"
    }
}


class GraphicsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private var rectf: RectF? = null
    private val random = Random()

    init {
        mPaint.setColor(Color.BLACK)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "onSizeChanged, size:${Size(w, h)}")
        }
        val radius = w / 2f
        rectf = RectF(-radius, -radius, radius, radius)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d(TAG,"onTouchEvent, event:${event}")
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.translate(width / 2f, height / 2f)
            mPaint.style = Paint.Style.STROKE
            it.drawCircle(0f, 0f, width.div(2f), mPaint)
            val rectF = rectf

            var currentAngle = -90f
            mPaint.style = Paint.Style.FILL_AND_STROKE
            for (i in 0..3) {
                val sweepAngle = random.nextFloat() * 20f + 70f
                Log.d(TAG, "onDraw, sweepAngle:$sweepAngle")
                val color = Color.rgb(
                    random.nextInt(255),
                    random.nextInt(255),
                    random.nextInt(255)
                )
                mPaint.setColor(color)
                it.drawArc(rectF!!, currentAngle, sweepAngle, true, mPaint)
                currentAngle += sweepAngle
            }
        }
    }


}