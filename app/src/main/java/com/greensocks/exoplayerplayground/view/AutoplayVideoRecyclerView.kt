package com.greensocks.exoplayerplayground.view

import android.view.View

interface AutoplayVideoRecyclerView {
    /** Check if the recyclerView can scroll vertically in the given direction **/
    fun canScrollVertically(direction: Int): Boolean

    /** Should return the AutoplayVideoView  specific to the given adapter position,
     * or null if it isn't an AutoplayVideoView**/
    fun findViewByAdapterPosition(position: Int): View?

    /**Should return the adapter position of the first visible AutoplayVideoView,
     * or RecyclerView.NO_POSITION if it's none are visible **/
    fun findFirstCompletelyVisibleItemPosition(): Int

    /**Should return the adapter position of the last visible AutoplayVideoView,
     * or RecyclerView.NO_POSITION if it's none are visible **/
    fun findLastCompletelyVisibleItemPosition(): Int

    /**Checks whether at the given adapter position,there is a Video item**/
    fun isVideoItem(position: Int): Boolean

}
