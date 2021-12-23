package com.greensocks.exoplayerplayground.model

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

data class VideoModel(
    val thumbnailUrl: String,
    val vidUrl: String
) {
    val mediaItem: MediaItem = MediaItem.Builder()
        .setMediaId(vidUrl)
        .setUri(vidUrl)
        .build()
}

