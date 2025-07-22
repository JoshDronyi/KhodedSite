package com.probro.khoded.components.privacy

import androidx.compose.runtime.*
import com.probro.khoded.security.*
import com.probro.khoded.styles.*
import com.probro.khoded.components.accessibility.KhodedAccessibleButton
import com.probro.khoded.components.accessibility.KhodedButtonVariant
import com.probro.khoded.components.accessibility.KhodedButtonSize
import com.probro.khoded.i18n.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

/**
 * Privacy Banner and Cookie Consent Components
 * 
 * GDPR/CCPA compliant privacy controls with:
 * - Granular consent management
 * - Multi-language support
 * - Accessibility compliance
 * - Persistent preferences
 * - Legal compliance features
 */

@Composable
fun PrivacyBannerProvider(
    privacyManager: PrivacyManager = rememberPrivacyManager(),
    content: @Composable () -> Unit
) {
    val preferences by privacyManager.preferences
    var showBanner by remember { mutableStateOf(false) }
    var showPreferences by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        // Show banner if consent is required and not already given
        if (privacyManager.requiresConsent() && preferences.lastUpdated == 0L) {
            delay(1000) // Delay to avoid blocking initial page load
            showBanner = true
        }
    }
    
    content()
    
    // Cookie consent banner
    if (showBanner) {
        CookieConsentBanner(
            onAcceptAll = {
                privacyManager.updatePreferences(
                    PrivacyPreferences(
                        analytics = true,
                        marketing = true,
                        functional = true,
                        performance = true
                    )
                )
                showBanner = false
            },
            onRejectAll = {
                privacyManager.updatePreferences(
                    PrivacyPreferences(
                        analytics = false,
                        marketing = false,
                        functional = true,
                        performance = false
                    )
                )
                showBanner = false
            },
            onCustomize = {
                showPreferences = true
            },
            onDismiss = {
                showBanner = false
            }
        )
    }
    
    // Privacy preferences modal
    if (showPreferences) {
        PrivacyPreferencesModal(
            currentPreferences = preferences,
            onSave = { newPreferences ->
                privacyManager.updatePreferences(newPreferences)
                showPreferences = false
                showBanner = false
            },
            onCancel = {
                showPreferences = false
            }
        )
    }
}

@Composable
private fun CookieConsentBanner(
    onAcceptAll: () -> Unit,
    onRejectAll: () -> Unit,
    onCustomize: () -> Unit,
    onDismiss: () -> Unit
) {
    val i18n = useTranslation()
    
    Div(
        attrs = Modifier
            .position(Position.Fixed)
            .bottom(0.px)
            .left(0.px)
            .right(0.px)
            .zIndex(10000)
            .backgroundColor(KhodedColors.Background)
            .border(1.px, LineStyle.Solid, KhodedColors.Gray200)
            .boxShadow(KhodedShadows.xl)
            .padding(KhodedSpacing.lg)
            .animation(
                Animation(
                    name = "slideInUp",
                    duration = KhodedAnimations.normal,
                    timingFunction = AnimationTimingFunction.EaseOut
                )
            )
            .toAttrs {
                attr("role", "banner")
                attr("aria-label", "Cookie consent banner")
            }
    ) {
        Container {
            Column(
                modifier = Modifier.gap(KhodedSpacing.lg)
            ) {
                // Banner content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gap(KhodedSpacing.lg)
                        .alignItems(AlignItems.Center)
                        .breakpoint(Breakpoint.MD) {
                            gap(KhodedSpacing.xl2)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cookie icon
                    Span(
                        attrs = Modifier
                            .fontSize(32.px)
                            .display(DisplayStyle.None)
                            .breakpoint(Breakpoint.MD) {
                                display(DisplayStyle.Block)
                            }
                            .toAttrs()
                    ) {
                        Text("🍪")
                    }
                    
                    // Banner text
                    Column(
                        modifier = Modifier
                            .flexGrow(1)
                            .gap(KhodedSpacing.sm)
                    ) {
                        H3(
                            attrs = Modifier
                                .fontSize(KhodedTypography.lg)
                                .fontWeight(KhodedTypography.semiBold)
                                .color(KhodedColors.TextPrimary)
                                .margin(0.px)
                                .toAttrs()
                        ) {
                            Text(i18n.translate("privacy.banner.title", "We value your privacy"))
                        }
                        
                        P(
                            attrs = Modifier
                                .fontSize(KhodedTypography.sm)
                                .lineHeight(KhodedTypography.relaxed)
                                .color(KhodedColors.TextSecondary)
                                .margin(0.px)
                                .toAttrs()
                        ) {
                            Text(
                                i18n.translate(
                                    "privacy.banner.description",
                                    "We use cookies to enhance your browsing experience, serve personalized content, and analyze our traffic. By clicking \"Accept All\", you consent to our use of cookies."
                                )
                            )
                            Text(" ")
                            A(
                                href = "/privacy",
                                attrs = Modifier
                                    .color(KhodedColors.Purple600)
                                    .textDecorationLine(TextDecorationLine.Underline)
                                    .hover {
                                        color(KhodedColors.Purple700)
                                    }
                                    .toAttrs()
                            ) {
                                Text(i18n.translate("privacy.banner.learn_more", "Learn more"))
                            }
                        }
                    }
                    
                    // Close button (mobile)
                    org.jetbrains.compose.web.dom.Button(
                        attrs = Modifier
                            .size(32.px)
                            .backgroundColor(Color.transparent)
                            .border(0.px)
                            .borderRadius(KhodedRadius.sm)
                            .color(KhodedColors.TextSecondary)
                            .cursor(Cursor.Pointer)
                            .display(DisplayStyle.Block)
                            .breakpoint(Breakpoint.MD) {
                                display(DisplayStyle.None)
                            }
                            .hover {
                                backgroundColor(KhodedColors.Gray100)
                            }
                            .toAttrs {
                                attr("aria-label", "Close banner")
                                onClick { onDismiss() }
                            }
                    ) {
                        Text("✕")
                    }
                }
                
                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gap(KhodedSpacing.md)
                        .justifyContent(JustifyContent.Center)
                        .breakpoint(Breakpoint.SM) {
                            justifyContent(JustifyContent.End)
                        },
                    horizontalArrangement = Arrangement.spacedBy(KhodedSpacing.md)
                ) {
                    // Reject all
                    KhodedAccessibleButton(
                        text = i18n.translate("privacy.banner.reject_all", "Reject All"),
                        onClick = onRejectAll,
                        variant = KhodedButtonVariant.Ghost,
                        size = KhodedButtonSize.Medium
                    )
                    
                    // Customize
                    KhodedAccessibleButton(
                        text = i18n.translate("privacy.banner.customize", "Customize"),
                        onClick = onCustomize,
                        variant = KhodedButtonVariant.Secondary,
                        size = KhodedButtonSize.Medium
                    )
                    
                    // Accept all
                    KhodedAccessibleButton(
                        text = i18n.translate("privacy.banner.accept_all", "Accept All"),
                        onClick = onAcceptAll,
                        variant = KhodedButtonVariant.Primary,
                        size = KhodedButtonSize.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun PrivacyPreferencesModal(
    currentPreferences: PrivacyPreferences,
    onSave: (PrivacyPreferences) -> Unit,
    onCancel: () -> Unit
) {
    val i18n = useTranslation()
    var preferences by remember { mutableStateOf(currentPreferences) }
    
    // Modal overlay
    Div(
        attrs = Modifier
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
            .right(0.px)
            .bottom(0.px)
            .backgroundColor(Color.black.copy(alpha = 128)) // 50% opacity
            .zIndex(10001)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .padding(KhodedSpacing.lg)
            .animation(
                Animation(
                    name = "fadeIn",
                    duration = KhodedAnimations.normal
                )
            )
            .toAttrs {
                onClick { onCancel() }
                attr("role", "dialog")
                attr("aria-modal", "true")
                attr("aria-labelledby", "privacy-modal-title")
            }
    ) {
        // Modal content
        Div(
            attrs = Modifier
                .maxWidth(600.px)
                .maxHeight(80.vh)
                .backgroundColor(KhodedColors.Background)
                .borderRadius(KhodedRadius.lg)
                .boxShadow(KhodedShadows.xl)
                .overflow(Overflow.Auto)
                .animation(
                    Animation(
                        name = "scaleIn",
                        duration = KhodedAnimations.normal,
                        timingFunction = AnimationTimingFunction.EaseOut
                    )
                )
                .toAttrs {
                    onClick { event -> event.stopPropagation() }
                }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(KhodedSpacing.xl2)
                        .borderBottom(1.px, LineStyle.Solid, KhodedColors.Gray200)
                        .justifyContent(JustifyContent.SpaceBetween)
                        .alignItems(AlignItems.Center)
                ) {
                    H2(
                        attrs = Modifier
                            .id("privacy-modal-title")
                            .fontSize(KhodedTypography.xl)
                            .fontWeight(KhodedTypography.semiBold)
                            .color(KhodedColors.TextPrimary)
                            .margin(0.px)
                            .toAttrs()
                    ) {
                        Text(i18n.translate("privacy.modal.title", "Privacy Preferences"))
                    }
                    
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
                                attr("aria-label", "Close modal")
                                onClick { onCancel() }
                            }
                    ) {
                        Text("✕")
                    }
                }
                
                // Content
                Div(
                    attrs = Modifier
                        .padding(KhodedSpacing.xl2)
                        .toAttrs()
                ) {
                    Column(
                        modifier = Modifier.gap(KhodedSpacing.xl)
                    ) {
                        // Description
                        P(
                            attrs = Modifier
                                .fontSize(KhodedTypography.sm)
                                .lineHeight(KhodedTypography.relaxed)
                                .color(KhodedColors.TextSecondary)
                                .margin(0.px)
                                .marginBottom(KhodedSpacing.lg)
                                .toAttrs()
                        ) {
                            Text(
                                i18n.translate(
                                    "privacy.modal.description",
                                    "Customize your privacy settings below. You can change these preferences at any time."
                                )
                            )
                        }
                        
                        // Cookie categories
                        Column(
                            modifier = Modifier.gap(KhodedSpacing.lg)
                        ) {
                            // Functional cookies (always on)
                            CookieCategoryCard(
                                title = i18n.translate("privacy.functional.title", "Essential"),
                                description = i18n.translate(
                                    "privacy.functional.description", 
                                    "These cookies are necessary for the website to function and cannot be switched off."
                                ),
                                enabled = true,
                                locked = true,
                                onToggle = { }
                            )
                            
                            // Analytics cookies
                            CookieCategoryCard(
                                title = i18n.translate("privacy.analytics.title", "Analytics"),
                                description = i18n.translate(
                                    "privacy.analytics.description",
                                    "These cookies help us understand how visitors interact with our website by collecting anonymous information."
                                ),
                                enabled = preferences.analytics,
                                onToggle = { enabled ->
                                    preferences = preferences.copy(analytics = enabled)
                                }
                            )
                            
                            // Marketing cookies
                            CookieCategoryCard(
                                title = i18n.translate("privacy.marketing.title", "Marketing"),
                                description = i18n.translate(
                                    "privacy.marketing.description",
                                    "These cookies are used to make advertising messages more relevant to you and your interests."
                                ),
                                enabled = preferences.marketing,
                                onToggle = { enabled ->
                                    preferences = preferences.copy(marketing = enabled)
                                }
                            )
                            
                            // Performance cookies
                            CookieCategoryCard(
                                title = i18n.translate("privacy.performance.title", "Performance"),
                                description = i18n.translate(
                                    "privacy.performance.description",
                                    "These cookies help us improve website performance and user experience."
                                ),
                                enabled = preferences.performance,
                                onToggle = { enabled ->
                                    preferences = preferences.copy(performance = enabled)
                                }
                            )
                        }
                    }
                }
                
                // Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(KhodedSpacing.xl2)
                        .borderTop(1.px, LineStyle.Solid, KhodedColors.Gray200)
                        .justifyContent(JustifyContent.End)
                        .gap(KhodedSpacing.md)
                ) {
                    KhodedAccessibleButton(
                        text = i18n.translate("privacy.modal.cancel", "Cancel"),
                        onClick = onCancel,
                        variant = KhodedButtonVariant.Ghost,
                        size = KhodedButtonSize.Medium
                    )
                    
                    KhodedAccessibleButton(
                        text = i18n.translate("privacy.modal.save", "Save Preferences"),
                        onClick = { onSave(preferences) },
                        variant = KhodedButtonVariant.Primary,
                        size = KhodedButtonSize.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun CookieCategoryCard(
    title: String,
    description: String,
    enabled: Boolean,
    locked: Boolean = false,
    onToggle: (Boolean) -> Unit
) {
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .padding(KhodedSpacing.lg)
            .border(1.px, LineStyle.Solid, KhodedColors.Gray200)
            .borderRadius(KhodedRadius.md)
            .backgroundColor(if (enabled) KhodedColors.Purple50 else KhodedColors.Background)
            .toAttrs()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .justifyContent(JustifyContent.SpaceBetween)
                .alignItems(AlignItems.Start)
                .gap(KhodedSpacing.lg)
        ) {
            Column(
                modifier = Modifier.flexGrow(1).gap(KhodedSpacing.sm)
            ) {
                H4(
                    attrs = Modifier
                        .fontSize(KhodedTypography.base)
                        .fontWeight(KhodedTypography.semiBold)
                        .color(KhodedColors.TextPrimary)
                        .margin(0.px)
                        .toAttrs()
                ) {
                    Text(title)
                    if (locked) {
                        Span(
                            attrs = Modifier
                                .fontSize(KhodedTypography.xs)
                                .color(KhodedColors.TextMuted)
                                .marginLeft(KhodedSpacing.sm)
                                .toAttrs()
                        ) {
                            Text("(Always On)")
                        }
                    }
                }
                
                P(
                    attrs = Modifier
                        .fontSize(KhodedTypography.sm)
                        .lineHeight(KhodedTypography.relaxed)
                        .color(KhodedColors.TextSecondary)
                        .margin(0.px)
                        .toAttrs()
                ) {
                    Text(description)
                }
            }
            
            // Toggle switch
            ToggleSwitch(
                enabled = enabled,
                disabled = locked,
                onToggle = onToggle
            )
        }
    }
}

@Composable
private fun ToggleSwitch(
    enabled: Boolean,
    disabled: Boolean = false,
    onToggle: (Boolean) -> Unit
) {
    org.jetbrains.compose.web.dom.Button(
        attrs = Modifier
            .width(52.px)
            .height(28.px)
            .backgroundColor(
                when {
                    disabled -> KhodedColors.Gray300
                    enabled -> KhodedColors.Purple500
                    else -> KhodedColors.Gray400
                }
            )
            .borderRadius(14.px)
            .border(0.px)
            .cursor(if (disabled) Cursor.NotAllowed else Cursor.Pointer)
            .position(Position.Relative)
            .transition(CSSTransition("background-color", KhodedAnimations.fast))
            .opacity(if (disabled) 0.6 else 1.0)
            .toAttrs {
                attr("role", "switch")
                attr("aria-checked", enabled.toString())
                disabled(disabled)
                onClick { 
                    if (!disabled) onToggle(!enabled) 
                }
            }
    ) {
        Div(
            attrs = Modifier
                .position(Position.Absolute)
                .width(24.px)
                .height(24.px)
                .backgroundColor(Color.white)
                .borderRadius(12.px)
                .top(2.px)
                .left(if (enabled) 26.px else 2.px)
                .transition(CSSTransition("left", KhodedAnimations.fast))
                .boxShadow("0 2px 4px rgba(0, 0, 0, 0.1)")
                .toAttrs()
        )
    }
}