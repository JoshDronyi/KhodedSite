package com.probro.khoded.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient

@Composable
fun Footer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(topBottom = 80.px)
            .backgroundImage(
                linearGradient(
                    dir = LinearGradient.Direction.ToBottomRight,
                    from = rgb(15, 23, 42),    // Deep navy
                    to = rgb(30, 41, 59)       // Lighter navy  
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(60.px)
    ) {

        // Quote section with logo accent
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(800.px)
                .padding(40.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.px)
        ) {
            // Small logo accent
            org.jetbrains.compose.web.dom.Div(
                attrs = {
                    style {
                        width(32.px)
                        height(26.px)
                        backgroundColor(rgba(6, 182, 212, 0.3))
                        borderRadius(6.px)
                        display(DisplayStyle.Flex)
                        property("align-items", "center")
                        property("justify-content", "center")
                        fontSize(12.px)
                        fontWeight(700)
                        color(rgb(6, 182, 212))
                        fontFamily("'Fira Code', 'Consolas', 'Monaco', monospace")
                    }
                }
            ) {
                org.jetbrains.compose.web.dom.Text("</&gt;")
            }
            
            SpanText(
                "Crafting the perfect website is like fashioning a tale that extends beyond the confines of conventional storytelling. It's about creating an immersive experience that ensures your narrative is heard, seen, and felt in places beyond your physical reach.",
                modifier = Modifier
                    .fontSize(28.px)
                    .lineHeight(1.6)
                    .color(Color.white)
                    .textAlign(TextAlign.Center)
            )
        }

        // Bottom navigation links
        Row(
            horizontalArrangement = Arrangement.spacedBy(40.px),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.px)
        ) {
            FooterNavLink("OUR STORY")
            FooterNavLink("JOIN OUR TEAM")
            FooterNavLink("CONTACT")
            FooterNavLink("CONSULTATION")
            FooterNavLink("TERMS")
        }
    }
}

@Composable
private fun FooterNavLink(
    text: String
) {
    SpanText(
        text,
        modifier = Modifier
            .fontSize(14.px)
            .fontWeight(500)
            .color(Color.white)
            .cursor(Cursor.Pointer)
            .padding(8.px)
    )
}