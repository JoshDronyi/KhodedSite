package com.probro.khoded.components.business

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.js.Date

/**
 * Interactive ROI Calculator for Small Businesses
 * Demonstrates cost savings and time benefits of Kotlin Multiplatform
 */

data class ROICalculation(
    val traditionalCost: Int,
    val kmpCost: Int, 
    val savings: Int,
    val percentSaved: Int,
    val timeToMarket: Int,
    val competitiveAdvantage: String
)

@Composable
fun BusinessROICalculator() {
    var appComplexity by remember { mutableStateOf("Medium") }
    var teamSize by remember { mutableStateOf("5-10") }
    var timeframe by remember { mutableStateOf("6-12 months") }
    var showResults by remember { mutableStateOf(false) }
    
    val calculation = remember(appComplexity, teamSize, timeframe) {
        calculateROI(appComplexity, teamSize, timeframe)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.backgroundDark)
            .padding(60.px, 40.px)
            .borderRadius(16.px),
        verticalArrangement = Arrangement.spacedBy(40.px)
    ) {
        // Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.px)
        ) {
            H3(
                attrs = Modifier
                    .fontSize(32.px)
                    .fontWeight(700)
                    .color(Color.white)
                    .textAlign(TextAlign.Center)
                    .toAttrs()
            ) {
                Text("Calculate Your Savings with Kotlin Multiplatform")
            }
            
            P(
                attrs = Modifier
                    .fontSize(18.px)
                    .color(rgba(255, 255, 255, 0.8))
                    .textAlign(TextAlign.Center)
                    .maxWidth(600.px)
                    .toAttrs()
            ) {
                Text("See how much money and time you'll save by choosing cross-platform development over traditional native apps.")
            }
        }
        
        // Input form
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(30.px, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Top
        ) {
            // App Complexity
            InputGroup(
                label = "App Complexity",
                value = appComplexity,
                options = listOf("Simple", "Medium", "Complex"),
                onValueChange = { appComplexity = it }
            )
            
            // Team Size
            InputGroup(
                label = "Development Team Size",
                value = teamSize,
                options = listOf("2-5", "5-10", "10-20", "20+"),
                onValueChange = { teamSize = it }
            )
            
            // Development Timeframe
            InputGroup(
                label = "Project Timeline",
                value = timeframe,
                options = listOf("3-6 months", "6-12 months", "1-2 years", "2+ years"),
                onValueChange = { timeframe = it }
            )
        }
        
        // Calculate button
        org.jetbrains.compose.web.dom.Button(
            attrs = {
                style {
                    backgroundColor(KhodedDesignSystem.colors.primary)
                    color(Color.white)
                    border(0.px)
                    borderRadius(12.px)
                    padding(16.px, 32.px)
                    fontSize(18.px)
                    fontWeight(600)
                    cursor(Cursor.Pointer)
                    minWidth(200.px)
                    property("margin", "0 auto")
                    property("display", "block")
                }
                onClick { showResults = true }
            }
        ) {
            Text("Calculate My Savings")
        }
        
        // Results
        if (showResults) {
            ROIResults(calculation)
        }
    }
}

@Composable
private fun InputGroup(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.px)
    ) {
        Label(
            attrs = Modifier
                .fontSize(16.px)
                .fontWeight(600)
                .color(Color.white)
                .toAttrs()
        ) {
            Text(label)
        }
        
        Select(
            attrs = {
                style {
                    backgroundColor(rgba(255, 255, 255, 0.1))
                    color(Color.white)
                    border(1.px, LineStyle.Solid, rgba(255, 255, 255, 0.3))
                    borderRadius(8.px)
                    padding(12.px)
                    fontSize(16.px)
                    minWidth(180.px)
                    property("outline", "none")
                }
                onInput { event ->
                    onValueChange(event.target.value)
                }
            }
        ) {
            options.forEach { option ->
                Option(
                    value = option,
                    attrs = {
                        if (option == value) {
                            selected()
                        }
                        style {
                            backgroundColor(rgb(15, 23, 42))
                            color(Color.white)
                        }
                    }
                ) {
                    Text(option)
                }
            }
        }
    }
}

@Composable
private fun ROIResults(calculation: ROICalculation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(rgba(6, 182, 212, 0.1))
            .border(1.px, LineStyle.Solid, rgb(6, 182, 212))
            .borderRadius(12.px)
            .padding(30.px),
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        H4(
            attrs = Modifier
                .fontSize(24.px)
                .fontWeight(700)
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text("Your Potential Savings")
        }
        
        // Savings grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.px, Alignment.CenterHorizontally)
        ) {
            ResultCard(
                title = "Total Savings",
                value = "$${calculation.savings.toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")}",
                subtitle = "${calculation.percentSaved}% cost reduction",
                color = rgb(34, 197, 94)
            )
            
            ResultCard(
                title = "Time to Market",
                value = "${calculation.timeToMarket} months faster",
                subtitle = "Beat competition to market",
                color = rgb(139, 92, 246)
            )
            
            ResultCard(
                title = "Competitive Edge",
                value = calculation.competitiveAdvantage,
                subtitle = "Market advantage",
                color = rgb(6, 182, 212)
            )
        }
        
        // Breakdown
        Column(
            verticalArrangement = Arrangement.spacedBy(12.px)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SpanText("Traditional Native Development:", Modifier.color(rgba(255, 255, 255, 0.8)))
                SpanText("$${calculation.traditionalCost.toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")}", Modifier.color(Color.white).fontWeight(600))
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SpanText("Kotlin Multiplatform Development:", Modifier.color(rgba(255, 255, 255, 0.8)))
                SpanText("$${calculation.kmpCost.toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")}", Modifier.color(rgb(34, 197, 94)).fontWeight(600))
            }
        }
    }
}

@Composable
private fun ResultCard(
    title: String,
    value: String,
    subtitle: String,
    color: CSSColorValue
) {
    Column(
        modifier = Modifier
            .backgroundColor(rgba(255, 255, 255, 0.05))
            .borderRadius(8.px)
            .padding(20.px)
            .minWidth(200.px),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        SpanText(
            title,
            modifier = Modifier
                .fontSize(14.px)
                .color(rgba(255, 255, 255, 0.8))
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            value,
            modifier = Modifier
                .fontSize(24.px)
                .fontWeight(700)
                .color(color)
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            subtitle,
            modifier = Modifier
                .fontSize(12.px)
                .color(rgba(255, 255, 255, 0.6))
                .textAlign(TextAlign.Center)
        )
    }
}

private fun calculateROI(complexity: String, teamSize: String, timeframe: String): ROICalculation {
    // Base multipliers
    val complexityMultiplier = when (complexity) {
        "Simple" -> 1.0
        "Medium" -> 1.5
        "Complex" -> 2.2
        else -> 1.5
    }
    
    val teamMultiplier = when (teamSize) {
        "2-5" -> 1.0
        "5-10" -> 1.4
        "10-20" -> 1.8
        "20+" -> 2.5
        else -> 1.4
    }
    
    val timeMultiplier = when (timeframe) {
        "3-6 months" -> 0.8
        "6-12 months" -> 1.0
        "1-2 years" -> 1.6
        "2+ years" -> 2.4
        else -> 1.0
    }
    
    // Base costs (in thousands)
    val baseCost = 120_000 // Base traditional development cost
    val traditionalCost = (baseCost * complexityMultiplier * teamMultiplier * timeMultiplier).toInt()
    val kmpCost = (traditionalCost * 0.4).toInt() // 60% savings
    val savings = traditionalCost - kmpCost
    val percentSaved = ((savings.toDouble() / traditionalCost.toDouble()) * 100).toInt()
    
    val timeToMarket = when (timeframe) {
        "3-6 months" -> 2
        "6-12 months" -> 4
        "1-2 years" -> 6
        "2+ years" -> 12
        else -> 4
    }
    
    val competitiveAdvantage = when {
        timeToMarket >= 6 -> "Major Advantage"
        timeToMarket >= 4 -> "Strong Advantage"
        else -> "Quick Launch"
    }
    
    return ROICalculation(
        traditionalCost = traditionalCost,
        kmpCost = kmpCost,
        savings = savings,
        percentSaved = percentSaved,
        timeToMarket = timeToMarket,
        competitiveAdvantage = competitiveAdvantage
    )
}