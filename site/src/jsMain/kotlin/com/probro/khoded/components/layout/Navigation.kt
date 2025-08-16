// Navigation.kt - Simplified Header and Navigation components  
package com.probro.khoded.components.layout

import androidx.compose.runtime.*
import com.probro.khoded.design.KhodedDesignSystem
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.clamp
import com.varabyte.kobweb.silk.components.navigation.Link
import org.jetbrains.compose.web.css.*
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*
import org.w3c.dom.HTMLElement
import kotlinx.browser.window
import kotlinx.browser.document

// =============================================================================
// HEADER COMPONENT - Simplified Implementation
// =============================================================================

/**
 * Skip Navigation Component - WCAG 2.4.1 Level A Compliance
 * Allows keyboard and screen reader users to bypass navigation
 */  
@Composable
private fun SkipNavigation() {
    A(
        href = "#main-content",
        attrs = Modifier
            .position(Position.Absolute)
            .left((-10000).px)
            .top(0.px)
            .width(1.px)
            .height(1.px)
            .overflow(Overflow.Hidden)
            .toAttrs {
                attr("aria-label", "Skip to main content")
                style {
                    property("&:focus", """
                        left: 6px !important;
                        top: 7px !important;
                        width: auto !important;
                        height: auto !important;
                        padding: 8px !important;
                        background: white !important;
                        color: black !important;
                        z-index: 999999 !important;
                        text-decoration: none !important;
                        border-radius: 4px !important;
                        border: 2px solid #0066cc !important;
                        font-weight: bold !important;
                    """)
                }
            }
    ) {
        Text("Skip to main content")
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    transparent: Boolean = false
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    var isScrolled by remember { mutableStateOf(false) }
    
    // Track scroll position for header styling
    LaunchedEffect(Unit) {
        val handleScroll = { _: dynamic ->
            isScrolled = window.pageYOffset > 50
        }
        
        window.addEventListener("scroll", handleScroll)
    }
    
    // Add skip navigation first (for screen readers and keyboard users)
    SkipNavigation()
    
    Header(
        attrs = modifier
            .fillMaxWidth()
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
            .right(0.px)
            .zIndex(1000)
            .backgroundColor(
                when {
                    transparent && !isScrolled -> rgba(255, 255, 255, 0.1)
                    transparent && isScrolled -> rgba(255, 255, 255, 0.95)
                    else -> Color.white
                }
            )
            .attrsModifier {
                style {
                    property("backdrop-filter", if (transparent) "blur(10px)" else "none")
                    property("border-bottom", if (!transparent || isScrolled) "1px solid #e5e7eb" else "none")
                    property("transition", "all 0.3s ease")
                }
            }
            .toAttrs {
                attr("role", "banner")
            }
    ) {
        SimpleContainer {
            SimpleRow {
                Logo(transparent = transparent && !isScrolled)
                DesktopNavigation(transparent = transparent && !isScrolled)
                MobileMenuButton(
                    isOpen = isMenuOpen,
                    onClick = { isMenuOpen = !isMenuOpen },
                    transparent = transparent && !isScrolled
                )
            }
        }
        
        if (isMenuOpen) {
            MobileNavigationMenu(
                onClose = { isMenuOpen = false }
            )
        }
    }
}

@Composable
private fun Logo(transparent: Boolean = false) {
    Link(
        "/"
    ) {
        Div(
            attrs = Modifier
                .attrsModifier {
                    style {
                        property("display", "flex")
                        property("align-items", "center")
                        property("gap", "12px")
                        property("text-decoration", "none")
                    }
                }
                .toAttrs {
                    attr("aria-label", "Khoded home page")
                }
        ) {
            // UPDATED LOGO - Consistent with Landing.kt branding
            Div(
                attrs = Modifier
                    .size(32.px, 26.px)  // Proportional to Landing.kt logo
                    .backgroundColor(KhodedDesignSystem.colors.primary)  // Modern teal
                    .borderRadius(6.px)  // Slightly more rounded
                    .attrsModifier {
                        style {
                            property("display", "flex")
                            property("align-items", "center")
                            property("justify-content", "center")
                            property("color", "white")
                            property("font-size", "14px")  // Adjusted for nav bar
                            property("font-weight", "700")
                            property("font-family", "'Fira Code', 'Consolas', 'Monaco', monospace")  // Landing.kt font
                        }
                    }
                    .toAttrs()
            ) {
                org.jetbrains.compose.web.dom.Text("</>")  // Consistent with Landing.kt
            }
            
            Span(
                attrs = Modifier
                    .fontSize(KhodedDesignSystem.typography.headingLarge)
                    .fontWeight(700)
                    .color(
                        if (transparent) Color.white 
                        else KhodedDesignSystem.colors.textPrimary
                    )
                    .toAttrs()
            ) {
                org.jetbrains.compose.web.dom.Text("Khoded")
            }
        }
    }
}

@Composable
private fun DesktopNavigation(
    transparent: Boolean = false
) {
    Nav(
        attrs = Modifier
            .attrsModifier {
                style {
                    property("display", "none")
                    property("@media (min-width: 1024px)", "{ display: flex; }")
                }
            }
            .toAttrs {
                attr("role", "navigation")
                attr("aria-label", "Main navigation")
            }
    ) {
        Div(
            attrs = Modifier
                .attrsModifier {
                    style {
                        property("display", "flex")
                        property("gap", "32px")
                        property("align-items", "center")
                    }
                }
                .toAttrs()
        ) {
            NavigationLink(
                text = "Services",
                href = "/services",
                transparent = transparent
            )
            
            NavigationLink(
                text = "About",
                href = "/about",
                transparent = transparent
            )
            
            NavigationLink(
                text = "Contact",
                href = "/contact",
                transparent = transparent
            )
            
            NavigationLink(
                text = "Join Team",
                href = "/join-team",
                transparent = transparent
            )
        }
    }
}

@Composable
private fun NavigationLink(
    text: String,
    href: String,
    transparent: Boolean = false
) {
    val isActive = remember(href) {
        window.location.pathname == href
    }
    
    Link(
        href
    ) {
        Span(
            attrs = Modifier
                .padding(8.px, 16.px)
                .fontSize(KhodedDesignSystem.typography.bodyMedium)
                .fontWeight(if (isActive) 600 else 500)
                .color(
                    when {
                        isActive && transparent -> Color.white
                        isActive -> KhodedDesignSystem.colors.primaryHover
                        transparent -> rgba(255, 255, 255, 0.9)
                        else -> KhodedDesignSystem.colors.textSecondary
                    }
                )
                .borderRadius(6.px)
                .attrsModifier {
                    style {
                        property("text-decoration", "none")
                        property("transition", "all 0.2s ease")
                        property("cursor", "pointer")
                    }
                }
                .toAttrs {
                    // WCAG 2.2 AA compliant focus states - handled by CSS
                    onFocus { event -> /* Focus styling handled by CSS :focus pseudo-class */ }
                    onBlur { event -> /* Blur styling handled by CSS */ }
                    onMouseOver { event -> /* Hover styling handled by CSS :hover pseudo-class */ }
                    onMouseOut { event -> /* Mouse out styling handled by CSS */ }
                }
        ) {
            org.jetbrains.compose.web.dom.Text(text)
        }
    }
}

@Composable
private fun MobileMenuButton(
    isOpen: Boolean,
    onClick: () -> Unit,
    transparent: Boolean = false
) {
    Button(
        attrs = Modifier
            .size(44.px)
            .backgroundColor(Color.transparent)
            .border(0.px)
            .borderRadius(6.px)
            .color(
                if (transparent) Color.white 
                else KhodedDesignSystem.colors.textPrimary
            )
            .attrsModifier {
                style {
                    property("cursor", "pointer")
                    property("display", "flex")
                    property("align-items", "center")
                    property("justify-content", "center")
                    property("transition", "all 0.2s ease")
                    property("@media (min-width: 1024px)", "{ display: none; }")
                }
            }
            .toAttrs {
                attr("aria-label", if (isOpen) "Close menu" else "Open menu")
                attr("aria-expanded", isOpen.toString())
                attr("aria-controls", "mobile-navigation")
                onClick { onClick() }
                // WCAG 2.2 AA compliant focus states - handled by CSS
                onFocus { event -> /* Focus styling handled by CSS :focus pseudo-class */ }
                onBlur { event -> /* Blur styling handled by CSS */ }
                onMouseOver { event -> /* Hover styling handled by CSS :hover pseudo-class */ }
                onMouseOut { event -> /* Mouse out styling handled by CSS */ }
            }
    ) {
        Div(
            attrs = Modifier
                .size(24.px)
                .position(Position.Relative)
                .toAttrs()
        ) {
            HamburgerIcon(isOpen = isOpen)
        }
    }
}

@Composable
private fun HamburgerIcon(isOpen: Boolean) {
    repeat(3) { index ->
        Div(
            attrs = Modifier
                .position(Position.Absolute)
                .width(20.px)
                .height(2.px)
                .backgroundColor(Color.currentColor)
                .borderRadius(1.px)
                .left(2.px)
                .top((6 + index * 6).px)
                .attrsModifier {
                    style {
                        property("transition", "all 0.3s ease")
                        when {
                            isOpen && index == 0 -> {
                                property("transform", "translateY(6px) rotate(45deg)")
                            }
                            isOpen && index == 1 -> {
                                property("transform", "scale(0)")
                            }
                            isOpen && index == 2 -> {
                                property("transform", "translateY(-6px) rotate(-45deg)")
                            }
                            else -> {
                                property("transform", "translateY(0) rotate(0)")
                            }
                        }
                    }
                }
                .toAttrs()
        )
    }
}

@Composable
private fun MobileNavigationMenu(
    onClose: () -> Unit
) {
    // Overlay
    Div(
        attrs = Modifier
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
            .right(0.px)
            .bottom(0.px)
            .backgroundColor(rgba(0, 0, 0, 0.5))
            .zIndex(999)
            .toAttrs {
                onClick { onClose() }
            }
    )
    
    // Menu panel
    Div(
        attrs = Modifier
            .id("mobile-navigation")
            .position(Position.Fixed)
            .top(0.px)
            .right(0.px)
            .bottom(0.px)
            .width(280.px)
            .backgroundColor(Color.white)
            .zIndex(1000)
            .attrsModifier {
                style {
                    property("box-shadow", "0 20px 25px -5px rgba(0, 0, 0, 0.1)")
                    property("animation", "slideInRight 0.3s ease-out")
                }
            }
            .toAttrs {
                attr("role", "dialog")
                attr("aria-label", "Mobile navigation menu")
                onClick { event -> event.stopPropagation() }
            }
    ) {
        SimpleColumn {
            // Header
            SimpleRow {
                H2(
                    attrs = Modifier
                        .fontSize(KhodedDesignSystem.typography.headingMedium)
                        .fontWeight(600)
                        .color(KhodedDesignSystem.colors.textPrimary)
                        .margin(0.px)
                        .toAttrs()
                ) {
                    org.jetbrains.compose.web.dom.Text("Menu")
                }
                
                Button(
                    attrs = Modifier
                        .size(KhodedDesignSystem.touchTargets.minimum)
                        .backgroundColor(Color.transparent)
                        .border(0.px)
                        .borderRadius(6.px)
                        .color(KhodedDesignSystem.colors.textSecondary)
                        .attrsModifier {
                            style {
                                property("cursor", "pointer")
                            }
                        }
                        .toAttrs {
                            attr("aria-label", "Close menu")
                            onClick { onClose() }
                            // WCAG 2.2 AA compliant focus states - handled by CSS
                            onFocus { event -> /* Focus styling handled by CSS :focus pseudo-class */ }
                            onBlur { event -> /* Blur styling handled by CSS */ }
                            onMouseOver { event -> /* Hover styling handled by CSS :hover pseudo-class */ }
                            onMouseOut { event -> /* Mouse out styling handled by CSS */ }
                        }
                ) {
                    org.jetbrains.compose.web.dom.Text("✕")
                }
            }
            
            // Navigation links
            SimpleColumn {
                MobileNavigationLink("Home", "/", "🏠", onClose)
                MobileNavigationLink("Services", "/services", "⚙️", onClose)
                MobileNavigationLink("About", "/about", "👥", onClose)
                MobileNavigationLink("Contact", "/contact", "📞", onClose)
                MobileNavigationLink("Join Team", "/join-team", "💼", onClose)
            }
            
            // CTA Section
            Div(
                attrs = Modifier
                    .fillMaxWidth()
                    .padding(16.px)
                    .backgroundColor(KhodedDesignSystem.colors.primaryLight)
                    .borderRadius(12.px)
                    .border(1.px, LineStyle.Solid, KhodedDesignSystem.colors.borderSecondary)
                    .attrsModifier {
                        style {
                            property("margin-top", "auto")
                            property("text-align", "center")
                        }
                    }
                    .toAttrs()
            ) {
                P(
                    attrs = Modifier
                        .fontSize(KhodedDesignSystem.typography.bodyMedium)
                        .fontWeight(600)
                        .color(KhodedDesignSystem.colors.primaryActive)
                        .margin(0.px, 0.px, 16.px, 0.px)
                        .toAttrs()
                ) {
                    org.jetbrains.compose.web.dom.Text("Ready to get started?")
                }
                
                KhodedButton(
                    text = "Free Consultation",
                    onClick = {
                        onClose()
                        scrollToSection("contact")
                    },
                    variant = ButtonVariant.Primary,
                    size = ButtonSize.Medium,
                    fullWidth = true
                )
            }
        }
    }
}

@Composable
private fun MobileNavigationLink(
    text: String,
    href: String,
    icon: String,
    onClick: () -> Unit
) {
    Link(
        href
    ) {
        Div(
            attrs = Modifier
                .fillMaxWidth()
                .padding(16.px)
                .borderRadius(8.px)
                .attrsModifier {
                    style {
                        property("text-decoration", "none")
                        property("transition", "all 0.2s ease")
                        property("display", "flex")
                        property("align-items", "center")
                        property("gap", "16px")
                        property("cursor", "pointer")
                    }
                }
                .toAttrs {
                    onClick { onClick() }
                    // WCAG 2.2 AA compliant focus states - handled by CSS
                    onFocus { event -> /* Focus styling handled by CSS :focus pseudo-class */ }
                    onBlur { event -> /* Blur styling handled by CSS */ }
                    onMouseOver { event -> /* Hover styling handled by CSS :hover pseudo-class */ }
                    onMouseOut { event -> /* Mouse out styling handled by CSS */ }
                }
        ) {
            Span(
                attrs = Modifier
                    .fontSize(20.px)
                    .width(24.px)
                    .attrsModifier {
                        style {
                            property("text-align", "center")
                        }
                    }
                    .toAttrs()
            ) {
                org.jetbrains.compose.web.dom.Text(icon)
            }
            
            Span(
                attrs = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodyLarge)
                    .fontWeight(500)
                    .color(KhodedDesignSystem.colors.textPrimary)
                    .attrsModifier {
                        style {
                            property("flex-grow", "1")
                        }
                    }
                    .toAttrs()
            ) {
                org.jetbrains.compose.web.dom.Text(text)
            }
            
            Span(
                attrs = Modifier
                    .fontSize(KhodedDesignSystem.typography.bodyMedium)
                    .color(KhodedDesignSystem.colors.textSecondary)
                    .toAttrs()
            ) {
                org.jetbrains.compose.web.dom.Text("→")
            }
        }
    }
}

// =============================================================================
// BUTTON COMPONENT PLACEHOLDER - REPLACE WITH ACTUAL IMPLEMENTATION
// =============================================================================

enum class ButtonVariant { Primary, Ghost }
enum class ButtonSize { Medium }

@Composable
private fun KhodedButton(
    text: String,
    onClick: () -> Unit,
    variant: ButtonVariant,
    size: ButtonSize,
    fullWidth: Boolean = false
) {
    org.jetbrains.compose.web.dom.Button(
        attrs = Modifier
            .padding(18.px, 36.px)  // Landing.kt button padding
            .backgroundColor(
                when (variant) {
                    ButtonVariant.Primary -> KhodedDesignSystem.colors.primary  // Modern teal
                    ButtonVariant.Ghost -> Color.transparent
                }
            )
            .color(
                when (variant) {
                    ButtonVariant.Primary -> Color.white
                    ButtonVariant.Ghost -> KhodedDesignSystem.colors.primary
                }
            )
            .borderRadius(12.px)  // Landing.kt button border radius
            .border(
                if (variant == ButtonVariant.Ghost) 2.px else 0.px,
                LineStyle.Solid,
                when (variant) {
                    ButtonVariant.Ghost -> rgba(255, 255, 255, 0.4)  // Landing.kt ghost button border
                    else -> KhodedDesignSystem.colors.primary
                }
            )
            .fontSize(clamp(14.px, 3.vw, 18.px))  // Responsive using Kobweb clamp function
            .fontWeight(600)
            .minHeight(56.px)  // Landing.kt button height
            .attrsModifier {
                style {
                    property("cursor", "pointer")
                    property("transition", "all 0.3s ease")  // Landing.kt transition
                    property("text-shadow", "0 1px 4px rgba(0, 0, 0, 0.2)")  // Landing.kt text shadow
                    property("box-shadow", 
                        when (variant) {
                            ButtonVariant.Primary -> "0 4px 12px rgba(6, 182, 212, 0.3)"  // Landing.kt glow
                            else -> "none"
                        }
                    )
                }
            }
            .apply {
                if (fullWidth) fillMaxWidth() else this
            }
            .toAttrs {
                onClick { onClick() }
                // Enhanced focus states from Landing.kt - handled by CSS
                onFocus { event -> /* Focus styling handled by CSS :focus pseudo-class */ }
                onBlur { event -> /* Blur styling handled by CSS */ }
                onMouseOver { event -> /* Hover styling handled by CSS :hover pseudo-class */ }
                onMouseOut { event -> /* Mouse out styling handled by CSS */ }
            }
    ) {
        Text(text)
    }
}

// =============================================================================
// UTILITY FUNCTIONS
// =============================================================================

private fun scrollToSection(sectionId: String) {
    val element = kotlinx.browser.document.getElementById(sectionId)
    element?.scrollIntoView(
        kotlin.js.json(
            "behavior" to "smooth",
            "block" to "start"
        )
    )
}

private fun getCurrentYear(): String {
    return js("new Date().getFullYear()").toString()
}

// =============================================================================
// SIMPLE LAYOUT COMPONENTS
// =============================================================================

@Composable
fun SimpleContainer(content: @Composable () -> Unit) {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .attrsModifier {
                style {
                    property("padding", "0 24px")
                }
            }
            .toAttrs()
    ) {
        content()
    }
}

@Composable
fun SimpleRow(content: @Composable () -> Unit) {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .attrsModifier {
                style {
                    property("display", "flex")
                    property("align-items", "center")
                    property("justify-content", "space-between")
                    property("padding", "16px 0")
                    property("min-height", "64px")
                }
            }
            .toAttrs()
    ) {
        content()
    }
}

@Composable
fun SimpleColumn(content: @Composable () -> Unit) {
    Div(
        attrs = Modifier
            .fillMaxSize()
            .attrsModifier {
                style {
                    property("display", "flex")
                    property("flex-direction", "column")
                    property("padding", "32px")
                    property("gap", "24px")
                }
            }
            .toAttrs()
    ) {
        content()
    }
}

// Note: slideInRight animation is now handled by KhodedAnimations.kt