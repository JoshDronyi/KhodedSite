// Navigation.kt - Header, Footer, and Navigation components
package com.probro.khoded.components.layout

import androidx.compose.runtime.*
import com.probro.khoded.styles.KhodedColors
import com.probro.khoded.styles.KhodedSpacing
import com.probro.khoded.styles.KhodedTypography
import com.probro.khoded.styles.KhodedRadius
import com.probro.khoded.styles.KhodedAnimations
import com.probro.khoded.styles.KhodedShadows
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.*
import com.varabyte.kobweb.silk.components.layout.*
import com.varabyte.kobweb.silk.components.navigation.*
import com.varabyte.kobweb.silk.components.style.*
import com.varabyte.kobweb.silk.components.text.*
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*

// =============================================================================
// HEADER COMPONENT
// =============================================================================

@Composable
fun Header(
    modifier: Modifier = Modifier,
    transparent: Boolean = false
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    var isScrolled by remember { mutableStateOf(false) }
    
    // Track scroll position for header styling
    LaunchedEffect(Unit) {
        val handleScroll = {
            isScrolled = kotlinx.browser.window.pageYOffset > 50
        }
        
        kotlinx.browser.window.addEventListener("scroll", handleScroll)
        // Cleanup handled by Compose
    }
    
    org.jetbrains.compose.web.dom.Header(
        attrs = modifier
            .fillMaxWidth()
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
            .right(0.px)
            .zIndex(1000)
            .background(
                when {
                    transparent && !isScrolled -> "rgba(255, 255, 255, 0.1)"
                    transparent && isScrolled -> "rgba(255, 255, 255, 0.95)"
                    else -> Color.white.toString()
                }
            )
            .backdropFilter(if (transparent) "blur(10px)" else "none")
            .borderBottom(
                if (!transparent || isScrolled) 1.px else 0.px,
                LineStyle.Solid,
                KhodedColors.Gray200
            )
            .transition(CSSTransition("all", KhodedAnimations.normal))
            .toAttrs {
                attr("role", "banner")
            }
    ) {
        Container {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .minHeight(KhodedSpacing.xl6)
                    .alignItems(AlignItems.Center)
                    .justifyContent(JustifyContent.SpaceBetween)
                    .padding(vertical = KhodedSpacing.lg)
            ) {
                // Logo
                Logo(transparent = transparent && !isScrolled)
                
                // Desktop Navigation
                DesktopNavigation(
                    modifier = Modifier
                        .display(DisplayStyle.None)
                        .breakpoint(Breakpoint.LG) {
                            display(DisplayStyle.Flex)
                        },
                    transparent = transparent && !isScrolled
                )
                
                // Desktop CTA Button
                Row(
                    modifier = Modifier
                        .gap(KhodedSpacing.md)
                        .display(DisplayStyle.None)
                        .breakpoint(Breakpoint.LG) {
                            display(DisplayStyle.Flex)
                        }
                ) {
                    KhodedButton(
                        text = "FREE CONSULTATION",
                        onClick = { scrollToSection("contact") },
                        variant = if (transparent && !isScrolled) ButtonVariant.Ghost else ButtonVariant.Primary,
                        size = ButtonSize.Medium
                    )
                }
                
                // Mobile Menu Button
                MobileMenuButton(
                    isOpen = isMenuOpen,
                    onClick = { isMenuOpen = !isMenuOpen },
                    transparent = transparent && !isScrolled,
                    modifier = Modifier
                        .display(DisplayStyle.Flex)
                        .breakpoint(Breakpoint.LG) {
                            display(DisplayStyle.None)
                        }
                )
            }
        }
        
        // Mobile Navigation Menu
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
        href = "/",
        modifier = Modifier
            .textDecorationLine(TextDecorationLine.None)
            .attrsModifier {
                attr("aria-label", "Khoded home page")
            }
    ) {
        Row(
            modifier = Modifier
                .gap(KhodedSpacing.sm)
                .alignItems(AlignItems.Center)
        ) {
            // Logo Icon
            Box(
                modifier = Modifier
                    .size(40.px)
                    .backgroundColor(KhodedColors.Purple500)
                    .borderRadius(KhodedRadius.md)
                    .display(DisplayStyle.Flex)
                    .alignItems(AlignItems.Center)
                    .justifyContent(JustifyContent.Center)
                    .color(Color.white)
                    .fontSize(20.px)
                    .fontWeight(KhodedTypography.bold)
            ) {
                Text("</\\>")
            }
            
            // Logo Text
            Text(
                "Khoded",
                modifier = Modifier
                    .fontSize(KhodedTypography.xl2)
                    .fontWeight(KhodedTypography.bold)
                    .color(
                        if (transparent) Color.white 
                        else KhodedColors.TextPrimary
                    )
                    .letterSpacing((-0.01).em)
            )
        }
    }
}

@Composable
private fun DesktopNavigation(
    modifier: Modifier = Modifier,
    transparent: Boolean = false
) {
    Nav(
        modifier = modifier
            .attrsModifier {
                attr("role", "navigation")
                attr("aria-label", "Main navigation")
            }
    ) {
        Row(
            modifier = Modifier
                .gap(KhodedSpacing.xl2)
                .alignItems(AlignItems.Center)
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
    transparent: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isActive = remember(href) {
        // In a real app, this would check current route
        kotlinx.browser.window.location.pathname == href
    }
    
    Link(
        href = href,
        modifier = modifier
            .padding(KhodedSpacing.sm, KhodedSpacing.md)
            .fontSize(KhodedTypography.base)
            .fontWeight(
                if (isActive) KhodedTypography.semiBold 
                else KhodedTypography.medium
            )
            .color(
                when {
                    isActive && transparent -> Color.white
                    isActive -> KhodedColors.Purple600
                    transparent -> Color.white.copy(alpha = 230) // 90% opacity
                    else -> KhodedColors.TextSecondary
                }
            )
            .textDecorationLine(TextDecorationLine.None)
            .borderRadius(KhodedRadius.sm)
            .transition(CSSTransition("all", KhodedAnimations.fast))
            .hover {
                color(
                    if (transparent) Color.white 
                    else KhodedColors.Purple600
                )
                backgroundColor(
                    if (transparent) Color.white.copy(alpha = 26) // 10% opacity
                    else KhodedColors.Purple50
                )
            }
            .focus {
                outline("2px solid ${KhodedColors.Focus}")
                outlineOffset(2.px)
            }
    ) {
        Text(text)
    }
}

@Composable
private fun MobileMenuButton(
    isOpen: Boolean,
    onClick: () -> Unit,
    transparent: Boolean = false,
    modifier: Modifier = Modifier
) {
    org.jetbrains.compose.web.dom.Button(
        attrs = modifier
            .size(KhodedSpacing.touchTargetMin)
            .backgroundColor(Color.transparent)
            .border(0.px)
            .borderRadius(KhodedRadius.sm)
            .color(
                if (transparent) Color.white 
                else KhodedColors.TextPrimary
            )
            .cursor(Cursor.Pointer)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .transition(CSSTransition("all", KhodedAnimations.fast))
            .hover {
                backgroundColor(
                    if (transparent) Color.white.copy(alpha = 26) // 10% opacity
                    else KhodedColors.Gray100
                )
            }
            .focus {
                outline("2px solid ${KhodedColors.Focus}")
                outlineOffset(2.px)
            }
            .toAttrs {
                attr("aria-label", if (isOpen) "Close menu" else "Open menu")
                attr("aria-expanded", isOpen.toString())
                attr("aria-controls", "mobile-navigation")
                onClick { onClick() }
            }
    ) {
        Box(
            modifier = Modifier
                .size(24.px)
                .position(Position.Relative)
        ) {
            HamburgerIcon(isOpen = isOpen)
        }
    }
}

@Composable
private fun HamburgerIcon(isOpen: Boolean) {
    // Animated hamburger menu icon
    repeat(3) { index ->
        Box(
            modifier = Modifier
                .position(Position.Absolute)
                .width(20.px)
                .height(2.px)
                .backgroundColor(Color.currentcolor)
                .borderRadius(1.px)
                .left(2.px)
                .top((6 + index * 6).px)
                .transition(CSSTransition("all", KhodedAnimations.normal))
                .transform {
                    when {
                        isOpen && index == 0 -> {
                            translateY(6.px)
                            rotate(45.deg)
                        }
                        isOpen && index == 1 -> {
                            scale(0)
                        }
                        isOpen && index == 2 -> {
                            translateY((-6).px)
                            rotate((-45).deg)
                        }
                        else -> {
                            // Default position
                        }
                    }
                }
        )
    }
}

@Composable
private fun MobileNavigationMenu(
    onClose: () -> Unit
) {
    // Overlay
    Box(
        modifier = Modifier
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
            .right(0.px)
            .bottom(0.px)
            .backgroundColor(Color.black.copy(alpha = 128)) // 50% opacity
            .zIndex(999)
            .onClick { onClose() }
    )
    
    // Menu panel
    Box(
        modifier = Modifier
            .id("mobile-navigation")
            .position(Position.Fixed)
            .top(0.px)
            .right(0.px)
            .bottom(0.px)
            .width(280.px)
            .backgroundColor(Color.white)
            .boxShadow(KhodedShadows.xl)
            .zIndex(1000)
            .animation(
                Animation(
                    name = "slideInRight",
                    duration = KhodedAnimations.normal,
                    timingFunction = AnimationTimingFunction.EaseOut
                )
            )
            .attrsModifier {
                attr("role", "dialog")
                attr("aria-label", "Mobile navigation menu")
                onClick { event -> event.stopPropagation() }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(KhodedSpacing.xl2)
                .gap(KhodedSpacing.xl)
        ) {
            // Close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .justifyContent(JustifyContent.SpaceBetween)
                    .alignItems(AlignItems.Center)
                    .marginBottom(KhodedSpacing.xl)
            ) {
                Text(
                    "Menu",
                    modifier = Modifier
                        .fontSize(KhodedTypography.xl)
                        .fontWeight(KhodedTypography.semiBold)
                        .color(KhodedColors.TextPrimary)
                )
                
                org.jetbrains.compose.web.dom.Button(
                    attrs = Modifier
                        .size(32.px)
                        .backgroundColor(Color.transparent)
                        .border(0.px)
                        .borderRadius(KhodedRadius.sm)
                        .color(KhodedColors.TextSecondary)
                        .cursor(Cursor.Pointer)
                        .hover {
                            backgroundColor(KhodedColors.Gray100)
                        }
                        .toAttrs {
                            attr("aria-label", "Close menu")
                            onClick { onClose() }
                        }
                ) {
                    Text("✕")
                }
            }
            
            // Navigation links
            Column(
                modifier = Modifier.gap(KhodedSpacing.lg)
            ) {
                MobileNavigationLink(
                    text = "Home",
                    href = "/",
                    icon = "🏠",
                    onClick = onClose
                )
                
                MobileNavigationLink(
                    text = "Services",
                    href = "/services",
                    icon = "⚙️",
                    onClick = onClose
                )
                
                MobileNavigationLink(
                    text = "About",
                    href = "/about",
                    icon = "👥",
                    onClick = onClose
                )
                
                MobileNavigationLink(
                    text = "Contact",
                    href = "/contact",
                    icon = "📞",
                    onClick = onClose
                )
                
                MobileNavigationLink(
                    text = "Join Team",
                    href = "/join-team",
                    icon = "💼",
                    onClick = onClose
                )
            }
            
            // CTA Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .marginTop(org.jetbrains.compose.web.css.auto)
                    .padding(KhodedSpacing.lg)
                    .backgroundColor(KhodedColors.Purple50)
                    .borderRadius(KhodedRadius.lg)
                    .border(1.px, LineStyle.Solid, KhodedColors.Purple200)
            ) {
                Column(
                    modifier = Modifier
                        .gap(KhodedSpacing.md)
                        .textAlign(TextAlign.Center)
                ) {
                    Text(
                        "Ready to get started?",
                        modifier = Modifier
                            .fontSize(KhodedTypography.base)
                            .fontWeight(KhodedTypography.semiBold)
                            .color(KhodedColors.Purple700)
                    )
                    
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
}

@Composable
private fun MobileNavigationLink(
    text: String,
    href: String,
    icon: String,
    onClick: () -> Unit
) {
    Link(
        href = href,
        modifier = Modifier
            .fillMaxWidth()
            .padding(KhodedSpacing.lg)
            .borderRadius(KhodedRadius.md)
            .textDecorationLine(TextDecorationLine.None)
            .transition(CSSTransition("all", KhodedAnimations.fast))
            .hover {
                backgroundColor(KhodedColors.Gray50)
            }
            .onClick { onClick() }
    ) {
        Row(
            modifier = Modifier
                .gap(KhodedSpacing.md)
                .alignItems(AlignItems.Center)
        ) {
            Text(
                icon,
                modifier = Modifier
                    .fontSize(20.px)
                    .width(24.px)
                    .textAlign(TextAlign.Center)
            )
            
            Text(
                text,
                modifier = Modifier
                    .fontSize(KhodedTypography.lg)
                    .fontWeight(KhodedTypography.medium)
                    .color(KhodedColors.TextPrimary)
            )
            
            Box(modifier = Modifier.flexGrow(1))
            
            Text(
                "→",
                modifier = Modifier
                    .fontSize(16.px)
                    .color(KhodedColors.TextSecondary)
            )
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
            .padding(KhodedSpacing.md)
            .backgroundColor(
                when (variant) {
                    ButtonVariant.Primary -> KhodedColors.Purple500
                    ButtonVariant.Ghost -> Color.transparent
                }
            )
            .color(
                when (variant) {
                    ButtonVariant.Primary -> Color.white
                    ButtonVariant.Ghost -> KhodedColors.Purple500
                }
            )
            .borderRadius(KhodedRadius.md)
            .border(
                if (variant == ButtonVariant.Ghost) 2.px else 0.px,
                LineStyle.Solid,
                KhodedColors.Purple500
            )
            .cursor(Cursor.Pointer)
            .apply {
                if (fullWidth) fillMaxWidth() else this
            }
            .toAttrs {
                onClick { onClick() }
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
    return kotlinx.browser.window.Date().getFullYear().toString()
}