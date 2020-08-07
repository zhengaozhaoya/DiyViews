package com.youareok.diyviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class WaterDropView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mRadius = 100f

    //x and y for: p0,p1,p2,p3
    private val mData = floatArrayOf(
        mRadius, 0f,
        0f, mRadius,
        -mRadius, 0f,
        0f, -mRadius
    )

    //control x and y for: p0,p1,p2,p3
    private val mControl = floatArrayOf(
        mRadius, -C * mRadius,
        mRadius, C * mRadius,

        mRadius * C, mRadius,
        -mRadius * C, mRadius,

        -mRadius, mRadius * C,
        -mRadius, -mRadius * C,

        -mRadius * C, -mRadius,
        mRadius * C, -mRadius
    )
    private val mPath = Path()
    private val mPaint = Paint()

    private var mStartTime = -1L
    private val mDuration = 2000L

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)

            mPaint.style = Paint.Style.FILL
            mPaint.color = Color.CYAN

            if (mStartTime > 0) {
                val currentTime = System.currentTimeMillis()
                val time = currentTime - mStartTime
                if (time > mDuration) {
                    mStartTime = -1
                } else {
                    when (time) {
                        in 0..(0.2 * mDuration).toInt() -> {
                            moveP0(time)
                        }
                        in (0.2 * mDuration)..0.3 * mDuration -> {
                            moveP0(time)
                            moveP1(time)
                            moveP3(time)
                        }
                        in 0.3 * mDuration..0.4 * mDuration -> {
                            moveP0(time)
                            moveP1(time)
                            moveP3(time)
                            moveP2(time)
                        }
                        in 0.4 * mDuration..0.9 * mDuration -> {

                        }
                        else -> {

                        }
                    }
                }
            }

            mPath.reset()
            mPath.moveTo(mData[0], mData[1])
            mPath.cubicTo(mControl[2], mControl[3], mControl[4], mControl[5], mData[2], mData[3])

            mPath.cubicTo(mControl[6], mControl[7], mControl[8], mControl[9], mData[4], mData[5])

            mPath.cubicTo(
                mControl[10],
                mControl[11],
                mControl[12],
                mControl[13],
                mData[6],
                mData[7]
            )

            mPath.cubicTo(mControl[14], mControl[15], mControl[0], mControl[1], mData[0], mData[1])


            it.drawPath(mPath, mPaint)
        }
    }

    private fun moveP2(time: Long) {
        mData[4] += 30f
        mControl[8] += 30f
        mControl[10] += 30f
    }

    private fun moveP3(time: Long) {
        mData[7] += 20f
        mControl[13] += 20f
        mControl[15] += 20f
    }

    private fun moveP1(time: Long) {
        mData[3] -= 20f
        mControl[5] -= 20f
        mControl[7] -= 20f
    }

    private fun moveP0(time: Long) {
        mData[0] += 30f
        mControl[0] += 30f
        mControl[2] += 30f
    }


    companion object {
        const val C = 0.551915024494f
    }
}