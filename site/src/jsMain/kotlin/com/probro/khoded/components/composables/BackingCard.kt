package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.breakpoint.ResponsiveValues
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px


val BackingCardStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth(80.percent)
            .borderRadius(20.px)
            .padding(topBottom = 20.px, leftRight = 10.px)
            .margin(topBottom = 20.px)
            .color(Colors.Black)
    }
}

val NoBorderBackingCardVariant by BackingCardStyle.addVariant {
    base {
        Modifier
    }
}

val SingleBorderBackingCardVariant by BackingCardStyle.addVariant {
    base {
        Modifier
            .border {
                width(2.px)
                color(Color.darkgray)
                style(LineStyle.Solid)
            }
    }
}


@Composable
fun TwoPaneCard(
    numColumns: ResponsiveValues<Int>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SimpleGrid(
        numColumns = numColumns,
        modifier = modifier,
        content = content
    )
}


@Composable
fun BackingCard(
    modifier: Modifier = Modifier,
    variant: ComponentVariant? = null,
    firstSection: @Composable () -> Unit,
    secondSection: @Composable () -> Unit,
) {
    TwoPaneCard(
        numColumns = numColumns(
            base = 1, md = 2
        ),
        modifier = BackingCardStyle.toModifier(variant)
            .then(modifier)
    ) {
        firstSection()
        secondSection()
    }
}

@Composable
fun TeamSectionCard(
    modifier: Modifier = Modifier,
    variant: ComponentVariant? = null,
    firstSection: @Composable () -> Unit,
    secondSection: @Composable () -> Unit
) {
    TwoPaneCard(
        numColumns = numColumns(
            base = 1, lg = 2
        ),
        modifier = BackingCardStyle.toModifier(variant)
            .then(modifier)
    ) {
        firstSection()
        secondSection()
    }
}