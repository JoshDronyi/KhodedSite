package com.probro.khoded.components.branding

import androidx.compose.runtime.Composable
import com.probro.khoded.design.KhodedDesignSystem
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

/**
 * Unified Khoded logo component
 * Replaces 5+ duplicate logo implementations across the codebase
 * Provides consistent branding with scalable sizing
 */
@Composable
fun KhodedLogo(
    size: LogoSize = LogoSize.Medium,
    color: CSSColorValue = KhodedDesignSystem.colors.primary,
    modifier: Modifier = Modifier
) {
    val dimensions = when (size) {
        LogoSize.Small -> 16.px to 12.px
        LogoSize.Medium -> 20.px to 16.px  
        LogoSize.Large -> 32.px to 26.px
        LogoSize.XLarge -> 80.px to 64.px
    }
    
    val fontSize = when (size) {
        LogoSize.Small -> 8.px
        LogoSize.Medium -> 10.px
        LogoSize.Large -> 14.px
        LogoSize.XLarge -> 24.px
    }
    
    val borderRadius = when (size) {
        LogoSize.Small, LogoSize.Medium -> 4.px
        else -> 6.px
    }
    
    Div(
        attrs = modifier
            .size(dimensions.first, dimensions.second)
            .backgroundColor(color)
            .borderRadius(borderRadius)
            .toAttrs {
                style {
                    display(DisplayStyle.Flex)
                    property("align-items", "center")
                    property("justify-content", "center")
                    fontSize(fontSize)
                    fontWeight(700)
                    color(Color.white)
                    fontFamily("'Fira Code', 'Consolas', 'Monaco', monospace")
                }
            }
    ) {
        Text("</>")
    }
}

enum class LogoSize { 
    Small,    // 16x12px - For badges and small UI elements
    Medium,   // 20x16px - Standard section headers
    Large,    // 32x26px - Navigation bar 
    XLarge    // 80x64px - Hero section
}