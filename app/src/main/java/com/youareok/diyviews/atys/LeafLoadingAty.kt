package com.youareok.diyviews.atys

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.youareok.diyviews.R

class LeafLoadingAty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaf_loading)
        supportActionBar?.title = TAG


    }


    companion object {
        const val TAG = "LeafLoadingAty"
    }

}

class LeafLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private val mRectF: RectF = RectF()
    private var mArcWidth = 0f
    private var mInnerArcWidth = 0f
    private var mOuterStrokeWidth = 10f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mPaint.isAntiAlias = true
        mArcWidth = height - 10f
        mInnerArcWidth = mArcWidth - mOuterStrokeWidth * 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)
            mRectF.left = -width / 2f
            mRectF.top = -height / 2f
            mRectF.right = -width / 2f + mArcWidth
            mRectF.bottom = height / 2f


            mPaint.style = Paint.Style.FILL
            mPaint.color = Color.YELLOW
            it.drawArc(mRectF, 90f, 180f, false, mPaint)
            mRectF.left = -width / 2f + mArcWidth / 2f
            mRectF.right = width / 2f - mArcWidth / 2f
            it.drawRect(mRectF, mPaint)

            mPaint.color = Color.BLUE

            mRectF.left = -width / 2f + mOuterStrokeWidth
            mRectF.top = mRectF.top + mOuterStrokeWidth
            mRectF.bottom = mRectF.bottom - mOuterStrokeWidth
            mRectF.right = -width / 2f + mOuterStrokeWidth + mInnerArcWidth

            it.drawArc(mRectF, 90f, 180f, false, mPaint)
            mRectF.left = -width / 2f + mOuterStrokeWidth + mInnerArcWidth / 2f
            mRectF.right = width / 2f - mArcWidth / 2f
            it.drawRect(mRectF, mPaint)

        }
    }

}