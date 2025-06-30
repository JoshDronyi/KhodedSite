package com.probro.khoded.styles.animations

import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.style.animation.Keyframes
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px


val makeInvisibleKeyFrames = Keyframes {
    from {
        Modifier.visibility(Visibility.Visible)
            .opacity(100)
    }
    to {
        Modifier.visibility(Visibility.Hidden)
            .opacity(0)
    }
}

val makeVisibleKeyFrames = Keyframes {
    from {
        Modifier.visibility(Visibility.Hidden)
            .opacity(0)
    }
    to {
        Modifier.visibility(Visibility.Visible)
            .opacity(100)
    }
}

val shiftForwardKeyFrames = Keyframes {
    from {
        Modifier.zIndex(0)
    }
    to {
        Modifier.zIndex(2)
    }
}

val shiftBackwardKeyframes = Keyframes {
    from {
        Modifier.zIndex(2)
    }
    to {
        Modifier.zIndex(0)
    }
}

val jobPostingShiftDownKeyFrames = Keyframes {
    from {
        Modifier.translateY(ty = 0.px)
            .animation(makeInvisibleKeyFrames.toAnimation(300.ms))
    }
    to {
        Modifier.translateY(ty = (-100).px)
            .animation(
                makeVisibleKeyFrames.toAnimation(300.ms)
            )
    }
}

val jobPostingShiftUPKeyFrames = Keyframes {
    from {
        Modifier.translateY(ty = (-100).px)
            .animation(makeVisibleKeyFrames.toAnimation(300.ms))
    }
    to {
        Modifier.translateY(ty = 0.px)
            .animation(
                makeInvisibleKeyFrames.toAnimation(300.ms)
            )
    }
}