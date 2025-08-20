package com.probro.khoded.components.forms

import com.probro.khoded.components.ui.models.ValidationResult
import com.probro.khoded.components.ui.models.ValidationSeverity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ValidatedContactFormTest {

    @Test
    fun `ValidationResult should handle valid results`() {
        val validResult = ValidationResult(
            isValid = true,
            message = "",
            severity = ValidationSeverity.Success
        )
        
        assertTrue(validResult.isValid)
        assertEquals("", validResult.message)
        assertEquals(ValidationSeverity.Success, validResult.severity)
    }

    @Test
    fun `ValidationResult should handle invalid results`() {
        val invalidResult = ValidationResult(
            isValid = false,
            message = "This field is required",
            severity = ValidationSeverity.Error
        )
        
        assertFalse(invalidResult.isValid)
        assertEquals("This field is required", invalidResult.message)
        assertEquals(ValidationSeverity.Error, invalidResult.severity)
    }

    @Test
    fun `ValidationResult should default to correct severity`() {
        val validResult = ValidationResult(isValid = true)
        assertEquals(ValidationSeverity.Success, validResult.severity)
        
        val invalidResult = ValidationResult(isValid = false, message = "Error")
        assertEquals(ValidationSeverity.Error, invalidResult.severity)
    }

    @Test
    fun `ValidationSeverity should have all expected values`() {
        val success = ValidationSeverity.Success
        val warning = ValidationSeverity.Warning
        val error = ValidationSeverity.Error
        
        // Just testing that these compile and are accessible
        assertEquals(ValidationSeverity.Success, success)
        assertEquals(ValidationSeverity.Warning, warning)
        assertEquals(ValidationSeverity.Error, error)
    }
}