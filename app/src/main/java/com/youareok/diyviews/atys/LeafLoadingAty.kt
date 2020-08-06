package com.youareok.diyviews.atys

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.youareok.diyviews.R
import com.youareok.diyviews.atys.LeafLoadingAty.Companion.TAG
import kotlinx.android.synthetic.main.activity_leaf_loading.*
import kotlin.math.sin
import kotlin.random.Random

class LeafLoadingAty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaf_loading)
        supportActionBar?.title = TAG

        sbLoadingProcess.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                llvShow.progress(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
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
    private var mArcRectWidth = 0f
    private var mInnerArcRectWidth = 0f
    private var mOuterStrokeWidth = 10f
    private var mProgress = 0
    private var mMiddleAmplitude = Leaf.MIDDLE_AMPLITUDE


    private var mLeafBmp: Bitmap
    private var mLeafWidth: Int = 0
    private var mLeafHeight: Int = 0
    private val mFanBmp: Bitmap

    private val mLeafs = mutableListOf<Leaf>()

    init {
        mLeafBmp = BitmapFactory.decodeResource(resources, R.drawable.leaf)
        mLeafWidth = mLeafBmp.width
        mLeafHeight = mLeafBmp.height
        mLeafs.addAll(Leaf.generateLeafs())

        mFanBmp = BitmapFactory.decodeResource(resources, R.drawable.fengshan)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mPaint.isAntiAlias = true
        mArcRectWidth = height - 10f
        mInnerArcRectWidth = mArcRectWidth - mOuterStrokeWidth * 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)
            drawOuterShape(it)
            drawInnerShape(it)

            val innerLeft = -width / 2f + mOuterStrokeWidth
            val innerRight = width / 2f - mArcRectWidth / 2 - mOuterStrokeWidth
            val innerWidth = innerRight - innerLeft
            drawLeafs(it, mLeafs, innerWidth.toInt())

            drawFan(it)
        }
        postDelayed({
            invalidate()
        }, 10L)
    }

    private fun drawOuterShape(it: Canvas) {
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.YELLOW

        mRectF.left = -width / 2f
        mRectF.top = -height / 2f
        mRectF.right = -width / 2f + mArcRectWidth
        mRectF.bottom = height / 2f
        it.drawArc(mRectF, 90f, 180f, false, mPaint)

        mRectF.left = -width / 2f + mArcRectWidth / 2f
        mRectF.right = width / 2f - mArcRectWidth / 2f
        it.drawRect(mRectF, mPaint)
    }

    private var mCurrentPorgressRight = 0F
    private fun drawInnerShape(it: Canvas) {
        mPaint.color = Color.parseColor("#ffffa800")

        val innerLeft = -width / 2f + mOuterStrokeWidth
        val innerRight = width / 2f - mArcRectWidth / 2 - mOuterStrokeWidth
        val innerWidth = innerRight - innerLeft

        val innerArcRight = innerLeft + mInnerArcRectWidth
        mCurrentPorgressRight = innerLeft + mProgress * innerWidth / 100f

        val innerArcProgress = mInnerArcRectWidth / 2f / innerWidth * 100
        val startAngle =
            if (mProgress > innerArcProgress)
                90f
            else
                180 - 90 * (mProgress * 1.0f / innerArcProgress)
        val sweepAngle = 2 * (180 - startAngle)
        //draw left arc for inner
        mRectF.left = innerLeft
        mRectF.top = mRectF.top + mOuterStrokeWidth
        mRectF.bottom = mRectF.bottom - mOuterStrokeWidth
        mRectF.right = innerArcRight
        it.drawArc(mRectF, startAngle, sweepAngle, false, mPaint)

        //draw middle rect for inner
        mRectF.left = -width / 2f + mOuterStrokeWidth + mInnerArcRectWidth / 2f
        mRectF.right = mCurrentPorgressRight
        if (mRectF.width() > 0) {
            it.drawRect(mRectF, mPaint)
        }
    }

    private fun drawLeafs(it: Canvas, leafs: MutableList<Leaf>, progressWidth: Int) {
        val currentTime = System.currentTimeMillis()
        val duration = 2_000L
        val rotationDuration = 1_000L
        for (leaf in leafs) {
            if (leaf.createTime in 1 until currentTime) {
                val cost = currentTime - leaf.createTime
                leaf.x =
                    (width / 2f - mArcRectWidth / 2 - (cost) * 1.0f * progressWidth / duration).toInt()
                leaf.y = getLocationY(leaf, progressWidth, height / 2f - mOuterStrokeWidth)

                if (leaf.x < mCurrentPorgressRight) {
                    mProgress++
                    leaf.createTime = 0
                    continue
                }

                val matrix = Matrix()
                matrix.postTranslate(leaf.x.toFloat(), leaf.y.toFloat())
                val angle = 360f * (currentTime - leaf.createTime) / rotationDuration
                matrix.postRotate(angle, leaf.x + mLeafWidth / 2f, leaf.y + mLeafHeight / 2f)

                it.save()
                it.drawBitmap(mLeafBmp, matrix, mPaint)
                it.restore()
            }
        }
        if (currentTime - leafs[0].createTime >= duration) {
            leafs.clear()
            leafs.addAll(Leaf.generateLeafs())
        }
    }

    private val startTime = System.currentTimeMillis()

    private fun drawFan(it: Canvas) {
        val radius = height / 2f

        val style = mPaint.style
        val strokeWidth = mPaint.strokeWidth

        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mOuterStrokeWidth
        mPaint.color = Color.WHITE

        it.drawCircle(width / 2f - radius, 0f, radius, mPaint)

        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.YELLOW
        it.drawCircle(width / 2f - radius, 0f, radius, mPaint)

        mPaint.style = style
        mPaint.strokeWidth = strokeWidth

        val fanMargin = 10

        val currentTime = System.currentTimeMillis()
        val cost = currentTime - startTime
        val angle = cost / 2000F * 360f
        it.save()
        it.rotate(-angle, width / 2f - radius, 0f)
        it.drawBitmap(
            mFanBmp, Rect(0, 0, mFanBmp.width, mFanBmp.height),
            Rect(
                (width / 2f - radius * 2 + fanMargin).toInt(),
                (-height / 2f + fanMargin).toInt(),
                (width / 2f - fanMargin).toInt(),
                (height / 2f - fanMargin).toInt()
            ), mPaint
        )
        it.restore()
    }

    private fun getLocationY(leaf: Leaf, progressWidth: Int, arcRadius: Float): Int {
        //y = A sin(wx+Q) + h
        val w = 2f * Math.PI / progressWidth
        val a = when (leaf.type) {
            StartType.LITTLE -> mMiddleAmplitude - 35
            StartType.BIG -> mMiddleAmplitude + 35
            else -> mMiddleAmplitude
        }
        return (a * sin(w * leaf.x)).toInt()
    }

    fun progress(progress: Int) {
        if (progress != mProgress) {
            mProgress = progress
            invalidate()
        }
        Log.d(TAG, "progress, $progress")
    }

    data class Leaf(
        var createTime: Long = System.currentTimeMillis(),
        val type: StartType = StartType.MIDDLE,
        var rotation: Int = 0,
        val rotationDirection: Int = 1,     //1 means clockwise   0 means anti-clockwise
        var x: Int = 0,
        var y: Int = 0
    ) {

        companion object {
            private const val MAX_LEAFS = 6
            const val MIDDLE_AMPLITUDE = 15

            fun generateLeaf(): Leaf {
                return Leaf(
                    type = when (Random.Default.nextInt(3)) {
                        0 -> StartType.LITTLE
                        1 -> StartType.MIDDLE
                        else -> StartType.BIG
                    },
                    rotation = Random.Default.nextInt(360),
                    rotationDirection = Random.Default.nextInt(2),
                    createTime = System.currentTimeMillis() + Random.Default.nextLong(2000)
                )
            }

            fun generateLeafs(size: Int = MAX_LEAFS): List<Leaf> {
                return MutableList(size) {
                    generateLeaf()
                }
            }
        }
    }

    enum class StartType {
        LITTLE, MIDDLE, BIG
    }


}