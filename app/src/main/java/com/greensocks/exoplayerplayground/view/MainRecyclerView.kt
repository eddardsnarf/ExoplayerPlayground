package com.greensocks.exoplayerplayground.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greensocks.exoplayerplayground.adapter.MainAdapter
import com.greensocks.exoplayerplayground.adapter.VideoViewHolder
import com.greensocks.exoplayerplayground.model.VideoModel


class MainRecyclerView : RecyclerView, AutoplayVideoRecyclerView,
    RecyclerView.OnChildAttachStateChangeListener {
    private var playerManager: AutoplayVideoManager? = null
    private lateinit var mainAdapter: MainAdapter

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        mainAdapter = adapter as MainAdapter
    }

    override fun findViewByAdapterPosition(position: Int): View? =
        (layoutManager?.findViewByPosition(position))
    //in case you have a more conmplex video item use tag to hold the viewholder
    // and use it to access the video-view

    override fun findFirstCompletelyVisibleItemPosition(): Int =
        (layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
            ?: NO_POSITION

    override fun findLastCompletelyVisibleItemPosition(): Int =
        (layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
            ?: NO_POSITION

    override fun isVideoItem(position: Int): Boolean {
        mainAdapter.items.apply {
            return isNotEmpty()
                    && isVideoElement(position)
        }
    }

    override fun onChildViewAttachedToWindow(view: View) {
        if (view is AutoplayVideoView) {
            playerManager?.onChildViewAttachedToWindow(view)
        }
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        if (view is AutoplayVideoView) {
            playerManager?.onChildViewDetachedFromWindow(view)
        }
    }

    fun setupPlayerManager(playerManager: AutoplayVideoManager) {
        this.playerManager = playerManager

        addOnScrollListener(playerManager)
        addOnChildAttachStateChangeListener(this)
    }

    fun resumeCurrentVideo() {
        playerManager?.resumeCurrentVideo()
    }

    fun pauseCurrentVideo() {
        playerManager?.pauseCurrentVideo()
    }

    fun destroy() {
        playerManager?.let {
            it.releaseAllPlayers()
            removeOnScrollListener(it)
            removeOnChildAttachStateChangeListener(this)
            it.destroy()
        }
        playerManager = null
    }

    private fun List<Any>.isVideoElement(
        position: Int
    ) = get(position) is VideoModel
            && (get(position) as VideoModel).vidUrl.isNotBlank()
}
