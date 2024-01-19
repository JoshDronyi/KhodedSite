package com.probro.khoded.pages.aboutSections

import androidx.compose.runtime.Composable
import com.probro.khoded.components.composables.LogoDisplay
import com.probro.khoded.components.composables.SeparatorImageVariant
import com.probro.khoded.components.composables.SeparatorLogoContainerVariant
import com.probro.khoded.components.composables.SeparatorLogoTextVariant
import com.probro.khoded.models.Images
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px

@Composable
fun SeparatorSectionDisplay(baseModifier: Modifier) = with(Pages.Story_Section.Separator) {
    Row(
        modifier = baseModifier.fillMaxWidth()
            .height(Height.MinContent)
            .backgroundColor(Colors.Black)
            .padding(topBottom = 10.px)
    ) {
        repeat(4) {
            LogoDisplay(
                image = Images.Logos.minimalLogo,
                variant = SeparatorLogoContainerVariant,
                textVariant = SeparatorLogoTextVariant,
                imageVariant = SeparatorImageVariant,
            )
        }
    }
}