package com.greensocks.exoplayerplayground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.greensocks.exoplayerplayground.R
import com.greensocks.exoplayerplayground.model.TextModel
import com.greensocks.exoplayerplayground.model.VideoModel
import com.greensocks.exoplayerplayground.view.AutoplayVideoView

class MainAdapter(val cacheDataSourceFactory: CacheDataSource.Factory) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIDEO: Int = 0
    private val TEXT: Int = 1
    var items: ArrayList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIDEO -> {
                VideoViewHolder(
                    (LayoutInflater.from(parent.context)
                        .inflate(
                            R.layout.item_video_recycler_view,
                            parent,
                            false
                        ) as AutoplayVideoView).apply {
                        setCacheDataSourceFactory(cacheDataSourceFactory)
                    }
                )
            }
            else -> {
                TextViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_text_recycler_view, parent, false)
                )
            }
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoViewHolder -> {
                holder.bind(items[position] as VideoModel)
            }
            is TextViewHolder -> {
                holder.bind(items[position] as TextModel)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is VideoModel -> VIDEO
        else -> TEXT
    }

    fun updateItemList(items: ArrayList<Any>) {
        this.items = items
        notifyDataSetChanged()
    }
}
