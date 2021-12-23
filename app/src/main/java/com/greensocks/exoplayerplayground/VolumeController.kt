package com.greensocks.exoplayerplayground

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.exoplayer2.ExoPlayer

class VolumeController(
    private val volumeControlView: ImageView
) {
    private var isAudioEnabled: Boolean = true
    private val TAG = "VolumeController"
    private var volumeState: VolumeState = VolumeState.OFF
    private lateinit var videoPlayer: ExoPlayer

    init {
        volumeControlView.setOnClickListener {
            if (isAudioEnabled) {
            toggleVolume()
        }
    }
    }

    fun setPlayer(player: ExoPlayer) {
        this.videoPlayer = player
        initVolumeControl()
    }

    fun setEnabled(enabled: Boolean) {
        isAudioEnabled = enabled
        if (this::videoPlayer.isInitialized) {
            initVolumeControl()
        }
    }

    fun showVolumeControl() {
        volumeControlView.bringToFront()
        volumeControlView.animate().cancel()
        volumeControlView.alpha = 1f
        volumeControlView.animate()
            .alpha(0f)
            .setDuration(600)
            .startDelay = 3000
    }

    fun toggleVolume() {
        if (volumeState === VolumeState.OFF) {
            Log.d(TAG, "togglePlaybackState: enabling volume.")
            setVolumeControl(VolumeState.ON)
        } else if (volumeState === VolumeState.ON) {
            Log.d(TAG, "togglePlaybackState: disabling volume.")
            setVolumeControl(VolumeState.OFF)
        }
    }

    private fun initVolumeControl() {
        setVolumeControl(VolumeState.OFF)
        volumeControlView.visibility = if (isAudioEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setVolumeControl(state: VolumeState) {
        volumeState = state
        if (state === VolumeState.OFF) {
            videoPlayer.volume = 0f
            animateVolumeControl()
        } else if (state === VolumeState.ON) {
            videoPlayer.volume = 1f
            animateVolumeControl()
        }
    }

    private fun animateVolumeControl() {
        if (volumeState === VolumeState.OFF) {
            volumeControlView.setImageResource(R.drawable.ic_sound_off)
        } else if (volumeState === VolumeState.ON) {
            volumeControlView.setImageResource(R.drawable.ic_sound_on)
        }
        showVolumeControl()
    }
}

private enum class VolumeState {
    ON, OFF
}
