package com.greensocks.exoplayerplayground

import android.animation.Animator
import android.view.View

fun View.fadeOut() {
    animate()
        .alpha(0f)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                visibility = View.GONE
            }
        })
        .start()
}

fun View.resetVisible() {
    visibility = View.VISIBLE
    alpha = 1f
}
