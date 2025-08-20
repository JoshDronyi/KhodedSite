package com.probro.khoded.components.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Section

/**
 * Shared layout components to eliminate code duplication across home page sections
 * Provides consistent styling and accessibility patterns
 */

/**
 * Standard container component used across all home page sections
 * Replaces duplicate Container implementations in ModernAbout, ModernServices, and ModernContact
 */
@Composable
fun KhodedContainer(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

/**
 * Standard section wrapper with consistent styling and accessibility
 * Provides unified pattern for all home page sections
 */
@Composable
fun KhodedSection(
    id: String,
    ariaLabel: String,
    backgroundColor: CSSColorValue,
    verticalPadding: CSSNumericValue<CSSUnit.px> = 120.px,
    horizontalPadding: CSSNumericValue<CSSUnit.px> = 24.px,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Section(
        attrs = modifier
            .fillMaxWidth()
            .backgroundColor(backgroundColor)
            .padding(topBottom = verticalPadding, leftRight = horizontalPadding)
            .toAttrs {
                id(id)
                attr("aria-label", ariaLabel)
            }
    ) {
        KhodedContainer(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Centered content container for consistent layout
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

/**
 * Standard spacer component for consistent vertical spacing
 */
@Composable
fun VerticalSpacer(height: CSSNumericValue<CSSUnit.px> = 80.px) {
    org.jetbrains.compose.web.dom.Div(
        attrs = { 
            style { 
                height(height) 
            } 
        }
    )
}