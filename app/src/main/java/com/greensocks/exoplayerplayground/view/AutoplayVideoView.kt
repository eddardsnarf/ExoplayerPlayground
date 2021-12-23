package com.greensocks.exoplayerplayground.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.common.cache.Cache
import com.greensocks.exoplayerplayground.R
import com.greensocks.exoplayerplayground.VolumeController
import com.greensocks.exoplayerplayground.databinding.ViewAutoplayVideoBinding
import com.greensocks.exoplayerplayground.fadeOut
import com.greensocks.exoplayerplayground.resetVisible
import kotlin.properties.Delegates


class AutoplayVideoView : AspectRatioFrameLayout, Player.Listener {
    private lateinit var cacheDataSourceFactory: CacheDataSource.Factory
    private val TAG = "AutoplayVideoView"
    private var binding: ViewAutoplayVideoBinding by Delegates.notNull()
    var player: ExoPlayer? = null
    private var volumeController: VolumeController
    private var thumbnailView: ImageView
    private var volumeControlView: ImageView
    var currentMediaItem: MediaItem? = null
    var currentMediaSource: MediaSource? = null
    var scheduledResumeOnNextMediaItem: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val inflater = LayoutInflater.from(context)
        binding = ViewAutoplayVideoBinding.inflate(inflater, this)
        setupDefaultLayoutParams(context, attrs)

        //sadly we cannot use view-binding on the StyleExoPlayerView layout
        //so we need to use findViewById().
        with(binding.playerView) {
            thumbnailView = findViewById(R.id.exo_thumbnail)
            volumeControlView = findViewById(R.id.exo_volume_control)
        }
        volumeController = VolumeController(volumeControlView)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                Log.e(TAG, "onPlayerStateChanged: Buffering video.")
            }
            Player.STATE_ENDED -> {
                Log.d(TAG, "onPlayerStateChanged: Video ended.")
            }
            Player.STATE_IDLE -> {
                Log.d(TAG, "onPlayerStateChanged: Video IDLE.")
            }
            Player.STATE_READY -> {
                Log.e(TAG, "onPlayerStateChanged: Ready to play.")
                thumbnailView.fadeOut()
            }
            else -> {
                Log.e(TAG, "onPlayerStateChanged: else?!.")
            }
        }
    }

    fun attachPlayer(exoplayer: ExoPlayer) {
        player = exoplayer
        player?.let {
            binding.playerView.player = it
            it.addListener(this)
            setupVolumeController(it, binding.root)
        }
    }

    // ExoPlayer and PlayerView hold circular refs to each other, so avoid leaking
    // Activity here by nulling it out.
    fun detachPlayer() {
        player?.removeListener(this)
        binding.playerView.player = null
        player = null
    }

    fun setAudioEnabled(isEnabled: Boolean) {
        volumeController.setEnabled(isEnabled)
    }

    fun setThumbnail(thumbnailUrl: String) {
        Log.i(TAG, "setThumbnail($thumbnailUrl)")
        resetThumbnail()
        loadThumbnail(thumbnailView, thumbnailUrl)
    }

    fun resetThumbnail() {
        thumbnailView.resetVisible()
    }

    fun setMediaItem(mediaItem: MediaItem) {
        Log.i(TAG, "setVideoUrl(${mediaItem})")
        this.currentMediaItem = mediaItem
        this.currentMediaSource = mediaItem.let {
            ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(mediaItem)
        }
    }

    fun setCacheDataSourceFactory(dataSourceFactory: CacheDataSource.Factory) {
        this.cacheDataSourceFactory = dataSourceFactory
    }

    fun prepareCurrentPlayer() {
        preparePlayerMediaItem(player)
    }

    fun resumePlayer() {
        Log.i(TAG, "resumePlayer($player)")
        player?.apply {
            if (!isPlaying) {
                play()
            }
        }
    }

    fun pausePlayer() {
        Log.i(TAG, "pausePlayer($player)")
        player?.apply {
            if (isPlaying) {
                pause()
            }
        }
    }

    fun playVideoIfScheduled() {
        if (scheduledResumeOnNextMediaItem) {
            prepareCurrentPlayer()
            resumePlayer()
            scheduledResumeOnNextMediaItem = false
        }
    }

    private fun setupDefaultLayoutParams(
        context: Context,
        attrs: AttributeSet?
    ) {
        if (this.layoutParams == null) {
            val params: ViewGroup.LayoutParams = LayoutParams(context, attrs)
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            this.layoutParams = params
        }
    }

    private fun setupVolumeController(
        player: ExoPlayer,
        clickableView: View
    ) {
        volumeController.setPlayer(player)
        clickableView.setOnClickListener {
            volumeController.showVolumeControl()
        }
    }

    private fun loadThumbnail(thumbnailView: ImageView, thumbnailUri: String?) {
        Glide
            .with(context)
            .load(thumbnailUri)
            .centerCrop()
            .into(thumbnailView);
    }

    private fun preparePlayerMediaItem(player: ExoPlayer?) {
        if (player == null) {
            Log.e(TAG, "player == null !")
            return
        }
        player.repeatMode = Player.REPEAT_MODE_ALL
        currentMediaSource?.let { player.setMediaSource(it) }
        player.prepare()
    }


}
