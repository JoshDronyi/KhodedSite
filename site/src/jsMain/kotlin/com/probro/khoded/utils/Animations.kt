package com.probro.khoded.utils

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.silk.components.animation.Keyframes
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val fallInAnimation by Keyframes {
    from {
        Modifier
            .opacity(0.percent)
            .top((-150).px)
    }
    to {
        Modifier
            .opacity(100.percent)
            .top(0.px)

    }
}
val flyUpAnimation by Keyframes {
    from {
        Modifier
            .opacity(100.percent)
            .top(0.px)
    }
    to {
        Modifier
            .opacity(0.percent)
            .top((-150).px)
    }
}