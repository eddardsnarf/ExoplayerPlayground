package com.greensocks.exoplayerplayground.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.IntDef
import com.greensocks.exoplayerplayground.R
import java.lang.annotation.Documented

/** A [FrameLayout] that resizes itself to match a specified aspect ratio.  */
open class AspectRatioFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? =  /* attrs= */null
) : FrameLayout(context, attrs) {
    /** Listener to be notified about changes of the aspect ratios of this view.  */
    interface AspectRatioListener {
        /**
         * Called when either the target aspect ratio or the view aspect ratio is updated.
         *
         * @param targetAspectRatio The aspect ratio that has been set in [.setAspectRatio]
         * @param naturalAspectRatio The natural aspect ratio of this view (before its width and height
         * are modified to satisfy the target aspect ratio).
         * @param aspectRatioMismatch Whether the target and natural aspect ratios differ enough for
         * changing the resize mode to have an effect.
         */
        fun onAspectRatioUpdated(
            targetAspectRatio: Float, naturalAspectRatio: Float, aspectRatioMismatch: Boolean
        )
    }

    /**
     * Resize modes for [AspectRatioFrameLayout]. One of [.RESIZE_MODE_FIT], [ ][.RESIZE_MODE_FIXED_WIDTH], [.RESIZE_MODE_FIXED_HEIGHT], [.RESIZE_MODE_FILL] or
     * [.RESIZE_MODE_ZOOM].
     */
    @Documented
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        RESIZE_MODE_FIT,
        RESIZE_MODE_FIXED_WIDTH,
        RESIZE_MODE_FIXED_HEIGHT,
        RESIZE_MODE_FILL,
        RESIZE_MODE_ZOOM
    )
    annotation class ResizeMode

    private val aspectRatioUpdateDispatcher: AspectRatioUpdateDispatcher
    private var aspectRatioListener: AspectRatioListener? = null
    private var aspectRatio = 0f

    @ResizeMode
    private var resizeMode: Int

    /**
     * Sets the aspect ratio that this view should satisfy.
     *
     * @param widthHeightRatio The width to height ratio.
     */
    fun setAspectRatio(widthHeightRatio: Float) {
        if (aspectRatio != widthHeightRatio) {
            aspectRatio = widthHeightRatio
            requestLayout()
        }
    }

    /**
     * Sets the [AspectRatioListener].
     *
     * @param listener The listener to be notified about aspect ratios changes, or null to clear a
     * listener that was previously set.
     */
    fun setAspectRatioListener(listener: AspectRatioListener?) {
        aspectRatioListener = listener
    }

    /** Returns the [ResizeMode].  */
    @ResizeMode
    fun getResizeMode(): Int {
        return resizeMode
    }

    /**
     * Sets the [ResizeMode]
     *
     * @param resizeMode The [ResizeMode].
     */
    fun setResizeMode(@ResizeMode resizeMode: Int) {
        if (this.resizeMode != resizeMode) {
            this.resizeMode = resizeMode
            requestLayout()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (aspectRatio <= 0) {
            // Aspect ratio not set.
            return
        }
        var width = measuredWidth
        var height = measuredHeight
        val viewAspectRatio = width.toFloat() / height
        val aspectDeformation = aspectRatio / viewAspectRatio - 1
        if (Math.abs(aspectDeformation) <= MAX_ASPECT_RATIO_DEFORMATION_FRACTION) {
            // We're within the allowed tolerance.
            aspectRatioUpdateDispatcher.scheduleUpdate(aspectRatio, viewAspectRatio, false)
            return
        }
        when (resizeMode) {
            RESIZE_MODE_FIXED_WIDTH -> height = (width / aspectRatio).toInt()
            RESIZE_MODE_FIXED_HEIGHT -> width = (height * aspectRatio).toInt()
            RESIZE_MODE_ZOOM -> if (aspectDeformation > 0) {
                width = (height * aspectRatio).toInt()
            } else {
                height = (width / aspectRatio).toInt()
            }
            RESIZE_MODE_FIT -> if (aspectDeformation > 0) {
                height = (width / aspectRatio).toInt()
            } else {
                width = (height * aspectRatio).toInt()
            }
            RESIZE_MODE_FILL -> {
            }
            else -> {
            }
        }
        aspectRatioUpdateDispatcher.scheduleUpdate(aspectRatio, viewAspectRatio, true)
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    /** Dispatches updates to [AspectRatioListener].  */
    private inner class AspectRatioUpdateDispatcher : Runnable {
        private var targetAspectRatio = 0f
        private var naturalAspectRatio = 0f
        private var aspectRatioMismatch = false
        private var isScheduled = false
        fun scheduleUpdate(
            targetAspectRatio: Float, naturalAspectRatio: Float, aspectRatioMismatch: Boolean
        ) {
            this.targetAspectRatio = targetAspectRatio
            this.naturalAspectRatio = naturalAspectRatio
            this.aspectRatioMismatch = aspectRatioMismatch
            if (!isScheduled) {
                isScheduled = true
                post(this)
            }
        }

        override fun run() {
            isScheduled = false
            if (aspectRatioListener == null) {
                return
            }
            aspectRatioListener!!.onAspectRatioUpdated(
                targetAspectRatio, naturalAspectRatio, aspectRatioMismatch
            )
        }
    }

    companion object {
        /** Either the width or height is decreased to obtain the desired aspect ratio.  */
        const val RESIZE_MODE_FIT = 0

        /**
         * The width is fixed and the height is increased or decreased to obtain the desired aspect ratio.
         */
        const val RESIZE_MODE_FIXED_WIDTH = 1

        /**
         * The height is fixed and the width is increased or decreased to obtain the desired aspect ratio.
         */
        const val RESIZE_MODE_FIXED_HEIGHT = 2

        /** The specified aspect ratio is ignored.  */
        const val RESIZE_MODE_FILL = 3

        /** Either the width or height is increased to obtain the desired aspect ratio.  */
        const val RESIZE_MODE_ZOOM = 4

        /**
         * The [FrameLayout] will not resize itself if the fractional difference between its natural
         * aspect ratio and the requested aspect ratio falls below this threshold.
         *
         *
         * This tolerance allows the view to occupy the whole of the screen when the requested aspect
         * ratio is very close, but not exactly equal to, the aspect ratio of the screen. This may reduce
         * the number of view layers that need to be composited by the underlying system, which can help
         * to reduce power consumption.
         */
        private const val MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f
    }

    init {
        resizeMode = RESIZE_MODE_FIT
        if (attrs != null) {
            val a = context
                .theme
                .obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout, 0, 0)
            try {
                resizeMode =
                    a.getInt(
                        R.styleable.AspectRatioFrameLayout_aspect_ratio_mode,
                        RESIZE_MODE_FIT
                    )
                aspectRatio =
                    a.getFloat(R.styleable.AspectRatioFrameLayout_aspect_ratio, .5F)
            } finally {
                a.recycle()
            }
        }
        aspectRatioUpdateDispatcher = AspectRatioUpdateDispatcher()
    }
}
