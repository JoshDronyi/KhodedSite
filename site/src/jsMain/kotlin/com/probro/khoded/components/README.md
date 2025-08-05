# Khoded Components

> **Reusable UI components for the Khoded website**

This directory contains all reusable UI components following a clean architecture pattern and accessibility-first design principles.

## 📁 **Directory Structure**

```
components/
├── accessibility/      # Accessible component enhancements
│   ├── AccessibleComponents.kt
│   └── AccessibilityEnhancements.kt
├── composables/        # Form and UI composables
│   ├── FormComposables.kt
│   ├── NavigationItem.kt
│   ├── OptimizedImage.kt
│   └── popupscreen/
├── interactions/       # Advanced interaction patterns
│   └── AdvancedInteractions.kt
├── layout/            # Layout components
│   └── Navigation.kt
├── privacy/           # Privacy and GDPR components
│   └── PrivacyBanner.kt
├── seo/               # SEO optimization components
│   └── SEOHead.kt
└── widgets/           # Standalone widgets
    └── Footer.kt
```

## 🧩 **Component Categories**

### **🎯 Core UI Components**
- **FormComposables.kt** - Accessible form inputs with validation
- **NavigationItem.kt** - Navigation menu items with proper ARIA
- **OptimizedImage.kt** - Performance-optimized image loading

### **♿ Accessibility Components**  
- **AccessibleComponents.kt** - WCAG 2.2 AA compliant UI elements
- **AccessibilityEnhancements.kt** - Screen reader and keyboard support

### **🎨 Layout Components**
- **Navigation.kt** - Responsive navigation with mobile menu
- **Footer.kt** - Site-wide footer with proper structure

### **🔒 Privacy Components**
- **PrivacyBanner.kt** - GDPR/CCPA compliant cookie consent

### **📈 SEO Components**
- **SEOHead.kt** - Meta tags, structured data, and Open Graph

### **🖱️ Interaction Components**
- **AdvancedInteractions.kt** - Micro-interactions and gestures

## 🛠️ **Usage Guidelines**

### **Component Design Principles**
1. **Accessibility First** - Every component meets WCAG 2.2 AA standards
2. **Performance Optimized** - Lazy loading and efficient rendering  
3. **Type Safe** - Full Kotlin type safety with compile-time checks
4. **Responsive Design** - Mobile-first with fluid breakpoints
5. **Clean Code** - Following Uncle Bob's principles

### **Example Usage**
```kotlin
// Accessible button with proper ARIA
KhodedAccessibleButton(
    text = "Submit Form",
    onClick = { /* handle click */ },
    variant = KhodedButtonVariant.Primary,
    size = KhodedButtonSize.Medium,
    ariaLabel = "Submit contact form",
    loadingState = isSubmitting
)

// Optimized image with lazy loading
OptimizedImage(
    src = "/images/hero-image.webp",
    alt = "Modern web development showcase",
    loading = ImageLoading.Lazy,
    sizes = "(min-width: 768px) 50vw, 100vw"
)
```

## 🎨 **Design System Integration**

All components integrate with the **KhodedDesignSystem** for consistent styling:

- **Colors**: WCAG 2.2 AA compliant color palette
- **Typography**: Fluid typography with proper line heights  
- **Spacing**: Consistent spacing scale (4px base unit)
- **Shadows**: Layered shadow system for depth
- **Animation**: Smooth transitions respecting reduced motion

## ♿ **Accessibility Features**

### **Built-in Accessibility**
- Proper ARIA attributes and roles
- Keyboard navigation support
- Screen reader optimization
- High contrast mode compatibility
- Focus management and indicators

### **Testing Support**
- Automated accessibility testing utilities
- WCAG compliance validation
- Screen reader testing helpers

## 📚 **Related Documentation**

- **[Design System Guide](../styles/README.md)** - Styling and theming
- **[Accessibility Guide](../accessibility/README.md)** - A11y implementation details
- **[Testing Guide](../testing/README.md)** - Component testing strategies

---

**See also:** [Kobweb Components Guide](https://github.com/varabyte/kobweb#layouts-sections-and-widgets)
