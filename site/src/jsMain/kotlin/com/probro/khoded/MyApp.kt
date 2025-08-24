package com.probro.khoded

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.systemPreference
import com.probro.khoded.utils.performance.PerformanceMonitor
import com.probro.khoded.accessibility.AccessibilityUtils
import com.probro.khoded.accessibility.FocusManager
import org.jetbrains.compose.web.css.*

@App
@Composable
fun MyApp(content: @Composable () -> Unit) {
    SilkApp {
        Surface(SmoothColorStyle.toModifier().minHeight(100.vh).fillMaxWidth()) {
            content()
        }
    }
}

@InitSilk
fun initSilk(ctx: InitSilkContext) {
    ctx.config.initialColorMode = ColorMode.systemPreference
    
    // Initialize performance monitoring for production
    PerformanceMonitor.measureCoreWebVitals()
    
    // Initialize accessibility enhancements
    val focusManager = FocusManager()
    focusManager.startManaging()
    AccessibilityUtils.createSkipLinks()
    AccessibilityUtils.enhanceKeyboardNavigation()
    
    // Track page view for analytics
    js("""
        console.log('Khoded application initialized - Performance monitoring & accessibility active');
        
        window.addEventListener('load', function() {
            var metricData = {
                metric: 'page_view',
                value: 1,
                timestamp: Date.now(),
                url: window.location.pathname,
                type: 'initialization'
            };
            
            fetch('/api/metrics', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(metricData)
            }).catch(function(err) {
                console.warn('Failed to send page view metric:', err);
            });
        });
    """)
}