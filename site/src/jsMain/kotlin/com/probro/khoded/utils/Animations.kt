package com.probro.khoded.utils

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.silk.components.animation.Keyframes
import org.jetbrains.compose.web.css.px

val fallInAnimation by Keyframes {
    from {
        Modifier
            .top((-100).px)
//            .opacity(0.percent)
    }
    to {
        Modifier
            .top(0.px)
//            .opacity(100.percent)
    }
}
val flyUpAnimation by Keyframes {
    from {
        Modifier
            .top(0.px)
//            .opacity(100.percent)
    }
    to {
        Modifier
            .top((-100).px)
//            .opacity(0.percent)
    }
}