# Contact Form Setup Instructions

## ✅ Status: FULLY FUNCTIONAL

The contact form is now fully integrated and production-ready with comprehensive validation, security features, and error handling.

## 🚀 What's Working

### Frontend Contact Form
- **Location**: `/contact` page
- **Component**: `ValidatedContactForm.kt`
- **Features**: 
  - Real-time field validation
  - Secure API submission
  - Rate limiting protection
  - WCAG accessibility compliance
  - User-friendly error messages
  - Success confirmation

### Backend API Endpoint  
- **Endpoint**: `/api/sendemail`
- **Method**: POST (supports both JSON and form data)
- **Security**:
  - Rate limiting (5 requests per 15 minutes per IP)
  - Comprehensive input validation and sanitization
  - XSS and SQL injection protection
  - Security headers implementation
  - Spam detection

### Email Service Integration
- **Service**: Gmail API via service account
- **Configuration**: Environment variable based
- **Features**: 
  - Professional email templates
  - Delivery confirmation
  - Error handling and retry logic

## 📋 Environment Setup Required

### 1. Gmail Service Account Setup

1. **Create Google Cloud Project**
   ```bash
   # Go to Google Cloud Console
   # Create new project or select existing one
   ```

2. **Enable Gmail API**
   ```bash
   # In Google Cloud Console:
   # APIs & Services > Library > Search "Gmail API" > Enable
   ```

3. **Create Service Account**
   ```bash
   # IAM & Admin > Service Accounts > Create Service Account
   # Download JSON key file
   ```

4. **Configure Environment Variables**
   
   Copy `.env.template` to `.env` and configure:
   ```env
   # Gmail Service Account Configuration
   GOOGLE_SERVICE_ACCOUNT_EMAIL=your-service-account@your-project.iam.gserviceaccount.com
   GOOGLE_PRIVATE_KEY=-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----
   GOOGLE_PRIVATE_KEY_ID=your-private-key-id
   GOOGLE_CLIENT_EMAIL=your-service-account@your-project.iam.gserviceaccount.com
   GOOGLE_CLIENT_ID=your-client-id
   GOOGLE_PROJECT_ID=your-google-cloud-project-id
   
   # Email Configuration
   ADMIN_EMAIL=admin@khoded.com
   FROM_EMAIL=noreply@khoded.com
   
   # Database Configuration (PostgreSQL)
   DATABASE_URL=jdbc:postgresql://localhost:5432/khoded_db
   DATABASE_USERNAME=khoded_user
   DATABASE_PASSWORD=secure_password
   
   # Application Configuration
   SERVER_PORT=8080
   SERVER_HOST=0.0.0.0
   ```

### 2. Gmail Delegation (Important!)

For the service account to send emails, you need to set up domain-wide delegation:

1. **In Google Admin Console** (if using Google Workspace):
   ```
   Security > API Controls > Domain-wide Delegation
   Add the service account client ID
   Authorize scopes: https://www.googleapis.com/auth/gmail.send
   ```

2. **Alternative**: Use OAuth2 for personal Gmail accounts

### 3. Database Setup

```sql
-- PostgreSQL setup
CREATE DATABASE khoded_db;
CREATE USER khoded_user WITH ENCRYPTED PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE khoded_db TO khoded_user;

-- Create contact submissions table
CREATE TABLE contact_submissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'pending'
);
```

## 🏗️ Deployment Instructions

### Option 1: Docker Deployment (Recommended)

1. **Configure Environment**
   ```bash
   cp .env.template .env
   # Edit .env with your actual values
   ```

2. **Build and Deploy**
   ```bash
   docker-compose up -d
   ```

3. **Verify Deployment**
   ```bash
   curl http://localhost:8080/health
   # Should return: {"status": "healthy"}
   ```

### Option 2: Manual Deployment

1. **Build Application**
   ```bash
   ./gradlew clean build
   ```

2. **Setup Environment**
   ```bash
   export DATABASE_URL="jdbc:postgresql://localhost:5432/khoded_db"
   export GOOGLE_SERVICE_ACCOUNT_EMAIL="your-service-account@project.iam.gserviceaccount.com"
   # ... other environment variables
   ```

3. **Run Application**
   ```bash
   java -jar site/build/libs/site.jar
   ```

## 🧪 Testing the Contact Form

### 1. Frontend Testing
- Navigate to `/contact` page
- Fill out the form with valid data
- Test validation errors with invalid data
- Verify accessibility features

### 2. API Testing
```bash
curl -X POST http://localhost:8080/api/sendemail \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "subject": "Test Message",
    "message": "This is a test message from the contact form."
  }'
```

### 3. Rate Limiting Testing
```bash
# Should be blocked after 5 requests in 15 minutes
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/sendemail -d "name=Test&email=test@test.com&subject=Test&message=Test message $i"
done
```

## 🔧 Troubleshooting

### Common Issues

1. **"Could not authenticate with Gmail API"**
   - Verify service account JSON is correct
   - Check domain-wide delegation is configured
   - Ensure Gmail API is enabled

2. **"Rate limit exceeded"**
   - Normal behavior after 5 requests per IP per 15 minutes
   - Wait or configure different rate limits

3. **"Database connection failed"**
   - Verify PostgreSQL is running
   - Check DATABASE_URL format
   - Confirm user permissions

4. **"Form validation errors"**
   - Check input lengths and formats
   - Verify email format is valid
   - Ensure message is 10-1000 characters

## 📊 Monitoring

### Logs Location
- Application logs: `stdout` (or configured log file)
- Email delivery logs: Google Cloud Console > Logging
- Database logs: PostgreSQL logs

### Key Metrics to Monitor
- Form submission rate
- Email delivery success rate
- Rate limiting triggers
- Validation error frequency
- Response times

## 🛡️ Security Features Active

- ✅ Input validation and sanitization
- ✅ Rate limiting (5 requests/15min/IP)
- ✅ XSS protection
- ✅ SQL injection prevention
- ✅ CSRF protection via secure headers
- ✅ Spam detection
- ✅ Email format validation
- ✅ Secure environment variable usage

## 📈 Production Readiness Checklist

- ✅ Contact form integrated on `/contact` page
- ✅ Backend API endpoint `/api/sendemail` functional
- ✅ Comprehensive input validation
- ✅ Security headers implemented
- ✅ Rate limiting active
- ✅ Error handling and logging
- ✅ Environment variable configuration
- ✅ Docker deployment ready
- ✅ Database schema defined
- ✅ Testing documentation provided

**The contact form is now fully functional and production-ready!**