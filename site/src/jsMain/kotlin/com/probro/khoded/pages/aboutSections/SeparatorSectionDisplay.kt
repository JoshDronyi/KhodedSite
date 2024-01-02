package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.LogoDisplay
import com.probro.khoded.components.composables.SeparatorLogoVariant
import com.probro.khoded.models.Images
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.px

@Composable
fun SeparatorSectionDisplay(baseModifier: Modifier) = with(Pages.About_Section.Separator) {
    Row(
        modifier = baseModifier.fillMaxWidth()
            .height(Height.MinContent)
            .backgroundColor(Colors.Black)
            .padding(topBottom = 10.px)
    ) {
        repeat(4) {
            LogoDisplay(
                image = Images.Logos.minimalLogo,
                modifier = Modifier.color(Colors.White),
                variant = SeparatorLogoVariant
            )
        }
    }
}