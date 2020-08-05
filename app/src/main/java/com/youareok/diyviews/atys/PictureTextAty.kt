package com.youareok.diyviews.atys

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.youareok.diyviews.R
import com.youareok.diyviews.atys.PictureTextAty.Companion.TAG

class PictureTextAty : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = TAG
        setContentView(
            PictureTextView(this).apply {
                layoutParams = ViewGroup.LayoutParams(-1, -1)
            }
        )
    }

    companion object {
        const val TAG = "PictureTextAty"
    }
}

class PictureTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val MAX_PAGE = 13
    private val DURATION = 100L
    private val mBitmap: Bitmap
    private val mPaint = Paint()
    private var mCurrentPageIndex = 0
    private val mHandler: Handler

    init {
        mBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.checkmark)

        mHandler = @SuppressLint("HandlerLeak") object : Handler() {

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                mCurrentPageIndex = (mCurrentPageIndex + 1) % MAX_PAGE
                invalidate()

                this.sendEmptyMessageDelayed(0, DURATION)
                Log.d(TAG, "handleMessage, mCurrentPageIndex:$mCurrentPageIndex")
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mHandler.sendEmptyMessage(0)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeCallbacksAndMessages(null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)

            mPaint.color = Color.YELLOW
            it.drawCircle(0f, 0f, 300f, mPaint)

            val height = mBitmap.height
            it.drawBitmap(
                mBitmap,
                Rect(height * mCurrentPageIndex, 0, height * (mCurrentPageIndex + 1), height),
                Rect(-height / 2, -height / 2, height / 2, height / 2),
                mPaint
            )
        }

    }

}