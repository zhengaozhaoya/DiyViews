package com.youareok.diyviews.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

class WaterDropView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mRadius = 100f

    private var mData = floatArrayOf()
    private var mControl = floatArrayOf()
    private val mPath = Path()
    private val mPaint = Paint()

    private var mStartTime = -1L
    private val mDuration = 2000L

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(mRadius, height / 2f)

            mPaint.style = Paint.Style.FILL
            mPaint.color = Color.CYAN

            resetDatas()

            if (mStartTime > 0) {
                val currentTime = System.currentTimeMillis()
                val time = currentTime - mStartTime
                if (time > mDuration) {
                    mStartTime = 0
                } else {
                    when (time) {
                        in 0..(0.2 * mDuration).toInt() -> {
                            val interceptor = time / (0.2f * mDuration)
                            moveP0(interceptor, mRadius.toLong())
                        }
                        in (0.2 * mDuration).toInt()..(0.5 * mDuration).toInt() -> {
                            val interceptor = (time.toFloat() / mDuration - 0.2f) / 0.3f
                            moveP0(1f, mRadius.toLong())

                            moveP1X(interceptor, (mRadius / 2).toLong())
                            moveP3X(interceptor, (mRadius / 2).toLong())
                        }
                        in (0.5 * mDuration).toInt()..(0.8 * mDuration).toInt() -> {
                            val inter = (time.toFloat() / mDuration - 0.5f) / 0.3f

                            moveP0(1f, mRadius.toLong())
                            moveP1X(1f, (mRadius / 2).toLong())
                            moveP3X(1f, (mRadius / 2).toLong())

                            moveP1X(inter, (mRadius / 2).toLong())
                            moveP3X(inter, (mRadius / 2).toLong())

                            moveP2(inter, (mRadius / 2).toLong())
                        }
                        in (0.8 * mDuration).toInt()..(0.9 * mDuration).toInt() -> {
                            val inter = ((time.toFloat() / mDuration) - 0.8f) / 0.1f

                            moveP0(1f, mRadius.toLong())
                            moveP1X(1f, (mRadius / 2).toLong())
                            moveP3X(1f, (mRadius / 2).toLong())
                            moveP1X(1f, (mRadius / 2).toLong())
                            moveP3X(1f, (mRadius / 2).toLong())
                            moveP2(1f, (mRadius / 2).toLong())

                            moveP2(inter, (mRadius / 2 * 1.3).toLong())
                        }
                        in (0.9 * mDuration).toInt()..(1 * mDuration).toInt() -> {
                            val inter = ((time.toFloat() / mDuration) - 0.9f) / 0.1f

                            moveP0(1f, mRadius.toLong())
                            moveP1X(1f, (mRadius / 2).toLong())
                            moveP3X(1f, (mRadius / 2).toLong())
                            moveP1X(1f, (mRadius / 2).toLong())
                            moveP3X(1f, (mRadius / 2).toLong())
                            moveP2(1f, (mRadius / 2).toLong())
                            moveP2(1f, (mRadius / 2 * 1.3).toLong())

                            moveP2(inter, (-mRadius / 2 * 0.3).toLong())
                        }
                    }

                    moveP0(time / mDuration.toFloat(), 500)
                    moveP1X(time / mDuration.toFloat(), 500)
                    moveP2(time / mDuration.toFloat(), 500)
                    moveP3X(time / mDuration.toFloat(), 500)
                }
            } else {
                mStartTime = System.currentTimeMillis() + 300L
            }

            Log.d(
                TAG,
                "onDraw, mData:${Arrays.toString(mData)} \n mControlData:${Arrays.toString(
                    mControl
                )}"
            )

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
            postInvalidateDelayed(10)
        }
    }

    private fun resetDatas() {
        val offset = -0
        mData = floatArrayOf(
            mRadius + offset, 0f,
            0f + offset, mRadius,
            -mRadius + offset, 0f,
            0f + offset, -mRadius
        )
        mControl = floatArrayOf(
            mRadius + offset, -C * mRadius,
            mRadius + offset, C * mRadius,

            (mRadius + offset) * C, mRadius,
            (-mRadius + offset) * C, mRadius,

            -mRadius + offset, mRadius * C,
            -mRadius + offset, -mRadius * C,

            (-mRadius + offset) * C, -mRadius,
            (mRadius + offset) * C, -mRadius
        )
    }

    private fun moveP2(interceptor: Float, totalDistance: Long) {
        mData[4] += interceptor * totalDistance
        mControl[8] += interceptor * totalDistance
        mControl[10] += interceptor * totalDistance
    }

    private fun moveP3(intercaptor: Float, totalDistance: Long) {
//        mData[7] += intercaptor * totalDistance
        mControl[13] += intercaptor * totalDistance
        mControl[15] += intercaptor * totalDistance
    }

    private fun moveP1(intercaptor: Float, totalDistance: Long) {
//        mData[3] += intercaptor * totalDistance
        mControl[5] += intercaptor * totalDistance
        mControl[7] += intercaptor * totalDistance
    }

    private fun moveP3X(intercaptor: Float, totalDistance: Long) {
        mData[6] += intercaptor * totalDistance
        mControl[12] += intercaptor * totalDistance
        mControl[14] += intercaptor * totalDistance
    }

    private fun moveP1X(intercaptor: Float, totalDistance: Long) {
        mData[2] += intercaptor * totalDistance
        mControl[4] += intercaptor * totalDistance
        mControl[6] += intercaptor * totalDistance
    }


    private fun moveP0(intercaptor: Float, totalDistance: Long) {
        mData[0] += intercaptor * totalDistance
        mControl[0] += intercaptor * totalDistance
        mControl[2] += intercaptor * totalDistance
    }


    companion object {
        const val C = 0.551915024494f
        const val TAG = "WaterDropView"
    }
}