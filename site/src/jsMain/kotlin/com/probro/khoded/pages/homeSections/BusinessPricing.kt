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
 * Business-Focused Pricing Section
 * Emphasizes value, ROI, and business outcomes rather than technical features
 */

data class PricingPackage(
    val name: String,
    val description: String,
    val price: String,
    val priceSubtext: String,
    val features: List<String>,
    val businessOutcomes: List<String>,
    val isPopular: Boolean = false,
    val ctaText: String,
    val savingsHighlight: String?,
    val idealFor: String
)

@Composable
fun BusinessPricingSection() {
    KhodedSection(
        id = "pricing",
        ariaLabel = "Pricing Plans",
        backgroundColor = KhodedDesignSystem.colors.backgroundDark
    ) {
        // Section header
        PricingHeader()
        
        VerticalSpacer(60.px)
        
        // Pricing packages
        PricingGrid()
        
        VerticalSpacer(40.px)
        
        // Value guarantee section
        ValueGuaranteeSection()
    }
}

@Composable
private fun PricingHeader() {
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
                text = "Investment Options",
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
                .color(Color.white)
                .textAlign(TextAlign.Center)
                .lineHeight(1.2)
                .letterSpacing((-0.5).px)
                .maxWidth(800.px)
                .toAttrs()
        ) {
            Text("Choose Your Business Growth Strategy")
        }
        
        // Description
        P(
            attrs = Modifier
                .fontSize(20.px)
                .fontWeight(400)
                .color(rgba(255, 255, 255, 0.8))
                .textAlign(TextAlign.Center)
                .lineHeight(1.6)
                .maxWidth(700.px)
                .toAttrs()
        ) {
            Text("Every package includes Kotlin Multiplatform expertise, dedicated support, and measurable ROI. Choose based on your business needs and growth timeline.")
        }
    }
}

@Composable 
private fun PricingGrid() {
    val packages = remember { getBusinessPackages() }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(32.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        packages.forEach { pricingPackage ->
            PricingCard(pricingPackage)
        }
    }
}

@Composable
private fun PricingCard(pricingPackage: PricingPackage) {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .maxWidth(600.px)
            .backgroundColor(if (pricingPackage.isPopular) rgba(6, 182, 212, 0.1) else rgba(255, 255, 255, 0.05))
            .borderRadius(16.px)
            .padding(32.px)
            .toAttrs {
                style {
                    border(
                        if (pricingPackage.isPopular) 2.px else 1.px,
                        LineStyle.Solid,
                        if (pricingPackage.isPopular) rgb(6, 182, 212) else rgba(255, 255, 255, 0.2)
                    )
                    property("transition", "all 0.3s ease")
                    position(Position.Relative)
                }
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.px)
        ) {
            // Popular badge
            if (pricingPackage.isPopular) {
                Div(
                    attrs = Modifier
                        .backgroundColor(rgb(6, 182, 212))
                        .color(Color.white)
                        .borderRadius(20.px)
                        .padding(6.px, 16.px)
                        .toAttrs {
                            style {
                                position(Position.Absolute)
                                top((-12).px)
                                left(50.percent)
                                property("transform", "translateX(-50%)")
                            }
                        }
                ) {
                    SpanText(
                        text = "Most Popular",
                        modifier = Modifier
                            .fontSize(12.px)
                            .fontWeight(600)
                    )
                }
            }
            
            // Package header
            Column(
                verticalArrangement = Arrangement.spacedBy(12.px)
            ) {
                H3(
                    attrs = Modifier
                        .fontSize(24.px)
                        .fontWeight(700)
                        .color(Color.white)
                        .toAttrs()
                ) {
                    Text(pricingPackage.name)
                }
                
                P(
                    attrs = Modifier
                        .fontSize(16.px)
                        .color(rgba(255, 255, 255, 0.8))
                        .lineHeight(1.5)
                        .toAttrs()
                ) {
                    Text(pricingPackage.description)
                }
                
                // Ideal for
                Div(
                    attrs = Modifier
                        .backgroundColor(rgba(139, 92, 246, 0.2))
                        .borderRadius(8.px)
                        .padding(8.px, 12.px)
                        .toAttrs()
                ) {
                    SpanText(
                        text = "Ideal for: ${pricingPackage.idealFor}",
                        modifier = Modifier
                            .fontSize(12.px)
                            .color(rgb(139, 92, 246))
                            .fontWeight(500)
                    )
                }
            }
            
            // Pricing
            Column(
                verticalArrangement = Arrangement.spacedBy(8.px)
            ) {
                SpanText(
                    text = pricingPackage.price,
                    modifier = Modifier
                        .fontSize(48.px)
                        .fontWeight(800)
                        .color(Color.white)
                )
                
                SpanText(
                    text = pricingPackage.priceSubtext,
                    modifier = Modifier
                        .fontSize(14.px)
                        .color(rgba(255, 255, 255, 0.6))
                )
                
                // Savings highlight
                pricingPackage.savingsHighlight?.let { savings ->
                    Div(
                        attrs = Modifier
                            .backgroundColor(rgba(34, 197, 94, 0.2))
                            .borderRadius(6.px)
                            .padding(6.px, 12.px)
                            .toAttrs()
                    ) {
                        SpanText(
                            text = savings,
                            modifier = Modifier
                                .fontSize(12.px)
                                .fontWeight(600)
                                .color(rgb(34, 197, 94))
                        )
                    }
                }
            }
            
            // Business outcomes
            Column(
                verticalArrangement = Arrangement.spacedBy(12.px)
            ) {
                H4(
                    attrs = Modifier
                        .fontSize(16.px)
                        .fontWeight(600)
                        .color(Color.white)
                        .toAttrs()
                ) {
                    Text("Business Outcomes:")
                }
                
                pricingPackage.businessOutcomes.forEach { outcome ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.px)
                    ) {
                        SpanText(
                            text = "💰",
                            modifier = Modifier.fontSize(16.px)
                        )
                        
                        SpanText(
                            text = outcome,
                            modifier = Modifier
                                .fontSize(14.px)
                                .color(rgba(255, 255, 255, 0.9))
                                .fontWeight(500)
                        )
                    }
                }
            }
            
            // Technical features
            Column(
                verticalArrangement = Arrangement.spacedBy(8.px)
            ) {
                H4(
                    attrs = Modifier
                        .fontSize(14.px)
                        .fontWeight(600)
                        .color(rgba(255, 255, 255, 0.7))
                        .toAttrs()
                ) {
                    Text("What's Included:")
                }
                
                pricingPackage.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.px)
                    ) {
                        SpanText(
                            text = "✓",
                            modifier = Modifier
                                .fontSize(14.px)
                                .color(rgb(34, 197, 94))
                                .fontWeight(700)
                        )
                        
                        SpanText(
                            text = feature,
                            modifier = Modifier
                                .fontSize(14.px)
                                .color(rgba(255, 255, 255, 0.8))
                        )
                    }
                }
            }
            
            // CTA Button
            org.jetbrains.compose.web.dom.Button(
                attrs = {
                    style {
                        backgroundColor(if (pricingPackage.isPopular) rgb(6, 182, 212) else Color.transparent)
                        color(if (pricingPackage.isPopular) Color.white else rgb(6, 182, 212))
                        border(2.px, LineStyle.Solid, rgb(6, 182, 212))
                        borderRadius(12.px)
                        padding(16.px, 24.px)
                        fontSize(16.px)
                        fontWeight(600)
                        cursor(Cursor.Pointer)
                        width(100.percent)
                        property("transition", "all 0.3s ease")
                    }
                    onClick {
                        // Scroll to contact section
                        kotlinx.browser.document.getElementById("contact")?.scrollIntoView(
                            js("{behavior: 'smooth', block: 'center'}")
                        )
                    }
                }
            ) {
                Text(pricingPackage.ctaText)
            }
        }
    }
}

@Composable
private fun ValueGuaranteeSection() {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .backgroundColor(rgba(6, 182, 212, 0.1))
            .borderRadius(16.px)
            .padding(40.px)
            .maxWidth(800.px)
            .toAttrs {
                style {
                    border(1.px, LineStyle.Solid, rgba(6, 182, 212, 0.3))
                    property("margin", "0 auto")
                }
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.px)
        ) {
            H3(
                attrs = Modifier
                    .fontSize(24.px)
                    .fontWeight(700)
                    .color(Color.white)
                    .textAlign(TextAlign.Center)
                    .toAttrs()
            ) {
                Text("🛡️ Our Value Guarantee")
            }
            
            P(
                attrs = Modifier
                    .fontSize(16.px)
                    .color(rgba(255, 255, 255, 0.9))
                    .textAlign(TextAlign.Center)
                    .lineHeight(1.6)
                    .toAttrs()
            ) {
                Text("If we don't deliver measurable cost savings and faster development within the first 90 days, we'll refund your investment. That's how confident we are in Kotlin Multiplatform's business value.")
            }
        }
    }
}

private fun getBusinessPackages(): List<PricingPackage> {
    return listOf(
        PricingPackage(
            name = "Quick Start",
            description = "Perfect for small businesses ready to modernize their mobile presence without enterprise complexity.",
            price = "$25K",
            priceSubtext = "Complete project cost",
            idealFor = "Small businesses, MVP launches, simple apps",
            savingsHighlight = "60% less than native development",
            features = listOf(
                "Cross-platform mobile app",
                "Basic admin dashboard",
                "App store deployment",
                "3 months support",
                "Source code ownership"
            ),
            businessOutcomes = listOf(
                "Launch on iOS & Android simultaneously",
                "Reach 100% of mobile users",
                "Save $15K+ vs separate native apps",
                "Get to market 4 months faster"
            ),
            ctaText = "Start Your Project",
            isPopular = false
        ),
        
        PricingPackage(
            name = "Business Growth",
            description = "Comprehensive solution for growing companies that need scalable, feature-rich applications with ongoing development.",
            price = "$75K",
            priceSubtext = "6-month engagement",
            idealFor = "Growing companies, complex features, ongoing development",
            savingsHighlight = "Save $120K+ vs traditional development",
            features = listOf(
                "Advanced cross-platform app",
                "Web admin portal",
                "API development & integration",
                "Analytics & reporting",
                "6 months dedicated support",
                "Team training included"
            ),
            businessOutcomes = listOf(
                "Scale to 10K+ concurrent users",
                "Reduce maintenance costs by 70%",
                "Launch new features 50% faster",
                "Build competitive moat with unique features"
            ),
            ctaText = "Scale Your Business",
            isPopular = true
        ),
        
        PricingPackage(
            name = "Enterprise Ready",
            description = "Full-scale enterprise solution with dedicated team, advanced security, and comprehensive business intelligence.",
            price = "Custom",
            priceSubtext = "Based on your requirements",
            idealFor = "Established companies, enterprise security, complex integrations",
            savingsHighlight = null,
            features = listOf(
                "Enterprise-grade architecture",
                "Advanced security & compliance",
                "Multiple platform deployment",
                "Dedicated development team",
                "Priority support & SLA",
                "Custom integrations & APIs",
                "Training & knowledge transfer"
            ),
            businessOutcomes = listOf(
                "Handle enterprise-scale traffic",
                "Meet compliance requirements (SOC2, HIPAA)",
                "Integrate with existing enterprise systems",
                "Build strategic technology advantage"
            ),
            ctaText = "Discuss Enterprise Needs",
            isPopular = false
        )
    )
}