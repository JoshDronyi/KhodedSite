package com.probro.khoded.components.composables

import androidx.compose.runtime.Composable
import com.probro.khoded.styles.componentStyles.BackingCardStyle
import com.probro.khoded.styles.componentStyles.DivKind
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.breakpoint.ResponsiveValues
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px


val NoBorderBackingCardVariant = BackingCardStyle.addVariant {
    base {
        Modifier
    }
}

val SingleBorderBackingCardVariant = BackingCardStyle.addVariant {
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
    variant: CssStyleVariant<DivKind>? = null,
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
    variant: CssStyleVariant<DivKind>? = null,
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