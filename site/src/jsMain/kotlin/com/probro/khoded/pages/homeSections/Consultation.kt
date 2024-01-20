package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.PinkButtonVariant
import com.probro.khoded.components.composables.BackingCard
import com.probro.khoded.components.composables.ImageBox
import com.probro.khoded.components.composables.SingleBorderBackingCardVaiant
import com.probro.khoded.styles.*
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text


@Composable
fun ConsultationSectionDisplay(
    footer: @Composable () -> Unit,
    data: Pages.Home_Section.Consultation
) = with(data) {
    Column(
        modifier = BackgroundStyle.toModifier(ConsultationBackgroundVariant)
            .id(id),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        BackingCard(
            modifier = Modifier
                .zIndex(2),
            variant = SingleBorderBackingCardVaiant,
            firstSection = { Text(data.mainText) },
            secondSection = { Text(data.subText) }
        )
        footer()
    }
}