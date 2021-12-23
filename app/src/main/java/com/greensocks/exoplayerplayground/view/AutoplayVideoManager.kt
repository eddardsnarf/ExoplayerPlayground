package com.greensocks.exoplayerplayground.view

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

import android.util.Log

import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.google.android.exoplayer2.ExoPlayer


class AutoplayVideoManager(
    private var rv: AutoplayVideoRecyclerView?,
    private val player: ExoPlayer
) : RecyclerView.OnScrollListener(),
    OnVideoChildAttachChangeListener{
    private val TAG = "AutoplayVideoRecyclerV"

    private var playedOnFirstLayout: Boolean = false
    private var playPosition = -1
    private var currentPlayingView: AutoplayVideoView? = null


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == SCROLL_STATE_IDLE) {
            // There's a special case when the end of the list has been reached.
            // Need to handle that with this bit of logic
            playVideo(!rv!!.canScrollVertically(1))
            Log.d(TAG, "onScrollStateChanged: idle.")
        }
    }

    override fun onChildViewAttachedToWindow(view: AutoplayVideoView) {
        Log.d(TAG,"onChildViewAttachedToWindow: $view")
            if (!playedOnFirstLayout) {
                Log.e("R$TAG", "onChildViewAttachedToWindow: $playPosition. careful.")
                playVideo(!rv!!.canScrollVertically(1))
            }
        }

    override fun onChildViewDetachedFromWindow(view: AutoplayVideoView) {}

    fun resumeCurrentVideo() {
        if (playPosition > -1) {
            Log.w(TAG, "resumeCurrentVideo: $playPosition view: $currentPlayingView")
            currentPlayingView?.resumePlayer()
        }
    }

    fun pauseCurrentVideo() {
        Log.w(
            TAG,
            "pauseCurrentVideo: $playPosition view: $currentPlayingView player: ${currentPlayingView?.player}"
        )
        currentPlayingView?.pausePlayer()
    }

    fun releaseAllPlayers() {
        Log.w(TAG, "releaseAllPlayers: $player")
        player.release()
    }

    fun destroy() {
        Log.w(TAG, "destroy() : rv going null")
        rv = null
    }


    private fun AutoplayVideoView.unplugPlayer() {
        Log.i(TAG, "pause and detach CurrentVideo($this)")
        pausePlayer()
        resetThumbnail()
        detachPlayer()
    }


    private fun playVideo(isEndOfList: Boolean) {
        val targetPosition = getPlayTargetPosition(isEndOfList)
        Log.d(TAG, "playVideo: found target position: $targetPosition")
        // video is already playing so return
        if (targetPosition == playPosition) {
            return
        }

        currentPlayingView?.unplugPlayer()

        // we are getting an invalid target position to play
        // so at least unplug the player and then return
        if (targetPosition < 0) {
            return
        }

        // set the position of the list-item that is to be played
        playPosition = targetPosition
        playedOnFirstLayout = true
        Log.d(TAG, "playVideo: gonna play target position: $playPosition")

        currentPlayingView =
            rv?.findViewByAdapterPosition(targetPosition)?.let { videoView ->
                videoView as AutoplayVideoView

                videoView.attachPlayer(player)
                if (videoView.currentMediaItem != null) {
                    videoView.prepareCurrentPlayer()
                    videoView.resumePlayer()
                } else {
                    videoView.scheduledResumeOnNextMediaItem = true
                }

                videoView
            }
    }


    private fun getPlayTargetPosition(isEndOfList: Boolean): Int {
        val targetPosition = if (!isEndOfList) {
            findFirstCompletelyVisibleVideoItemPosition()
        } else {
            findLastCompletelyVisibleVideoItemPosition()
        }
        return targetPosition
    }

    private fun findFirstCompletelyVisibleVideoItemPosition(): Int {
        rv?.let { rv ->
            val firstItemPos =
                rv.findFirstCompletelyVisibleItemPosition()
            val lastItemPos =
                rv.findLastCompletelyVisibleItemPosition()
            //if we are not getting any completely visible items (user flings like crazy)
            //then just return an invalid target position
            if (firstItemPos < 0 || lastItemPos < 0) {
                return RecyclerView.NO_POSITION
            }
            for (i in firstItemPos..lastItemPos) {
                if (rv.isVideoItem(i)
                    && isVideoViewCompletelyVisible(i)) {
                        return i
                    }
                }
            }
        return RecyclerView.NO_POSITION
    }

    private fun findLastCompletelyVisibleVideoItemPosition(): Int {
        var result = RecyclerView.NO_POSITION
        rv?.let { rv ->
            val firstItemPos =
                rv.findFirstCompletelyVisibleItemPosition()
            val lastItemPos =
                rv.findLastCompletelyVisibleItemPosition()
            //if we are not getting any completely visible items (user flings like crazy)
            //then just return an invalid target position
            if (firstItemPos < 0 || lastItemPos < 0) {
                return RecyclerView.NO_POSITION
            }
            for (i in firstItemPos..lastItemPos) {
                if (rv.isVideoItem(i)
                    && isVideoViewCompletelyVisible(i)) {
                        result = i
                    }
                }
            }
        return result
    }

    private fun isVideoViewCompletelyVisible(position: Int): Boolean {
        // this is done in order so that the video-view only is checked for complete-visibility
        return rv?.findViewByAdapterPosition(position)
            ?.run {
                val rect = Rect()
                getGlobalVisibleRect(rect)
                        && measuredHeight == rect.height()
                        && measuredWidth == rect.width()
            } ?: false
    }
}
