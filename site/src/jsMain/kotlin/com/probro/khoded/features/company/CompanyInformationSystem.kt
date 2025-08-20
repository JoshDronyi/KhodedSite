package com.probro.khoded.features.company

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.components.icons.fa.FaStar
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

/**
 * Company Information Display System
 * 
 * Mobile-first responsive company information display implementing the three main goals:
 * 1. Displaying information about Khoded's services and capabilities
 * 2. Showcasing company values and expertise
 * 3. Providing clear paths to consultation requests
 * 
 * Features:
 * - Responsive design with mobile-first approach
 * - Accessible content structure
 * - Brand-consistent visual design
 * - Performance optimized loading
 * - SEO-friendly content structure
 * 
 * @since 2.0.0 (Brand Redesign Implementation)
 */

/**
 * Company data models based on brand analysis
 */
data class CompanyInfo(
    val name: String = "Khoded",
    val tagline: String = "Perform natively, scale effortlessly",
    val description: String = "Cross Platform Development & Mobile App Development specialists",
    val services: List<ServiceInfo> = defaultServices,
    val values: List<CompanyValue> = defaultValues,
    val stats: List<CompanyStat> = defaultStats
)

data class ServiceInfo(
    val id: String,
    val title: String,
    val description: String,
    val features: List<String>,
    val icon: String = "star", // FontAwesome icon name
    val primaryColor: CSSColorValue = KhodedDesignSystem.colors.primary
)

data class CompanyValue(
    val title: String,
    val description: String,
    val icon: String = "star"
)

data class CompanyStat(
    val label: String,
    val value: String,
    val description: String
)

/**
 * Default company data based on brand analysis from redesign materials
 */
private val defaultServices = listOf(
    ServiceInfo(
        id = "code-transformation",
        title = "Code-Base Transformation",
        description = "Transfer existing code to multiplatform solutions for maximum reach and efficiency",
        features = listOf(
            "Legacy system modernization",
            "Cross-platform migration",
            "Performance optimization",
            "Code consolidation"
        ),
        primaryColor = KhodedDesignSystem.colors.primary
    ),
    ServiceInfo(
        id = "ready-solutions",
        title = "Ready-Made Solutions",
        description = "Plug & play dashboards and tools designed for immediate business impact",
        features = listOf(
            "Pre-built dashboard templates",
            "Business intelligence tools",
            "Analytics platforms",
            "Instant deployment"
        ),
        primaryColor = KhodedDesignSystem.colors.secondary
    ),
    ServiceInfo(
        id = "custom-development",
        title = "Custom Development",
        description = "Tools, apps, and MVPs built from scratch to meet your unique requirements",
        features = listOf(
            "Custom mobile applications",
            "Web platform development",
            "MVP prototyping",
            "Scalable architecture"
        ),
        primaryColor = KhodedDesignSystem.colors.interactive
    )
)

private val defaultValues = listOf(
    CompanyValue(
        title = "Efficiency",
        description = "We optimize every aspect of development to deliver results faster and more cost-effectively"
    ),
    CompanyValue(
        title = "Consistency",
        description = "Our systematic approach ensures reliable, maintainable code across all platforms"
    ),
    CompanyValue(
        title = "Innovation",
        description = "We leverage cutting-edge technologies to solve complex business challenges"
    ),
    CompanyValue(
        title = "Scalability",
        description = "Every solution we build is designed to grow with your business needs"
    )
)

private val defaultStats = listOf(
    CompanyStat(
        label = "Projects Delivered",
        value = "50+",
        description = "Successfully completed projects across various industries"
    ),
    CompanyStat(
        label = "Client Satisfaction",
        value = "98%",
        description = "Client satisfaction rate based on project deliveries"
    ),
    CompanyStat(
        label = "Code Reusability",
        value = "75%",
        description = "Average code reusability across multiplatform projects"
    ),
    CompanyStat(
        label = "Time to Market",
        value = "40%",
        description = "Faster time to market with our cross-platform approach"
    )
)

/**
 * Main company information display component
 */
@Composable
fun CompanyInformationDisplay(
    companyInfo: CompanyInfo = CompanyInfo(),
    onRequestConsultation: () -> Unit = {},
    onLearnMore: (serviceId: String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(KhodedDesignSystem.spacing.sectionSpacing)
    ) {
        // Hero section
        CompanyHeroSection(
            name = companyInfo.name,
            tagline = companyInfo.tagline,
            description = companyInfo.description,
            onRequestConsultation = onRequestConsultation
        )
        
        // Services section
        ServicesSection(
            services = companyInfo.services,
            onLearnMore = onLearnMore,
            onRequestConsultation = onRequestConsultation
        )
        
        // Company values section
        ValuesSection(values = companyInfo.values)
        
        // Stats section
        StatsSection(stats = companyInfo.stats)
        
        // Call to action section
        CallToActionSection(onRequestConsultation = onRequestConsultation)
    }
}

/**
 * Hero section with company introduction
 */
@Composable
private fun CompanyHeroSection(
    name: String,
    tagline: String,
    description: String,
    onRequestConsultation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(KhodedDesignSystem.spacing.xl2)
            .textAlign(TextAlign.Center)
            .gap(KhodedDesignSystem.spacing.space6),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Company logo/name
        SpanText(
            text = name,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.displayLarge)
                .fontWeight(KhodedDesignSystem.typography.fontWeightBold)
                .color(KhodedDesignSystem.colors.primary)
        )
        
        // Tagline
        SpanText(
            text = tagline,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.headingLarge)
                .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                .color(KhodedDesignSystem.colors.textPrimary)
                .maxWidth(600.px)
        )
        
        // Description
        SpanText(
            text = description,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.bodyLarge)
                .color(KhodedDesignSystem.colors.textSecondary)
                .lineHeight(KhodedDesignSystem.typography.lineHeightRelaxed)
                .maxWidth(700.px)
        )
        
        // CTA Button
        Button(
            onClick = { onRequestConsultation() },
            modifier = Modifier
                .backgroundColor(KhodedDesignSystem.colors.primary)
                .color(KhodedDesignSystem.colors.textInverse)
                .padding(KhodedDesignSystem.spacing.xl, KhodedDesignSystem.spacing.md)
                .borderRadius(8.px)
                .fontSize(KhodedDesignSystem.typography.bodyLarge)
                .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                .minWidth(200.px)
                .minHeight(KhodedDesignSystem.spacing.touchTargetLarge)
                .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 12.px, color = rgba(0, 0, 0, 0.1))
// Note: transition will be added when CSS API stabilizes
        ) {
            SpanText("Request Consultation")
        }
    }
}

/**
 * Services section showcasing the three main services
 */
@Composable
private fun ServicesSection(
    services: List<ServiceInfo>,
    onLearnMore: (String) -> Unit,
    onRequestConsultation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(KhodedDesignSystem.spacing.xl2)
            .gap(KhodedDesignSystem.spacing.space8)
    ) {
        // Section header
        Column(
            modifier = Modifier.fillMaxWidth().textAlign(TextAlign.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText(
                text = "Our Services",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.displayMedium)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightBold)
                    .color(KhodedDesignSystem.colors.textPrimary)
            )
            SpanText(
                text = "Comprehensive solutions for your development needs",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodyLarge)
                    .color(KhodedDesignSystem.colors.textSecondary)
                    .margin(top = KhodedDesignSystem.spacing.lg)
            )
        }
        
        // Services grid (responsive)
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .toAttrs {
                    style {
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "repeat(auto-fit, minmax(320px, 1fr))")
                        property("gap", KhodedDesignSystem.spacing.space6)
                        property("margin-top", KhodedDesignSystem.spacing.space8)
                    }
                }
        ) {
            services.forEach { service ->
                ServiceCard(
                    service = service,
                    onLearnMore = { onLearnMore(service.id) },
                    onRequestConsultation = onRequestConsultation
                )
            }
        }
    }
}

/**
 * Individual service card component
 */
@Composable
private fun ServiceCard(
    service: ServiceInfo,
    onLearnMore: () -> Unit,
    onRequestConsultation: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.surface)
            .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.borderSecondary)
            .borderRadius(8.px)
            .padding(KhodedDesignSystem.spacing.xl2)
            .boxShadow(offsetX = 0.px, offsetY = 2.px, blurRadius = 8.px, color = rgba(0, 0, 0, 0.1))
            .gap(KhodedDesignSystem.spacing.space4)
// Note: transition will be added when CSS API stabilizes
    ) {
        // Service icon and title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(KhodedDesignSystem.spacing.space3)
        ) {
            FaStar(
                modifier = Modifier
                    .fontSize(32.px)
                    .color(service.primaryColor)
            )
            SpanText(
                text = service.title,
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.headingMedium)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightSemiBold)
                    .color(KhodedDesignSystem.colors.textPrimary)
            )
        }
        
        // Service description
        SpanText(
            text = service.description,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .color(KhodedDesignSystem.colors.textSecondary)
                .lineHeight(KhodedDesignSystem.typography.lineHeightNormal)
        )
        
        // Features list
        Column(
            modifier = Modifier.gap(KhodedDesignSystem.spacing.space2)
        ) {
            service.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.gap(KhodedDesignSystem.spacing.space2)
                ) {
                    Div(
                        attrs = Modifier
                            .size(6.px)
                            .backgroundColor(service.primaryColor)
                            .borderRadius(50.percent)
                            .toAttrs()
                    )
                    SpanText(
                        text = feature,
                        modifier = Modifier
                            .fontSize(KhodedDesignSystem.typography.bodySmall)
                            .color(KhodedDesignSystem.colors.textSecondary)
                    )
                }
            }
        }
        
        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .margin(top = KhodedDesignSystem.spacing.space4)
                .gap(KhodedDesignSystem.spacing.space3),
            horizontalArrangement = Arrangement.spacedBy(KhodedDesignSystem.spacing.md)
        ) {
            Button(
                onClick = { onLearnMore() },
                modifier = Modifier
                    .backgroundColor(Color.transparent)
                    .color(service.primaryColor)
                    .border(1.px, LineStyle.Solid, service.primaryColor)
                    .padding(KhodedDesignSystem.spacing.md, KhodedDesignSystem.spacing.sm)
                    .borderRadius(6.px)
                    .fontSize(KhodedDesignSystem.typography.bodySmall)
                    .flexGrow(1)
                    .minHeight(KhodedDesignSystem.spacing.touchTargetMin)
            ) {
                SpanText("Learn More")
            }
            
            Button(
                onClick = { onRequestConsultation() },
                modifier = Modifier
                    .backgroundColor(service.primaryColor)
                    .color(KhodedDesignSystem.colors.textInverse)
                    .padding(KhodedDesignSystem.spacing.md, KhodedDesignSystem.spacing.sm)
                    .borderRadius(6.px)
                    .fontSize(KhodedDesignSystem.typography.bodySmall)
                    .flexGrow(1)
                    .minHeight(KhodedDesignSystem.spacing.touchTargetMin)
            ) {
                SpanText("Get Started")
            }
        }
    }
}

/**
 * Company values section
 */
@Composable
private fun ValuesSection(values: List<CompanyValue>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(KhodedDesignSystem.spacing.xl2)
            .gap(KhodedDesignSystem.spacing.space8)
    ) {
        // Section header
        Column(
            modifier = Modifier.fillMaxWidth().textAlign(TextAlign.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText(
                text = "Our Values",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.displayMedium)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightBold)
                    .color(KhodedDesignSystem.colors.textPrimary)
            )
            SpanText(
                text = "The principles that guide everything we do",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodyLarge)
                    .color(KhodedDesignSystem.colors.textSecondary)
                    .margin(top = KhodedDesignSystem.spacing.lg)
            )
        }
        
        // Values grid
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .toAttrs {
                    style {
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "repeat(auto-fit, minmax(280px, 1fr))")
                        property("gap", KhodedDesignSystem.spacing.space6)
                        property("margin-top", KhodedDesignSystem.spacing.space8)
                    }
                }
        ) {
            values.forEach { value ->
                ValueCard(value = value)
            }
        }
    }
}

/**
 * Individual value card component
 */
@Composable
private fun ValueCard(value: CompanyValue) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.backgroundSecondary)
            .borderRadius(8.px)
            .padding(KhodedDesignSystem.spacing.xl)
            .gap(KhodedDesignSystem.spacing.space3),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FaStar(
            modifier = Modifier
                .fontSize(40.px)
                .color(KhodedDesignSystem.colors.primary)
        )
        
        SpanText(
            text = value.title,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.headingSmall)
                .fontWeight(KhodedDesignSystem.typography.fontWeightSemiBold)
                .color(KhodedDesignSystem.colors.textPrimary)
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            text = value.description,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .color(KhodedDesignSystem.colors.textSecondary)
                .lineHeight(KhodedDesignSystem.typography.lineHeightNormal)
                .textAlign(TextAlign.Center)
        )
    }
}

/**
 * Statistics section
 */
@Composable
private fun StatsSection(stats: List<CompanyStat>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.primary)
            .padding(KhodedDesignSystem.spacing.xl3)
            .gap(KhodedDesignSystem.spacing.space8)
    ) {
        // Section header
        Column(
            modifier = Modifier.fillMaxWidth().textAlign(TextAlign.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText(
                text = "Our Impact",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.displayMedium)
                    .fontWeight(KhodedDesignSystem.typography.fontWeightBold)
                    .color(KhodedDesignSystem.colors.textInverse)
            )
            SpanText(
                text = "Numbers that speak to our success",
                modifier = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodyLarge)
                    .color(KhodedDesignSystem.colors.textInverse)
                    .opacity(0.9)
                    .margin(top = KhodedDesignSystem.spacing.lg)
            )
        }
        
        // Stats grid
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .toAttrs {
                    style {
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "repeat(auto-fit, minmax(200px, 1fr))")
                        property("gap", KhodedDesignSystem.spacing.space8)
                        property("margin-top", KhodedDesignSystem.spacing.space8)
                    }
                }
        ) {
            stats.forEach { stat ->
                StatCard(stat = stat)
            }
        }
    }
}

/**
 * Individual stat card component
 */
@Composable
private fun StatCard(stat: CompanyStat) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(KhodedDesignSystem.spacing.space2),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            text = stat.value,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.displayLarge)
                .fontWeight(KhodedDesignSystem.typography.fontWeightBold)
                .color(KhodedDesignSystem.colors.textInverse)
        )
        
        SpanText(
            text = stat.label,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.headingSmall)
                .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                .color(KhodedDesignSystem.colors.textInverse)
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            text = stat.description,
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.bodySmall)
                .color(KhodedDesignSystem.colors.textInverse)
                .opacity(0.8)
                .textAlign(TextAlign.Center)
                .lineHeight(KhodedDesignSystem.typography.lineHeightNormal)
        )
    }
}

/**
 * Call to action section
 */
@Composable
private fun CallToActionSection(onRequestConsultation: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(KhodedDesignSystem.colors.backgroundSecondary)
            .padding(KhodedDesignSystem.spacing.xl3)
            .gap(KhodedDesignSystem.spacing.space6),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            text = "Ready to Transform Your Ideas?",
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.displayMedium)
                .fontWeight(KhodedDesignSystem.typography.fontWeightBold)
                .color(KhodedDesignSystem.colors.textPrimary)
                .textAlign(TextAlign.Center)
        )
        
        SpanText(
            text = "Let's discuss how we can help you achieve your development goals with our proven expertise.",
            modifier = Modifier
                .fontSize(KhodedDesignSystem.typography.bodyLarge)
                .color(KhodedDesignSystem.colors.textSecondary)
                .textAlign(TextAlign.Center)
                .maxWidth(600.px)
                .lineHeight(KhodedDesignSystem.typography.lineHeightRelaxed)
        )
        
        Button(
            onClick = { onRequestConsultation() },
            modifier = Modifier
                .backgroundColor(KhodedDesignSystem.colors.primary)
                .color(KhodedDesignSystem.colors.textInverse)
                .padding(KhodedDesignSystem.spacing.xl2, KhodedDesignSystem.spacing.lg)
                .borderRadius(8.px)
                .fontSize(KhodedDesignSystem.typography.bodyLarge)
                .fontWeight(KhodedDesignSystem.typography.fontWeightMedium)
                .minWidth(250.px)
                .minHeight(KhodedDesignSystem.spacing.touchTargetLarge)
                .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 16.px, color = rgba(0, 0, 0, 0.15))
// Note: transition will be added when CSS API stabilizes
        ) {
            SpanText("Start Your Project Today")
        }
    }
}