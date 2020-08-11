package com.youareok.diyviews.atys

import HeartView
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.youareok.diyviews.views.*

class ContainerAty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SelectView(this))
        supportActionBar?.title = TAG
    }

    override fun onBackPressed() {
        if (supportActionBar?.title != TAG) {
            setContentView(SelectView(this))
            supportActionBar?.title = TAG
            return
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val TAG = "ContainerAty"
    }
}


class SelectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private val views = listOf<Pair<String, (Context) -> View>>(
        Pair("BezierView") { it ->
            BezierView(context)
        },
        Pair("HeartView") { it ->
            HeartView(it)
        },
        Pair("WaterDrop") { it ->
            WaterDropView(context)
        },
        Pair("MagicCircle") { it ->
            MagicCircle(context)
        },
        Pair("YinYangFish") { it ->
            YinYangFish(context)
        },
        Pair("PathPlay") { it ->
            PathPlay(context)
        },
        Pair("SearchLoading") { it ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SearchLoading(context)
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
        }
    )

    init {
        val container = LinearLayout(context).apply {
            layoutParams = LayoutParams(-1, -1)
            orientation = LinearLayout.VERTICAL
        }
        layoutParams = container.layoutParams
        addView(container)

        for (pair in views) {
            container.addView(TextView(context).apply {
                textSize = 13 * resources.displayMetrics.density
                text = pair.first
                layoutParams = LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                }

                setBackgroundColor(Color.GRAY)
                setOnClickListener {
                    if (context is AppCompatActivity) {
                        context.setContentView(pair.second(context).apply {
                            layoutParams = LayoutParams(-1, -1)
                        })
                        context.supportActionBar?.title = pair.first
                    }
                }
            })

            container.addView(View(context).apply {
                layoutParams = LayoutParams(-1, 15)
            })
        }
    }

}