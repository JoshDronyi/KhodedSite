package com.probro.khoded.features.email.service

import com.probro.khoded.features.consultation.models.ConsultationRequest
import com.probro.khoded.features.email.models.*

/**
 * Email Service Interface
 * 
 * Defines the contract for email services and template generation.
 * Separated from implementation for dependency injection and testing.
 */

interface EmailService {
    suspend fun sendEmail(request: EmailRequest): Result<EmailResponse>
    suspend fun sendConsultationConfirmation(consultation: ConsultationRequest): Result<EmailResponse>
    suspend fun sendGeneralInquiry(inquiry: GeneralInquiryRequest): Result<EmailResponse>
    fun generateEmailTemplate(template: EmailTemplate, data: Map<String, Any>): String
}

/**
 * Email Template Generator
 * 
 * Handles template processing and variable substitution for emails.
 */
object EmailTemplateGenerator {
    
    fun generateConsultationConfirmation(consultation: ConsultationRequest): String {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Thank you for your consultation request - Khoded</title>
            <style>
                body { font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                .content { background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px; }
                .highlight { background: #e3f2fd; padding: 15px; border-radius: 5px; margin: 20px 0; }
                .footer { text-align: center; margin-top: 30px; font-size: 14px; color: #666; }
                .logo { font-size: 24px; font-weight: bold; }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="logo">Khoded</div>
                <h1>Thank you for reaching out!</h1>
            </div>
            
            <div class="content">
                <p>Dear ${consultation.personalInfo.firstName},</p>
                
                <p>We have received your consultation request and are excited to learn more about your project. Our team will review your requirements and get back to you within 24 hours.</p>
                
                <div class="highlight">
                    <strong>Your Project Details:</strong><br>
                    <strong>Project Type:</strong> ${consultation.projectInfo.projectType}<br>
                    <strong>Timeline:</strong> ${consultation.projectInfo.timeline}<br>
                    <strong>Budget Range:</strong> ${consultation.projectInfo.budget}
                </div>
                
                <p>In the meantime, feel free to:</p>
                <ul>
                    <li>Check out our <a href="https://khoded.com/portfolio">recent projects</a></li>
                    <li>Follow us on <a href="https://linkedin.com/company/khoded">LinkedIn</a> for updates</li>
                    <li>Review our <a href="https://khoded.com/process">development process</a></li>
                </ul>
                
                <p>We're looking forward to potentially working together to bring your vision to life!</p>
                
                <p>Best regards,<br>
                <strong>The Khoded Team</strong><br>
                Esther & Joshua Dronyi</p>
            </div>
            
            <div class="footer">
                <p>Khoded | Custom Web Development & Digital Solutions</p>
                <p>Email: admin@khoded.com | Website: khoded.com</p>
            </div>
        </body>
        </html>
        """.trimIndent()
    }
    
    fun generateGeneralInquiry(inquiry: GeneralInquiryRequest): String {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Thank you for contacting Khoded</title>
            <style>
                body { font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                .content { background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px; }
                .footer { text-align: center; margin-top: 30px; font-size: 14px; color: #666; }
                .logo { font-size: 24px; font-weight: bold; }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="logo">Khoded</div>
                <h1>Thanks for reaching out!</h1>
            </div>
            
            <div class="content">
                <p>Hi ${inquiry.name},</p>
                
                <p>Thank you for contacting Khoded. We've received your message and will respond within 24 hours.</p>
                
                <p><strong>Your Message:</strong><br>
                Subject: ${inquiry.subject}</p>
                
                <p>We appreciate your interest in our services and look forward to discussing how we can help with your project.</p>
                
                <p>Best regards,<br>
                <strong>The Khoded Team</strong></p>
            </div>
            
            <div class="footer">
                <p>Khoded | Custom Web Development & Digital Solutions</p>
                <p>Email: admin@khoded.com | Website: khoded.com</p>
            </div>
        </body>
        </html>
        """.trimIndent()
    }
    
    fun generateTemplate(template: EmailTemplate, data: Map<String, Any>): String {
        return when (template) {
            EmailTemplate.CONSULTATION_CONFIRMATION -> {
                val consultation = data["consultation"] as? ConsultationRequest
                consultation?.let { generateConsultationConfirmation(it) } ?: ""
            }
            EmailTemplate.GENERAL -> {
                val inquiry = data["inquiry"] as? GeneralInquiryRequest
                inquiry?.let { generateGeneralInquiry(it) } ?: ""
            }
            else -> generateBasicTemplate(template, data)
        }
    }
    
    private fun generateBasicTemplate(template: EmailTemplate, data: Map<String, Any>): String {
        val templateName = template.name.lowercase().replace("_", " ")
        return """
        <html>
        <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
            <h2>Khoded - ${templateName.capitalize()}</h2>
            <p>This is a basic template for ${templateName}.</p>
            <p>Template data: ${data.entries.joinToString(", ") { "${it.key}: ${it.value}" }}</p>
            <br>
            <p>Best regards,<br>The Khoded Team</p>
        </body>
        </html>
        """.trimIndent()
    }
    
    private fun String.capitalize(): String {
        return this.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
    }
}