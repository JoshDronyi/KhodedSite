package com.probro.khoded.i18n

import androidx.compose.runtime.*
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * Internationalization (i18n) System
 * 
 * Comprehensive internationalization support with:
 * - Multiple language support
 * - RTL language support
 * - Pluralization rules
 * - Date/time/number formatting
 * - Dynamic language switching
 * - Lazy loading of translations
 */

// =============================================================================
// CORE INTERNATIONALIZATION TYPES
// =============================================================================

data class Locale(
    val code: String,
    val name: String,
    val nativeName: String,
    val isRTL: Boolean = false,
    val dateFormat: String = "MM/dd/yyyy",
    val timeFormat: String = "HH:mm",
    val numberFormat: NumberFormat = NumberFormat.Western,
    val pluralRules: PluralRules = PluralRules.English
)

enum class NumberFormat {
    Western, // 1,234.56
    European, // 1.234,56
    Indian, // 1,23,456.78
    Arabic // ١٢٣٤٫٥٦
}

enum class PluralRules {
    English, // one, other
    Slavic, // one, few, many, other
    Arabic, // zero, one, two, few, many, other
    Chinese // other (no plurals)
}

// Supported languages
object SupportedLocales {
    val English = Locale("en", "English", "English")
    val Spanish = Locale("es", "Spanish", "Español")
    val French = Locale("fr", "French", "Français") 
    val German = Locale("de", "German", "Deutsch")
    val Portuguese = Locale("pt", "Portuguese", "Português")
    val Arabic = Locale("ar", "Arabic", "العربية", isRTL = true, pluralRules = PluralRules.Arabic)
    val Chinese = Locale("zh", "Chinese", "中文", pluralRules = PluralRules.Chinese)
    val Japanese = Locale("ja", "Japanese", "日本語", pluralRules = PluralRules.Chinese)
    val Russian = Locale("ru", "Russian", "Русский", pluralRules = PluralRules.Slavic)
    val Hindi = Locale("hi", "Hindi", "हिंदी", numberFormat = NumberFormat.Indian)
    
    val all = listOf(English, Spanish, French, German, Portuguese, Arabic, Chinese, Japanese, Russian, Hindi)
    
    fun fromCode(code: String): Locale? = all.find { it.code == code }
}

// =============================================================================
// TRANSLATION MANAGEMENT
// =============================================================================

@Serializable
data class TranslationKey(
    val key: String,
    val defaultValue: String = "",
    val description: String = "",
    val context: String = ""
)

interface TranslationProvider {
    suspend fun loadTranslations(locale: Locale): Map<String, String>
    suspend fun getTranslation(key: String, locale: Locale): String?
}

class JsonTranslationProvider : TranslationProvider {
    private val cache = mutableMapOf<String, Map<String, String>>()
    
    override suspend fun loadTranslations(locale: Locale): Map<String, String> {
        return cache.getOrPut(locale.code) {
            try {
                val response = window.fetch("/i18n/${locale.code}.json")
                val json = response.text()
                Json.decodeFromString<Map<String, String>>(json)
            } catch (e: Exception) {
                console.warn("Failed to load translations for ${locale.code}: ${e.message}")
                emptyMap()
            }
        }
    }
    
    override suspend fun getTranslation(key: String, locale: Locale): String? {
        val translations = loadTranslations(locale)
        return translations[key]
    }
    
    fun clearCache() {
        cache.clear()
    }
}

// =============================================================================
// I18N CONTEXT AND PROVIDER
// =============================================================================

data class I18nContext(
    val currentLocale: Locale,
    val translations: Map<String, String>,
    val isLoading: Boolean = false,
    val error: String? = null
)

class I18nManager(
    private val translationProvider: TranslationProvider,
    private val fallbackLocale: Locale = SupportedLocales.English
) {
    private val _context = mutableStateOf(I18nContext(
        currentLocale = detectUserLocale(),
        translations = emptyMap()
    ))
    
    val context: State<I18nContext> = _context
    
    suspend fun setLocale(locale: Locale) {
        _context.value = _context.value.copy(isLoading = true, error = null)
        
        try {
            val translations = translationProvider.loadTranslations(locale)
            
            _context.value = _context.value.copy(
                currentLocale = locale,
                translations = translations,
                isLoading = false
            )
            
            // Update document language
            document.documentElement.lang = locale.code
            document.documentElement.dir = if (locale.isRTL) "rtl" else "ltr"
            
            // Store preference
            window.localStorage.setItem("preferred_locale", locale.code)
            
        } catch (e: Exception) {
            _context.value = _context.value.copy(
                isLoading = false,
                error = "Failed to load translations: ${e.message}"
            )
        }
    }
    
    fun translate(key: String, fallback: String = key, args: Map<String, Any> = emptyMap()): String {
        val translation = _context.value.translations[key] ?: fallback
        return interpolateTranslation(translation, args)
    }
    
    fun translatePlural(
        key: String, 
        count: Int, 
        fallback: String = key,
        args: Map<String, Any> = emptyMap()
    ): String {
        val pluralForm = getPluralForm(_context.value.currentLocale, count)
        val pluralKey = "${key}.${pluralForm}"
        
        val translation = _context.value.translations[pluralKey] 
            ?: _context.value.translations[key] 
            ?: fallback
            
        return interpolateTranslation(translation, args + ("count" to count))
    }
    
    private fun interpolateTranslation(template: String, args: Map<String, Any>): String {
        var result = template
        args.forEach { (key, value) ->
            result = result.replace("{$key}", value.toString())
            result = result.replace("{{$key}}", value.toString()) // Alternative syntax
        }
        return result
    }
    
    private fun getPluralForm(locale: Locale, count: Int): String {
        return when (locale.pluralRules) {
            PluralRules.English -> {
                when {
                    count == 1 -> "one"
                    else -> "other"
                }
            }
            PluralRules.Slavic -> {
                when {
                    count == 1 -> "one"
                    count % 10 in 2..4 && count % 100 !in 12..14 -> "few"
                    count % 10 == 0 || count % 10 in 5..9 || count % 100 in 11..14 -> "many"
                    else -> "other"
                }
            }
            PluralRules.Arabic -> {
                when {
                    count == 0 -> "zero"
                    count == 1 -> "one"
                    count == 2 -> "two"
                    count % 100 in 3..10 -> "few"
                    count % 100 in 11..99 -> "many"
                    else -> "other"
                }
            }
            PluralRules.Chinese -> "other"
        }
    }
    
    private fun detectUserLocale(): Locale {
        // Check stored preference first
        window.localStorage.getItem("preferred_locale")?.let { code ->
            SupportedLocales.fromCode(code)?.let { return it }
        }
        
        // Check browser language
        val browserLang = window.navigator.language.split("-").first()
        SupportedLocales.fromCode(browserLang)?.let { return it }
        
        // Check browser languages list
        window.navigator.languages.forEach { lang ->
            val code = lang.split("-").first()
            SupportedLocales.fromCode(code)?.let { return it }
        }
        
        return fallbackLocale
    }
}

// =============================================================================
// COMPOSABLE FUNCTIONS
// =============================================================================

@Composable
fun I18nProvider(
    translationProvider: TranslationProvider = JsonTranslationProvider(),
    content: @Composable () -> Unit
) {
    val manager = remember { I18nManager(translationProvider) }
    val context by manager.context
    
    LaunchedEffect(Unit) {
        manager.setLocale(context.currentLocale)
    }
    
    CompositionLocalProvider(LocalI18nManager provides manager) {
        content()
    }
}

val LocalI18nManager = compositionLocalOf<I18nManager> { error("I18nManager not provided") }

@Composable
fun useTranslation(): I18nManager {
    return LocalI18nManager.current
}

@Composable
fun T(key: String, fallback: String = key, args: Map<String, Any> = emptyMap()): String {
    val i18n = useTranslation()
    return i18n.translate(key, fallback, args)
}

@Composable
fun TPlural(
    key: String, 
    count: Int, 
    fallback: String = key,
    args: Map<String, Any> = emptyMap()
): String {
    val i18n = useTranslation()
    return i18n.translatePlural(key, count, fallback, args)
}

// =============================================================================
// FORMATTING UTILITIES
// =============================================================================

object I18nFormatters {
    fun formatNumber(number: Double, locale: Locale): String {
        return when (locale.numberFormat) {
            NumberFormat.Western -> {
                val formatter = js("new Intl.NumberFormat('en-US')")
                formatter.format(number) as String
            }
            NumberFormat.European -> {
                val formatter = js("new Intl.NumberFormat('de-DE')")
                formatter.format(number) as String
            }
            NumberFormat.Indian -> {
                val formatter = js("new Intl.NumberFormat('hi-IN')")
                formatter.format(number) as String
            }
            NumberFormat.Arabic -> {
                val formatter = js("new Intl.NumberFormat('ar-SA')")
                formatter.format(number) as String
            }
        }
    }
    
    fun formatCurrency(amount: Double, currency: String, locale: Locale): String {
        val formatter = js("new Intl.NumberFormat('${locale.code}', { style: 'currency', currency: '$currency' })")
        return formatter.format(amount) as String
    }
    
    fun formatDate(timestamp: Long, locale: Locale, options: DateFormatOptions? = null): String {
        val formatter = if (options != null) {
            js("new Intl.DateTimeFormat('${locale.code}', options)")
        } else {
            js("new Intl.DateTimeFormat('${locale.code}')")
        }
        return formatter.format(js("new Date($timestamp)")) as String
    }
    
    fun formatRelativeTime(timestamp: Long, locale: Locale): String {
        val now = js("Date.now()") as Long
        val diffMs = now - timestamp
        val diffMinutes = diffMs / (1000 * 60)
        val diffHours = diffMinutes / 60
        val diffDays = diffHours / 24
        
        val formatter = js("new Intl.RelativeTimeFormat('${locale.code}', { numeric: 'auto' })")
        
        return when {
            diffMinutes < 1 -> formatter.format(0, "minute") as String
            diffMinutes < 60 -> formatter.format(-diffMinutes.toInt(), "minute") as String
            diffHours < 24 -> formatter.format(-diffHours.toInt(), "hour") as String
            diffDays < 30 -> formatter.format(-diffDays.toInt(), "day") as String
            else -> formatDate(timestamp, locale)
        }
    }
}

external interface DateFormatOptions {
    val year: String? // "numeric", "2-digit"
    val month: String? // "numeric", "2-digit", "long", "short", "narrow"
    val day: String? // "numeric", "2-digit"
    val hour: String? // "numeric", "2-digit"
    val minute: String? // "numeric", "2-digit"
    val second: String? // "numeric", "2-digit"
    val timeZoneName: String? // "short", "long"
}

// =============================================================================
// RTL SUPPORT
// =============================================================================

@Composable
fun RTLProvider(
    isRTL: Boolean,
    content: @Composable () -> Unit
) {
    LaunchedEffect(isRTL) {
        document.documentElement.dir = if (isRTL) "rtl" else "ltr"
        
        // Inject RTL-specific styles
        if (isRTL) {
            injectRTLStyles()
        } else {
            removeRTLStyles()
        }
    }
    
    content()
}

private fun injectRTLStyles() {
    val existingStyle = document.getElementById("rtl-styles")
    if (existingStyle != null) return
    
    val style = document.createElement("style").apply {
        id = "rtl-styles"
        textContent = """
            [dir="rtl"] .text-left { text-align: right !important; }
            [dir="rtl"] .text-right { text-align: left !important; }
            [dir="rtl"] .float-left { float: right !important; }
            [dir="rtl"] .float-right { float: left !important; }
            [dir="rtl"] .ml-auto { margin-left: 0 !important; margin-right: auto !important; }
            [dir="rtl"] .mr-auto { margin-right: 0 !important; margin-left: auto !important; }
            [dir="rtl"] .pl-4 { padding-left: 0 !important; padding-right: 1rem !important; }
            [dir="rtl"] .pr-4 { padding-right: 0 !important; padding-left: 1rem !important; }
            [dir="rtl"] .border-l { border-left: none !important; border-right: 1px solid !important; }
            [dir="rtl"] .border-r { border-right: none !important; border-left: 1px solid !important; }
        """.trimIndent()
    }
    
    document.head?.appendChild(style)
}

private fun removeRTLStyles() {
    document.getElementById("rtl-styles")?.remove()
}

// =============================================================================
// LANGUAGE SWITCHER COMPONENT
// =============================================================================

@Composable
fun LanguageSwitcher(
    modifier: Modifier = Modifier,
    availableLocales: List<Locale> = SupportedLocales.all.take(5), // Show top 5 by default
    showFlags: Boolean = true
) {
    val i18n = useTranslation()
    val context by i18n.context
    var isOpen by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier.position(Position.Relative)
    ) {
        // Current language button
        org.jetbrains.compose.web.dom.Button(
            attrs = Modifier
                .padding(KhodedSpacing.sm)
                .borderRadius(KhodedRadius.md)
                .border(1.px, LineStyle.Solid, KhodedColors.Gray300)
                .backgroundColor(KhodedColors.Background)
                .cursor(Cursor.Pointer)
                .toAttrs {
                    onClick { isOpen = !isOpen }
                    attr("aria-expanded", isOpen.toString())
                    attr("aria-haspopup", "true")
                    attr("aria-label", "Select language")
                }
        ) {
            Row(
                modifier = Modifier
                    .alignItems(AlignItems.Center)
                    .gap(KhodedSpacing.xs)
            ) {
                if (showFlags) {
                    Span(
                        attrs = Modifier
                            .fontSize(18.px)
                            .toAttrs()
                    ) {
                        Text(getFlagEmoji(context.currentLocale.code))
                    }
                }
                
                Span(
                    attrs = Modifier
                        .fontSize(KhodedTypography.sm)
                        .toAttrs()
                ) {
                    Text(context.currentLocale.nativeName)
                }
                
                Span(
                    attrs = Modifier
                        .fontSize(12.px)
                        .transform { if (isOpen) rotate(180.deg) else rotate(0.deg) }
                        .transition(CSSTransition("transform", KhodedAnimations.fast))
                        .toAttrs()
                ) {
                    Text("▼")
                }
            }
        }
        
        // Dropdown menu
        if (isOpen) {
            Div(
                attrs = Modifier
                    .position(Position.Absolute)
                    .top(100.percent)
                    .left(0.px)
                    .zIndex(1000)
                    .minWidth(200.px)
                    .backgroundColor(KhodedColors.Background)
                    .borderRadius(KhodedRadius.md)
                    .boxShadow(KhodedShadows.lg)
                    .border(1.px, LineStyle.Solid, KhodedColors.Gray200)
                    .padding(KhodedSpacing.xs)
                    .toAttrs {
                        attr("role", "menu")
                    }
            ) {
                availableLocales.forEach { locale ->
                    org.jetbrains.compose.web.dom.Button(
                        attrs = Modifier
                            .fillMaxWidth()
                            .padding(KhodedSpacing.sm)
                            .borderRadius(KhodedRadius.sm)
                            .backgroundColor(
                                if (locale == context.currentLocale) KhodedColors.Purple50
                                else Color.transparent
                            )
                            .border(0.px)
                            .cursor(Cursor.Pointer)
                            .textAlign(TextAlign.Start)
                            .hover {
                                backgroundColor(KhodedColors.Gray50)
                            }
                            .toAttrs {
                                attr("role", "menuitem")
                                onClick { 
                                    kotlinx.coroutines.MainScope().launch {
                                        i18n.setLocale(locale)
                                        isOpen = false
                                    }
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .alignItems(AlignItems.Center)
                                .gap(KhodedSpacing.sm)
                        ) {
                            if (showFlags) {
                                Span(
                                    attrs = Modifier.fontSize(16.px).toAttrs()
                                ) {
                                    Text(getFlagEmoji(locale.code))
                                }
                            }
                            
                            Column(
                                modifier = Modifier.flexGrow(1)
                            ) {
                                Span(
                                    attrs = Modifier
                                        .fontSize(KhodedTypography.sm)
                                        .fontWeight(KhodedTypography.medium)
                                        .toAttrs()
                                ) {
                                    Text(locale.nativeName)
                                }
                                
                                if (locale.name != locale.nativeName) {
                                    Span(
                                        attrs = Modifier
                                            .fontSize(KhodedTypography.xs)
                                            .color(KhodedColors.TextSecondary)
                                            .toAttrs()
                                    ) {
                                        Text(locale.name)
                                    }
                                }
                            }
                            
                            if (locale == context.currentLocale) {
                                Span(
                                    attrs = Modifier
                                        .color(KhodedColors.Purple500)
                                        .fontSize(12.px)
                                        .toAttrs()
                                ) {
                                    Text("✓")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getFlagEmoji(localeCode: String): String {
    return when (localeCode) {
        "en" -> "🇺🇸"
        "es" -> "🇪🇸"
        "fr" -> "🇫🇷"
        "de" -> "🇩🇪"
        "pt" -> "🇵🇹"
        "ar" -> "🇸🇦"
        "zh" -> "🇨🇳"
        "ja" -> "🇯🇵"
        "ru" -> "🇷🇺"
        "hi" -> "🇮🇳"
        else -> "🌐"
    }
}