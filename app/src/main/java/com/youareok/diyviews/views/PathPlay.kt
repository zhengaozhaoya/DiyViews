package com.youareok.diyviews.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.youareok.diyviews.R

class PathPlay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mArrowBmp = BitmapFactory.decodeResource(resources, R.drawable.arrow)
    private val mPaint = Paint()
    private val mPath = Path()
    private val mPathMeasure = PathMeasure()
    private var mCurrentLength = 0f
    private val mMatrix = Matrix()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)

            mPaint.style = Paint.Style.STROKE

            mPath.reset()
            mPath.addCircle(0f, 0f, 300f, Path.Direction.CW)

            mPathMeasure.setPath(mPath, false)

            mCurrentLength = (mCurrentLength + 5f) % mPathMeasure.length
            val posArray = floatArrayOf(0f, 0f)
            val tanArray = floatArrayOf(0f, 0f)
            mPathMeasure.getPosTan(mCurrentLength, posArray, tanArray)

            mMatrix.reset()

//            val degree = Math.atan2(tanArray[1].toDouble(), tanArray[0].toDouble()) * 180 / Math.PI
//            mMatrix.postRotate(degree.toFloat(), mArrowBmp.width / 2f, mArrowBmp.height / 2f)
//            mMatrix.postTranslate(
//                posArray[0] - mArrowBmp.width / 2f,
//                posArray[1] - mArrowBmp.height / 2f
//            )

            mPathMeasure.getMatrix(
                mCurrentLength,
                mMatrix,
                PathMeasure.POSITION_MATRIX_FLAG or PathMeasure.TANGENT_MATRIX_FLAG
            )

            it.drawBitmap(mArrowBmp, mMatrix, mPaint)
            it.drawPath(mPath, mPaint)

            invalidate()
        }

    }

}