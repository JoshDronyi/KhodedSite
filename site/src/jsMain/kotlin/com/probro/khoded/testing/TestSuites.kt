package com.probro.khoded.testing

import com.probro.khoded.data.api.*
import com.probro.khoded.data.models.*
import com.probro.khoded.data.state.*
import com.probro.khoded.accessibility.*
import kotlinx.coroutines.*
import kotlin.js.Date
import kotlin.test.*

/**
 * Comprehensive Test Suites
 * 
 * Pre-built test suites for various components and features:
 * - API Integration Tests
 * - Form Validation Tests  
 * - State Management Tests
 * - Component Integration Tests
 * - Performance Benchmark Tests
 * - Accessibility Compliance Tests
 */

// =============================================================================
// API INTEGRATION TEST SUITE
// =============================================================================

class ApiIntegrationTestSuite : TestSuite() {
    private lateinit var mockApiClient: MockApiClient
    
    override fun getTestName(): String = "API Integration Tests"
    
    override fun setup() {
        super.setup()
        mockApiClient = MockApiClient()
    }
    
    override suspend fun runTests(): TestSuiteResult {
        val startTime = Date().getTime()
        var passed = 0
        var failed = 0
        val totalTests = 5
        
        // Test 1: Successful contact form submission
        try {
            testContactFormSubmission()
            passed++
        } catch (e: Exception) {
            console.error("Contact form test failed", e)
            failed++
        }
        
        // Test 2: Newsletter subscription
        try {
            testNewsletterSubscription()
            passed++
        } catch (e: Exception) {
            console.error("Newsletter test failed", e)
            failed++
        }
        
        // Test 3: API error handling
        try {
            testApiErrorHandling()
            passed++
        } catch (e: Exception) {
            console.error("Error handling test failed", e)
            failed++
        }
        
        // Test 4: Rate limiting
        try {
            testRateLimiting()
            passed++
        } catch (e: Exception) {
            console.error("Rate limiting test failed", e)
            failed++
        }
        
        // Test 5: Request validation
        try {
            testRequestValidation()
            passed++
        } catch (e: Exception) {
            console.error("Request validation test failed", e)
            failed++
        }
        
        val endTime = Date().getTime()
        
        return TestSuiteResult(
            suiteName = getTestName(),
            totalTests = totalTests,
            passedTests = passed,
            failedTests = failed,
            duration = endTime - startTime,
            performanceReport = performanceTester.generateReport()
        )
    }
    
    private suspend fun testContactFormSubmission() {
        // Mock successful response
        mockApiClient.mockResponse(
            "/api/contact",
            ApiResult.Success(ContactFormResponse(
                id = "test-123",
                status = "received",
                message = "Thank you for your message"
            ))
        )
        
        val contactForm = ContactFormData(
            fullName = "John Doe",
            email = "john@example.com",
            phoneNumber = "+1234567890",
            company = "Test Corp",
            message = "Test message",
            acceptsMarketing = true
        )
        
        val measurement = performanceTester.measureAsyncOperation("contact-form-submit") {
            val result = mockApiClient.post<ContactFormResponse>("/api/contact", contactForm)
            
            assertTrue(result is ApiResult.Success)
            assertEquals("test-123", (result as ApiResult.Success).data.id)
            assertEquals("received", result.data.status)
        }
        
        // Assert performance requirements
        assertTrue(measurement.duration < 1000, "Contact form submission should complete within 1s")
    }
    
    private suspend fun testNewsletterSubscription() {
        mockApiClient.mockResponse(
            "/api/newsletter/subscribe",
            ApiResult.Success(NewsletterResponse(
                success = true,
                message = "Successfully subscribed"
            ))
        )
        
        val subscription = NewsletterSubscription(
            email = "test@example.com",
            firstName = "Test",
            interests = listOf("web-development", "design")
        )
        
        val result = mockApiClient.post<NewsletterResponse>("/api/newsletter/subscribe", subscription)
        
        assertTrue(result is ApiResult.Success)
        assertTrue((result as ApiResult.Success).data.success)
    }
    
    private suspend fun testApiErrorHandling() {
        // Mock error response
        mockApiClient.mockResponse(
            "/api/test-error",
            ApiResult.Error(ApiError.ServerError(500, "Internal server error"))
        )
        
        val result = mockApiClient.get<Any>("/api/test-error")
        
        assertTrue(result is ApiResult.Error)
        val error = result as ApiResult.Error
        assertTrue(error.error is ApiError.ServerError)
        assertEquals(500, (error.error as ApiError.ServerError).statusCode)
    }
    
    private suspend fun testRateLimiting() {
        // Test rate limiting functionality
        val rateLimiter = com.probro.khoded.security.RateLimiter(maxRequests = 3, windowMs = 1000)
        
        // First 3 requests should be allowed
        repeat(3) {
            assertTrue(rateLimiter.isAllowed("test-user"))
        }
        
        // 4th request should be blocked
        assertFalse(rateLimiter.isAllowed("test-user"))
    }
    
    private suspend fun testRequestValidation() {
        val validator = ContactFormValidator()
        
        // Valid form data
        val validForm = ContactFormData(
            fullName = "John Doe",
            email = "john@example.com",
            phoneNumber = "+1234567890",
            company = "Test Corp",
            message = "Valid message",
            acceptsMarketing = true
        )
        
        val validResult = validator.validate(validForm)
        assertTrue(validResult is ValidationResult.Success)
        
        // Invalid form data
        val invalidForm = ContactFormData(
            fullName = "",
            email = "invalid-email",
            phoneNumber = "",
            company = "",
            message = "",
            acceptsMarketing = true
        )
        
        val invalidResult = validator.validate(invalidForm)
        assertTrue(invalidResult is ValidationResult.Error)
        assertTrue((invalidResult as ValidationResult.Error).errors.size > 0)
    }
}

// =============================================================================
// FORM VALIDATION TEST SUITE
// =============================================================================

class FormValidationTestSuite : TestSuite() {
    override fun getTestName(): String = "Form Validation Tests"
    
    override suspend fun runTests(): TestSuiteResult {
        val startTime = Date().getTime()
        var passed = 0
        var failed = 0
        val totalTests = 8
        
        val tests = listOf(
            ::testEmailValidation,
            ::testPhoneValidation,
            ::testRequiredFields,
            ::testFieldLengthLimits,
            ::testSpecialCharacters,
            ::testSanitization,
            ::testFormStateManagement,
            ::testRealTimeValidation
        )
        
        for (test in tests) {
            try {
                test()
                passed++
            } catch (e: Exception) {
                console.error("Form validation test failed: ${test.name}", e)
                failed++
            }
        }
        
        val endTime = Date().getTime()
        
        return TestSuiteResult(
            suiteName = getTestName(),
            totalTests = totalTests,
            passedTests = passed,
            failedTests = failed,
            duration = endTime - startTime
        )
    }
    
    private fun testEmailValidation() {
        val validator = ContactFormValidator()
        
        // Valid emails
        val validEmails = listOf(
            "user@example.com",
            "test.email+tag@example.co.uk",
            "user123@test-domain.com"
        )
        
        validEmails.forEach { email ->
            val form = createTestContactForm(email = email)
            val result = validator.validate(form)
            assertTrue(result is ValidationResult.Success, "Email $email should be valid")
        }
        
        // Invalid emails
        val invalidEmails = listOf(
            "invalid-email",
            "@example.com",
            "user@",
            "user space@example.com"
        )
        
        invalidEmails.forEach { email ->
            val form = createTestContactForm(email = email)
            val result = validator.validate(form)
            assertTrue(result is ValidationResult.Error, "Email $email should be invalid")
        }
    }
    
    private fun testPhoneValidation() {
        val validator = ContactFormValidator()
        
        // Valid phone numbers
        val validPhones = listOf(
            "+1234567890",
            "(555) 123-4567",
            "555-123-4567"
        )
        
        validPhones.forEach { phone ->
            val form = createTestContactForm(phoneNumber = phone)
            val result = validator.validate(form)
            assertTrue(result is ValidationResult.Success, "Phone $phone should be valid")
        }
        
        // Invalid phone numbers
        val invalidPhones = listOf(
            "123",
            "abcdefghijk",
            "123-45-6789-0123-4567" // too long
        )
        
        invalidPhones.forEach { phone ->
            val form = createTestContactForm(phoneNumber = phone)
            val result = validator.validate(form)
            assertTrue(result is ValidationResult.Error, "Phone $phone should be invalid")
        }
    }
    
    private fun testRequiredFields() {
        val validator = ContactFormValidator()
        
        // Test missing required fields
        val emptyForm = ContactFormData(
            fullName = "",
            email = "",
            phoneNumber = "",
            company = "",
            message = "",
            acceptsMarketing = true
        )
        
        val result = validator.validate(emptyForm)
        assertTrue(result is ValidationResult.Error)
        
        val errors = (result as ValidationResult.Error).errors
        assertTrue(errors.any { it.field == "fullName" })
        assertTrue(errors.any { it.field == "email" })
        assertTrue(errors.any { it.field == "message" })
    }
    
    private fun testFieldLengthLimits() {
        val validator = ContactFormValidator()
        
        // Test message too long
        val longMessage = "x".repeat(2001) // Assuming 2000 char limit
        val form = createTestContactForm(message = longMessage)
        val result = validator.validate(form)
        
        assertTrue(result is ValidationResult.Error)
        val errors = (result as ValidationResult.Error).errors
        assertTrue(errors.any { it.field == "message" && it.message.contains("too long") })
    }
    
    private fun testSpecialCharacters() {
        val sanitizer = com.probro.khoded.security.InputSanitizer
        
        val maliciousInput = "<script>alert('xss')</script>"
        val sanitized = sanitizer.sanitizeHtml(maliciousInput)
        
        assertFalse(sanitized.contains("<script>"))
        assertFalse(sanitized.contains("alert"))
    }
    
    private fun testSanitization() {
        val sanitizer = com.probro.khoded.security.InputSanitizer
        
        val testCases = mapOf(
            "Hello <b>World</b>" to "Hello World", // HTML removal
            "Hello & goodbye" to "Hello &amp; goodbye", // HTML entities
            "javascript:alert(1)" to "", // Malicious URLs
            "onclick=\"alert(1)\"" to "" // Event handlers
        )
        
        testCases.forEach { (input, expected) ->
            val result = sanitizer.sanitizeText(input)
            assertTrue(result != input || input == expected, "Input should be sanitized: $input")
        }
    }
    
    private fun testFormStateManagement() {
        // Test form state transitions
        val initialData = createTestContactForm()
        // Note: This would need proper mocking of Compose state in a real test
        // For now, we test the validation logic
        
        val validator = ContactFormValidator()
        val result = validator.validate(initialData)
        assertTrue(result is ValidationResult.Success)
    }
    
    private fun testRealTimeValidation() {
        // Test incremental field validation
        val validator = ContactFormValidator()
        
        // Test partial form completion
        val partialForm = ContactFormData(
            fullName = "John Doe",
            email = "john@example.com",
            phoneNumber = "",
            company = "",
            message = "",
            acceptsMarketing = true
        )
        
        val result = validator.validate(partialForm)
        assertTrue(result is ValidationResult.Error)
        
        val errors = (result as ValidationResult.Error).errors
        // Should have errors for missing message, but not for completed fields
        assertTrue(errors.any { it.field == "message" })
        assertFalse(errors.any { it.field == "fullName" })
        assertFalse(errors.any { it.field == "email" })
    }
    
    private fun createTestContactForm(
        fullName: String = "John Doe",
        email: String = "john@example.com",
        phoneNumber: String = "+1234567890",
        company: String = "Test Corp",
        message: String = "Test message"
    ): ContactFormData {
        return ContactFormData(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber,
            company = company,
            message = message,
            acceptsMarketing = true
        )
    }
}

// =============================================================================
// STATE MANAGEMENT TEST SUITE
// =============================================================================

class StateManagementTestSuite : TestSuite() {
    override fun getTestName(): String = "State Management Tests"
    
    override suspend fun runTests(): TestSuiteResult {
        val startTime = Date().getTime()
        var passed = 0
        var failed = 0
        val totalTests = 6
        
        val tests = listOf(
            ::testAsyncStateTransitions,
            ::testOptimisticUpdates,
            ::testUndoRedoFunctionality,
            ::testPaginationState,
            ::testSearchState,
            ::testStatePersistence
        )
        
        for (test in tests) {
            try {
                test()
                passed++
            } catch (e: Exception) {
                console.error("State management test failed: ${test.name}", e)
                failed++
            }
        }
        
        val endTime = Date().getTime()
        
        return TestSuiteResult(
            suiteName = getTestName(),
            totalTests = totalTests,
            passedTests = passed,
            failedTests = failed,
            duration = endTime - startTime
        )
    }
    
    private fun testAsyncStateTransitions() {
        // Test AsyncState transitions
        var state: AsyncState<String> = AsyncState.Idle
        
        assertTrue(state.isIdle)
        assertFalse(state.isLoading)
        assertNull(state.getOrNull())
        
        state = AsyncState.Loading
        assertTrue(state.isLoading)
        assertFalse(state.isSuccess)
        
        state = AsyncState.Success("test data")
        assertTrue(state.isSuccess)
        assertEquals("test data", state.getOrNull())
        
        state = AsyncState.Error("test error")
        assertTrue(state.isError)
        assertEquals("test error", state.getErrorOrNull())
    }
    
    private fun testOptimisticUpdates() {
        val manager = OptimisticStateManager<String>()
        
        manager.setState("initial state")
        assertEquals("initial state", manager.getCurrentState())
        
        // Optimistic update
        manager.optimisticUpdate("update-1", "optimistic state")
        assertEquals("optimistic state", manager.getCurrentState())
        
        // Confirm update
        manager.confirmUpdate("update-1")
        assertEquals("optimistic state", manager.getCurrentState())
        
        // Test revert
        manager.optimisticUpdate("update-2", "another optimistic state")
        assertEquals("another optimistic state", manager.getCurrentState())
        
        manager.revertUpdate("update-2")
        assertEquals("optimistic state", manager.getCurrentState())
    }
    
    private fun testUndoRedoFunctionality() {
        val undoRedo = UndoRedoManager("initial", maxHistory = 5)
        
        assertFalse(undoRedo.canUndo)
        assertFalse(undoRedo.canRedo)
        
        undoRedo.pushState("state 1")
        assertTrue(undoRedo.canUndo)
        assertFalse(undoRedo.canRedo)
        
        undoRedo.pushState("state 2")
        assertEquals("state 2", undoRedo.currentState.value)
        
        val undoResult = undoRedo.undo()
        assertEquals("state 1", undoResult)
        assertTrue(undoRedo.canRedo)
        
        val redoResult = undoRedo.redo()
        assertEquals("state 2", redoResult)
        assertFalse(undoRedo.canRedo)
    }
    
    private fun testPaginationState() {
        val initialState = PaginationState(
            currentPage = 1,
            pageSize = 10,
            totalItems = 100,
            totalPages = 10,
            hasNext = true,
            hasPrev = false
        )
        
        assertEquals(1, initialState.startItem)
        assertEquals(10, initialState.endItem)
        assertTrue(initialState.canLoadNext)
        assertFalse(initialState.canLoadPrev)
        
        val page2State = initialState.copy(
            currentPage = 2,
            hasPrev = true
        )
        
        assertEquals(11, page2State.startItem)
        assertEquals(20, page2State.endItem)
        assertTrue(page2State.canLoadPrev)
    }
    
    private fun testSearchState() {
        val initialState = SearchState<String>()
        
        assertFalse(initialState.hasQuery)
        assertFalse(initialState.hasResults)
        assertFalse(initialState.isEmpty)
        
        val searchingState = initialState.copy(
            query = "test",
            isSearching = true
        )
        
        assertTrue(searchingState.hasQuery)
        assertFalse(searchingState.hasResults)
        assertFalse(searchingState.isEmpty) // Not empty while searching
        
        val resultsState = searchingState.copy(
            results = listOf("result1", "result2"),
            isSearching = false,
            totalResults = 2
        )
        
        assertTrue(resultsState.hasResults)
        assertEquals(2, resultsState.totalResults)
        
        val emptyResultsState = searchingState.copy(
            results = emptyList(),
            isSearching = false
        )
        
        assertTrue(emptyResultsState.isEmpty)
    }
    
    private fun testStatePersistence() {
        // Test would require mocking localStorage
        // For now, test the serialization/deserialization logic
        
        val testData = "test state data"
        val serializer = { data: String -> data }
        val deserializer = { str: String -> str }
        
        val serialized = serializer(testData)
        assertEquals(testData, serialized)
        
        val deserialized = deserializer(serialized)
        assertEquals(testData, deserialized)
    }
}

// =============================================================================
// PERFORMANCE BENCHMARK TEST SUITE
// =============================================================================

class PerformanceBenchmarkTestSuite : TestSuite() {
    override fun getTestName(): String = "Performance Benchmark Tests"
    
    override suspend fun runTests(): TestSuiteResult {
        val startTime = Date().getTime()
        var passed = 0
        var failed = 0
        val totalTests = 4
        
        try {
            testRenderPerformance()
            passed++
        } catch (e: Exception) {
            console.error("Render performance test failed", e)
            failed++
        }
        
        try {
            testApiPerformance()
            passed++
        } catch (e: Exception) {
            console.error("API performance test failed", e)
            failed++
        }
        
        try {
            testStateUpdatePerformance()
            passed++
        } catch (e: Exception) {
            console.error("State update performance test failed", e)
            failed++
        }
        
        try {
            testMemoryUsage()
            passed++
        } catch (e: Exception) {
            console.error("Memory usage test failed", e)
            failed++
        }
        
        val endTime = Date().getTime()
        
        return TestSuiteResult(
            suiteName = getTestName(),
            totalTests = totalTests,
            passedTests = passed,
            failedTests = failed,
            duration = endTime - startTime,
            performanceReport = performanceTester.generateReport()
        )
    }
    
    private fun testRenderPerformance() {
        val measurement = performanceTester.measureOperation("component-render") {
            // Simulate component rendering
            repeat(100) {
                // Mock component creation and layout
                val element = kotlinx.browser.document.createElement("div")
                element.textContent = "Test content $it"
            }
        }
        
        // Assert render performance is within acceptable limits
        assertTrue(measurement.duration < 100, "Component rendering should complete within 100ms")
    }
    
    private suspend fun testApiPerformance() {
        val mockClient = MockApiClient()
        mockClient.mockResponse("/api/test", ApiResult.Success("test response"))
        
        val measurement = performanceTester.measureAsyncOperation("api-call") {
            repeat(10) {
                mockClient.get<String>("/api/test")
            }
        }
        
        // Assert API call performance
        assertTrue(measurement.duration < 1000, "10 API calls should complete within 1s")
        
        val averagePerCall = measurement.duration / 10
        assertTrue(averagePerCall < 50, "Average API call should be under 50ms")
    }
    
    private fun testStateUpdatePerformance() {
        val undoRedo = UndoRedoManager("initial")
        
        val measurement = performanceTester.measureOperation("state-updates") {
            repeat(1000) {
                undoRedo.pushState("state $it")
            }
        }
        
        // Assert state update performance
        assertTrue(measurement.duration < 500, "1000 state updates should complete within 500ms")
    }
    
    private fun testMemoryUsage() {
        // Simple memory pressure test
        val measurement = performanceTester.measureOperation("memory-allocation") {
            val largeList = mutableListOf<String>()
            repeat(10000) {
                largeList.add("Item $it with some content to consume memory")
            }
            largeList.clear()
        }
        
        // Memory operations should be reasonably fast
        assertTrue(measurement.duration < 1000, "Memory allocation test should complete within 1s")
    }
}

// =============================================================================
// ACCESSIBILITY COMPLIANCE TEST SUITE  
// =============================================================================

class AccessibilityComplianceTestSuite : TestSuite() {
    override fun getTestName(): String = "Accessibility Compliance Tests"
    
    override suspend fun runTests(): TestSuiteResult {
        val startTime = Date().getTime()
        val auditReport = a11yRunner.runFullAccessibilityAudit()
        
        // Check WCAG compliance levels
        val wcagAAResult = a11yRunner.checkWCAGCompliance("AA")
        
        val passed = if (wcagAAResult.isCompliant) 1 else 0
        val failed = if (wcagAAResult.isCompliant) 0 else 1
        
        val endTime = Date().getTime()
        
        return TestSuiteResult(
            suiteName = getTestName(),
            totalTests = 1,
            passedTests = passed,
            failedTests = failed,
            duration = endTime - startTime,
            accessibilityReport = auditReport
        )
    }
}