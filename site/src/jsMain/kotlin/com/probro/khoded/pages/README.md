# Khoded Pages

> **Page components and routing for the Khoded website**

This directory contains all page-level components that define the main routes and sections of the website.

## 📁 **Page Structure**

```
pages/
├── Index.kt               # Homepage (/)
├── About.kt               # About page (/about)
├── Services.kt            # Services page (/services)
├── Contact.kt             # Contact page (/contact)
├── aboutSections/         # About page sections
├── contactSections/       # Contact page sections
├── homeSections/          # Homepage sections
├── intakeForm/            # Client intake workflow
└── misc/                  # Utility pages
```

## 🏠 **Main Pages**

### **Homepage - Index.kt**
- Hero/landing section with value proposition
- Services overview with interactive elements
- Client testimonials with proper accessibility
- Call-to-action for consultation booking

### **About - About.kt** 
- Company story and mission
- Team member profiles
- Core values and approach
- Partnership opportunities

### **Services - Services.kt**
- Comprehensive service offerings
- Detailed capability descriptions
- Technology stack showcase
- Project case studies

### **Contact - Contact.kt**
- Multi-step contact form with validation
- Office location and contact information
- Interactive consultation booking
- Response time commitments

## 📄 **Page Sections**

### **Home Sections**
- **Landing.kt** - Hero section with animated elements
- **ServicesSection.kt** - Service overview cards
- **Testimonial.kt** - Client testimonial carousel
- **Consultation.kt** - Consultation booking form
- **DesignSection.kt** - Design process showcase

### **About Sections** 
- **AboutLandingDisplay.kt** - About hero section
- **StorySectionDisplay.kt** - Company story narrative
- **TeamSectionDisplay.kt** - Team member profiles
- **OpportunitiesSectionDisplay.kt** - Career opportunities

### **Contact Sections**
- **ContactFormState.kt** - Form state management
- **ContactPageStateHolder.kt** - Page-level state

### **Intake Form Workflow**
- **ServicesLanding.kt** - Service selection
- **ClientRequestForm.kt** - Detailed project requirements
- **IntakeFormReview.kt** - Form review and validation
- **ClientRequestThanks.kt** - Success confirmation
- **GetStartedService.kt** - Initial service setup

## 🎯 **Page Design Principles**

### **Performance Optimization**
- **Lazy Loading** - Sections load as they enter viewport
- **Code Splitting** - Pages loaded on-demand
- **Image Optimization** - WebP format with fallbacks
- **Resource Preloading** - Critical resources loaded first

### **Accessibility Compliance**
- **WCAG 2.2 AA** - Full compliance across all pages
- **Semantic HTML** - Proper heading hierarchy and landmarks
- **Screen Reader Support** - ARIA labels and descriptions
- **Keyboard Navigation** - Full keyboard accessibility

### **SEO Optimization**
- **Structured Data** - JSON-LD for rich snippets
- **Meta Tags** - Proper Open Graph and Twitter Cards
- **URL Structure** - Clean, descriptive URLs
- **Internal Linking** - Strategic cross-page linking

## 🚀 **Routing System**

Pages use Kobweb's file-based routing:

```kotlin
// Homepage
@Page("/")
@Composable
fun HomePage() { /* ... */ }

// About page  
@Page("/about")
@Composable
fun AboutPage() { /* ... */ }

// Services with dynamic routing
@Page("/services/{service}")
@Composable
fun ServicePage(service: String) { /* ... */ }
```

## 📱 **Responsive Design**

All pages implement mobile-first responsive design:

- **Mobile** (0-768px) - Stack layout with touch-friendly UI
- **Tablet** (768-1024px) - Adaptive grid with enhanced navigation
- **Desktop** (1024px+) - Full-width layouts with advanced interactions

## 🧪 **Testing Support**

Each page includes comprehensive testing:

- **Unit Tests** - Component logic validation
- **Integration Tests** - Form submission and navigation
- **E2E Tests** - Complete user journey validation
- **Accessibility Tests** - WCAG compliance verification
- **Performance Tests** - Core Web Vitals benchmarking

## 📚 **Related Documentation**

- **[Components Guide](../components/README.md)** - Reusable UI components
- **[Styling Guide](../styles/README.md)** - Design system and theming
- **[Utils Guide](../utils/README.md)** - Utility functions and helpers

---

**See also:** [Kobweb Pages Guide](https://github.com/varabyte/kobweb#create-a-page)
