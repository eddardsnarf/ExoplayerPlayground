package com.greensocks.exoplayerplayground.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.greensocks.exoplayerplayground.model.VideoModel
import com.greensocks.exoplayerplayground.view.AutoplayVideoView

class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val autoplayVideoView = view as AutoplayVideoView

    fun bind(model: VideoModel) {
        autoplayVideoView.apply {
            setThumbnail(model.thumbnailUrl)
            autoplayVideoView.setMediaItem(model.mediaItem)
            playVideoIfScheduled()
            setAudioEnabled(true)
        }

    }
}
