package com.youareok.diyviews.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class SearchLoading @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mState = State.INITIAL
    private val mCirclePath = Path()
    private val mSearchPath = Path()
    private val mPaint = Paint()
    private val mPathMeasure = PathMeasure()

    private var mSearchCount = 0
    private var mAnimatorValue = 1f
    private val mValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            mAnimatorValue = it.animatedValue as Float
            invalidate()
        }
    }

    init {
        val innerRadius = 60f
        val outterRadius = 150f

        mCirclePath.addArc(-outterRadius, -outterRadius, outterRadius, outterRadius, 45f, -359f)
        mSearchPath.addArc(-innerRadius, -innerRadius, innerRadius, innerRadius, 45f, 359f)
        val angle = 45f / 180 * Math.PI
        mSearchPath.lineTo(
            (Math.sin(angle) * outterRadius).toFloat(),
            (Math.cos(angle) * outterRadius).toFloat()
        )

        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2f * resources.displayMetrics.density
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(width / 2f, height / 2f)

            when (mState) {
                State.INITIAL -> {
                    it.drawPath(mSearchPath, mPaint)

                    startPrepareAnimation()
                }
                State.PREPARE -> {
                    mPathMeasure.setPath(mSearchPath, false)
                    val lnt = mPathMeasure.length
                    val show = (1 - mAnimatorValue) * lnt
                    val path = Path()
                    mPathMeasure.getSegment(lnt - show, lnt, path, true)
                    it.drawPath(path, mPaint)
                }
                State.SEARCHING -> {
                    mPathMeasure.setPath(mCirclePath, false)
                    val lnt = mPathMeasure.length
                    val show = Math.sin(mAnimatorValue * Math.PI) * lnt * 2f / 10f
                    val path = Path()
                    mPathMeasure.getSegment(
                        mAnimatorValue * lnt,
                        mAnimatorValue * lnt + show.toFloat(),
                        path,
                        true
                    )
                    it.drawPath(path, mPaint)
                }
                State.END -> {
                    mPathMeasure.setPath(mSearchPath, false)
                    val lnt = mPathMeasure.length
                    val show = (mAnimatorValue) * lnt
                    val path = Path()
                    mPathMeasure.getSegment(lnt - show, lnt, path, true)
                    it.drawPath(path, mPaint)
                }
            }
        }
    }

    fun startPrepareAnimation() {
        mState = State.PREPARE

        if (mValueAnimator.isRunning) {
            mValueAnimator.cancel()
        }
        mValueAnimator.duration = 2000
        mValueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mValueAnimator.removeListener(this)
                mSearchCount = 0
                startSearchingAnim()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
        mValueAnimator.start()
    }


    private fun startSearchingAnim() {
        mState = State.SEARCHING
        mSearchCount++
        mValueAnimator.interpolator = AccelerateInterpolator()
        mValueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mValueAnimator.removeListener(this)
                mValueAnimator.interpolator = LinearInterpolator()

                if (mSearchCount >= 2) {
                    startSearchEndAnim()
                } else {
                    startSearchingAnim()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        mValueAnimator.start()
    }

    private fun startSearchEndAnim() {
        mState = State.END

        if (mValueAnimator.isRunning) {
            mValueAnimator.cancel()
        }
        mValueAnimator.duration = 2000
        mValueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mValueAnimator.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
        mValueAnimator.start()
    }


    enum class State {
        INITIAL, PREPARE, SEARCHING, END
    }
}