package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.Composable
import com.probro.khoded.utils.Pages
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*

/**
 * Placeholder implementations for disabled sections
 * These provide basic functionality while full sections are being migrated
 */

@Composable
fun ServicesSectionDisplay(data: Pages.Home_Section.Services) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(rgb(249, 250, 251))
            .padding(80.px, 40.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.px)
    ) {
        SpanText(
            "Our Services",
            modifier = Modifier
                .fontSize(36.px)
                .fontWeight(700)
                .color(rgb(31, 41, 55))
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            "Web Development • E-commerce • Branding • Hosting",
            modifier = Modifier
                .fontSize(18.px)
                .color(rgb(75, 85, 99))
                .textAlign(TextAlign.Center)
        )
    }
}

@Composable
fun DesignSectionDisplay(data: Pages.Home_Section.Design) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Color.white)
            .padding(80.px, 40.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.px)
    ) {
        SpanText(
            "Design Excellence",
            modifier = Modifier
                .fontSize(36.px)
                .fontWeight(700)
                .color(rgb(31, 41, 55))
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            "Beautiful, functional designs that tell your story and engage your audience.",
            modifier = Modifier
                .fontSize(18.px)
                .color(rgb(75, 85, 99))
                .textAlign(TextAlign.Center)
                .maxWidth(600.px)
        )
    }
}

@Composable
fun ConsultationSectionDisplay(data: Pages.Home_Section.Consultation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(rgb(147, 51, 234))
            .padding(80.px, 40.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.px)
    ) {
        SpanText(
            "Ready to Get Started?",
            modifier = Modifier
                .fontSize(36.px)
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            "Schedule a free consultation to discuss your project needs.",
            modifier = Modifier
                .fontSize(18.px)
                .color(Color.white)
                .textAlign(TextAlign.Center)
        )
        
        Column(
            modifier = Modifier
                .padding(16.px, 24.px)
                .backgroundColor(Color.white)
                .borderRadius(50.px),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText(
                "Schedule Free Consultation",
                modifier = Modifier
                    .fontSize(16.px)
                    .fontWeight(600)
                    .color(rgb(147, 51, 234))
            )
        }
    }
}