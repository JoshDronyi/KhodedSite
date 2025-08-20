package com.probro.khoded.pages.homeSections

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.probro.khoded.components.layout.KhodedSection
import com.probro.khoded.components.layout.VerticalSpacer
import com.probro.khoded.components.branding.KhodedLogo
import com.probro.khoded.components.branding.LogoSize
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

/**
 * Detailed Business Case Studies Section
 * Shows comprehensive before/after scenarios with specific business metrics
 */

data class BusinessCaseStudy(
    val companyName: String,
    val industry: String,
    val companySize: String,
    val challenge: String,
    val solution: String,
    val implementation: String,
    val results: BusinessResults,
    val quote: String,
    val contact: String,
    val timeline: String,
    val technologies: List<String>
)

data class BusinessResults(
    val costSavings: String,
    val timeToMarket: String,
    val performanceGain: String,
    val maintenanceReduction: String,
    val userGrowth: String,
    val revenueImpact: String
)

@Composable
fun DetailedCaseStudiesSection() {
    KhodedSection(
        id = "case-studies",
        ariaLabel = "Detailed Case Studies",
        backgroundColor = KhodedDesignSystem.colors.backgroundSecondary
    ) {
        // Section header
        CaseStudiesHeader()
        
        VerticalSpacer(60.px)
        
        // Detailed case studies
        val caseStudies = remember { getDetailedCaseStudies() }
        
        caseStudies.forEachIndexed { index, caseStudy ->
            DetailedCaseStudyCard(caseStudy, index % 2 == 0)
            if (index < caseStudies.size - 1) {
                VerticalSpacer(80.px)
            }
        }
        
        VerticalSpacer(60.px)
        
        // Results summary
        OverallResultsSection()
    }
}

@Composable
private fun CaseStudiesHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        // Section badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.px),
            modifier = Modifier
                .backgroundColor(rgba(6, 182, 212, 0.15))
                .padding(10.px, 24.px)
                .borderRadius(50.px)
        ) {
            KhodedLogo(size = LogoSize.Medium)
            
            SpanText(
                text = "Proven Results",
                modifier = Modifier
                    .fontSize(14.px)
                    .fontWeight(600)
                    .color(rgb(6, 182, 212))
            )
        }
        
        // Main heading
        H2(
            attrs = Modifier
                .fontSize(48.px)
                .fontWeight(800)
                .color(rgb(15, 23, 42))
                .textAlign(TextAlign.Center)
                .lineHeight(1.2)
                .letterSpacing((-0.5).px)
                .maxWidth(800.px)
                .toAttrs()
        ) {
            Text("How Businesses Actually Save with Kotlin Multiplatform")
        }
        
        // Description
        P(
            attrs = Modifier
                .fontSize(20.px)
                .fontWeight(400)
                .color(rgba(15, 23, 42, 0.7))
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(700.px)
                .toAttrs()
        ) {
            Text("Real projects, real numbers, real business impact. See the complete journey from challenge to measurable results.")
        }
    }
}

@Composable
private fun DetailedCaseStudyCard(caseStudy: BusinessCaseStudy, isReversed: Boolean) {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .toAttrs {
                style {
                    display(DisplayStyle.Grid)
                    property("grid-template-columns", if (isReversed) "1fr 1fr" else "1fr 1fr")
                    property("gap", "60px")
                    property("align-items", "center")
                    property("@media (max-width: 768px)", "{ grid-template-columns: 1fr; gap: 40px; }")
                }
            }
    ) {
        // Content side
        Div(
            attrs = Modifier
                .toAttrs {
                    style {
                        property("order", if (isReversed) "2" else "1")
                        property("@media (max-width: 768px)", "{ order: 1; }")
                    }
                }
        ) {
            CaseStudyContent(caseStudy)
        }
        
        // Results side
        Div(
            attrs = Modifier
                .toAttrs {
                    style {
                        property("order", if (isReversed) "1" else "2")
                        property("@media (max-width: 768px)", "{ order: 2; }")
                    }
                }
        ) {
            CaseStudyResults(caseStudy)
        }
    }
}

@Composable
private fun CaseStudyContent(caseStudy: BusinessCaseStudy) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        // Company header
        Column(
            verticalArrangement = Arrangement.spacedBy(8.px)
        ) {
            H3(
                attrs = Modifier
                    .fontSize(32.px)
                    .fontWeight(700)
                    .color(rgb(15, 23, 42))
                    .toAttrs()
            ) {
                Text(caseStudy.companyName)
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.px),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Span(
                    attrs = Modifier
                        .fontSize(16.px)
                        .color(rgba(15, 23, 42, 0.6))
                        .toAttrs()
                ) {
                    Text(caseStudy.industry)
                }
                
                Div(
                    attrs = Modifier
                        .backgroundColor(rgba(6, 182, 212, 0.1))
                        .borderRadius(12.px)
                        .padding(4.px, 12.px)
                        .toAttrs()
                ) {
                    SpanText(
                        text = caseStudy.companySize,
                        modifier = Modifier
                            .fontSize(12.px)
                            .fontWeight(600)
                            .color(rgb(6, 182, 212))
                    )
                }
            }
        }
        
        // Challenge
        Column(
            verticalArrangement = Arrangement.spacedBy(12.px)
        ) {
            H4(
                attrs = Modifier
                    .fontSize(18.px)
                    .fontWeight(600)
                    .color(rgb(220, 38, 38))
                    .toAttrs()
            ) {
                Text("The Challenge")
            }
            
            P(
                attrs = Modifier
                    .fontSize(16.px)
                    .color(rgba(15, 23, 42, 0.8))
                    .lineHeight(1.6)
                    .toAttrs()
            ) {
                Text(caseStudy.challenge)
            }
        }
        
        // Solution
        Column(
            verticalArrangement = Arrangement.spacedBy(12.px)
        ) {
            H4(
                attrs = Modifier
                    .fontSize(18.px)
                    .fontWeight(600)
                    .color(rgb(34, 197, 94))
                    .toAttrs()
            ) {
                Text("Our Solution")
            }
            
            P(
                attrs = Modifier
                    .fontSize(16.px)
                    .color(rgba(15, 23, 42, 0.8))
                    .lineHeight(1.6)
                    .toAttrs()
            ) {
                Text(caseStudy.solution)
            }
        }
        
        // Quote
        Div(
            attrs = Modifier
                .backgroundColor(rgba(6, 182, 212, 0.05))
                .borderRadius(12.px)
                .padding(24.px)
                .toAttrs {
                    style {
                        borderLeft(4.px, LineStyle.Solid, rgb(6, 182, 212))
                    }
                }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.px)
            ) {
                P(
                    attrs = Modifier
                        .fontSize(18.px)
                        .fontStyle(FontStyle.Italic)
                        .color(rgb(15, 23, 42))
                        .lineHeight(1.5)
                        .toAttrs()
                ) {
                    Text("\"${caseStudy.quote}\"")
                }
                
                SpanText(
                    text = "— ${caseStudy.contact}",
                    modifier = Modifier
                        .fontSize(14.px)
                        .fontWeight(600)
                        .color(rgb(6, 182, 212))
                )
            }
        }
    }
}

@Composable
private fun CaseStudyResults(caseStudy: BusinessCaseStudy) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.px)
    ) {
        H4(
            attrs = Modifier
                .fontSize(24.px)
                .fontWeight(700)
                .color(rgb(15, 23, 42))
                .textAlign(TextAlign.Center)
                .toAttrs()
        ) {
            Text("Business Impact")
        }
        
        // Results grid
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .toAttrs {
                    style {
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "1fr 1fr")
                        property("gap", "20px")
                    }
                }
        ) {
            ResultMetricCard("Cost Savings", caseStudy.results.costSavings, rgb(34, 197, 94))
            ResultMetricCard("Time to Market", caseStudy.results.timeToMarket, rgb(6, 182, 212))
            ResultMetricCard("Performance", caseStudy.results.performanceGain, rgb(139, 92, 246))
            ResultMetricCard("Maintenance", caseStudy.results.maintenanceReduction, rgb(249, 115, 22))
        }
        
        // Additional results
        Column(
            verticalArrangement = Arrangement.spacedBy(16.px)
        ) {
            if (caseStudy.results.userGrowth.isNotEmpty()) {
                ResultRow("User Growth", caseStudy.results.userGrowth)
            }
            
            if (caseStudy.results.revenueImpact.isNotEmpty()) {
                ResultRow("Revenue Impact", caseStudy.results.revenueImpact)
            }
            
            ResultRow("Implementation Time", caseStudy.timeline)
        }
        
        // Technologies used
        Column(
            verticalArrangement = Arrangement.spacedBy(12.px)
        ) {
            SpanText(
                text = "Technologies Used:",
                modifier = Modifier
                    .fontSize(14.px)
                    .fontWeight(600)
                    .color(rgba(15, 23, 42, 0.7))
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.px),
                modifier = Modifier.fillMaxWidth()
            ) {
                caseStudy.technologies.forEach { tech ->
                    Div(
                        attrs = Modifier
                            .backgroundColor(rgba(15, 23, 42, 0.1))
                            .borderRadius(16.px)
                            .padding(6.px, 12.px)
                            .toAttrs()
                    ) {
                        SpanText(
                            text = tech,
                            modifier = Modifier
                                .fontSize(12.px)
                                .color(rgb(15, 23, 42))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultMetricCard(label: String, value: String, color: CSSColorValue) {
    Div(
        attrs = Modifier
            .backgroundColor(rgba(255, 255, 255, 0.8))
            .borderRadius(12.px)
            .padding(20.px)
            .toAttrs {
                style {
                    border(2.px, LineStyle.Solid, color)
                    property("text-align", "center")
                }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.px)
        ) {
            SpanText(
                text = value,
                modifier = Modifier
                    .fontSize(24.px)
                    .fontWeight(800)
                    .color(color)
            )
            
            SpanText(
                text = label,
                modifier = Modifier
                    .fontSize(12.px)
                    .color(rgba(15, 23, 42, 0.7))
                    .fontWeight(500)
            )
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SpanText(
            text = label,
            modifier = Modifier
                .fontSize(14.px)
                .color(rgba(15, 23, 42, 0.7))
        )
        
        SpanText(
            text = value,
            modifier = Modifier
                .fontSize(14.px)
                .fontWeight(600)
                .color(rgb(15, 23, 42))
        )
    }
}

@Composable
private fun OverallResultsSection() {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .backgroundColor(rgb(15, 23, 42))
            .borderRadius(16.px)
            .padding(40.px)
            .toAttrs()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.px)
        ) {
            H3(
                attrs = Modifier
                    .fontSize(32.px)
                    .fontWeight(700)
                    .color(Color.white)
                    .textAlign(TextAlign.Center)
                    .toAttrs()
            ) {
                Text("Average Client Results")
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(60.px, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OverallResultStat("60%", "Average Cost Savings")
                OverallResultStat("4 months", "Time to Market Improvement")
                OverallResultStat("80%", "Code Reuse Achieved")
                OverallResultStat("70%", "Maintenance Reduction")
            }
        }
    }
}

@Composable
private fun OverallResultStat(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        SpanText(
            text = value,
            modifier = Modifier
                .fontSize(32.px)
                .fontWeight(800)
                .color(rgb(6, 182, 212))
        )
        
        SpanText(
            text = label,
            modifier = Modifier
                .fontSize(14.px)
                .color(rgba(255, 255, 255, 0.8))
                .textAlign(TextAlign.Center)
                .maxWidth(120.px)
        )
    }
}

private fun getDetailedCaseStudies(): List<BusinessCaseStudy> {
    return listOf(
        BusinessCaseStudy(
            companyName = "MedConnect Solutions",
            industry = "Healthcare Technology",
            companySize = "Series A Startup (35 employees)",
            challenge = "MedConnect needed HIPAA-compliant mobile apps for both doctors and patients, but developing separate iOS and Android apps would cost $300K and take 14 months. Their Series A funding required faster time-to-market to capture competitive advantage in telehealth.",
            solution = "We implemented a single Kotlin Multiplatform codebase with shared business logic, HIPAA-compliant data handling, and platform-specific UI optimizations. The shared architecture ensured consistent security implementations across platforms while maintaining native performance for real-time video consultations.",
            implementation = "6-month development cycle with shared security layer, real-time communication features, and automated compliance reporting",
            results = BusinessResults(
                costSavings = "$180K saved",
                timeToMarket = "8 months faster",
                performanceGain = "99.9% uptime",
                maintenanceReduction = "70% fewer bugs",
                userGrowth = "15K active users",
                revenueImpact = "$2M ARR achieved"
            ),
            quote = "Kotlin Multiplatform didn't just save us money—it saved our Series A timeline. We launched both apps simultaneously and captured market share our competitors are still fighting for.",
            contact = "Dr. Sarah Chen, Founder & CEO",
            timeline = "6 months",
            technologies = listOf("Kotlin Multiplatform", "Ktor", "SQLDelight", "Compose Multiplatform")
        ),
        
        BusinessCaseStudy(
            companyName = "RetailFlow Analytics",
            industry = "Retail Technology",
            companySize = "Growing Business (85 employees)",
            challenge = "RetailFlow's inventory management system needed mobile apps for warehouse staff and store managers across 200+ locations. Traditional development would require separate teams, different release cycles, and inconsistent feature sets between platforms, risking operational efficiency.",
            solution = "Built a unified Kotlin Multiplatform solution with shared business logic for inventory calculations, offline synchronization, and real-time updates. Platform-specific implementations handled barcode scanning and native integrations while maintaining consistent user experience and data accuracy.",
            implementation = "8-month engagement including backend API optimization, offline-first architecture, and comprehensive testing across device types",
            results = BusinessResults(
                costSavings = "$240K saved",
                timeToMarket = "5 months faster",
                performanceGain = "40% faster operations",
                maintenanceReduction = "65% cost reduction",
                userGrowth = "500+ daily users",
                revenueImpact = "22% efficiency gain"
            ),
            quote = "Our warehouse efficiency improved 40% because staff finally had consistent, reliable tools on both Android tablets and iOS devices. The cost savings let us expand to three new markets.",
            contact = "Michael Rodriguez, Operations Director",
            timeline = "8 months",
            technologies = listOf("Kotlin Multiplatform", "Compose Multiplatform", "SQLDelight", "Ktor Client")
        )
    )
}