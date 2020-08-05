package com.youareok.diyviews

import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youareok.diyviews.atys.BasicGraphicsAty
import com.youareok.diyviews.atys.CanvasConvertAty
import com.youareok.diyviews.atys.LeafLoadingAty
import com.youareok.diyviews.atys.PictureTextAty
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvViews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = DiyViewAdapter()
        }
    }
}


class DiyViewAdapter : RecyclerView.Adapter<DiyViewViewHolder>() {
    private val list = listOf(
        Pair(BasicGraphicsAty.TAG, BasicGraphicsAty::class.java),
        Pair(CanvasConvertAty.TAG, CanvasConvertAty::class.java),
        Pair(PictureTextAty.TAG, PictureTextAty::class.java),
        Pair(LeafLoadingAty.TAG, LeafLoadingAty::class.java)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiyViewViewHolder {
        return DiyViewViewHolder(Button(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DiyViewViewHolder, position: Int) {
        holder.bindData(list[position])
    }

}

class DiyViewViewHolder(val button: Button) : RecyclerView.ViewHolder(button) {
    fun bindData(pair: Pair<String, Class<out Activity>>) {
        button.apply {
            setText(pair.first)
            setOnClickListener {
                it.context.startActivity(Intent(it.context, pair.second))
            }
        }
    }

}