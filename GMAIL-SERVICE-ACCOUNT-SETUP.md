# Gmail Service Account Setup Guide

This guide explains how to properly configure Gmail API with Google Cloud Service Account for the Khoded project.

## ✅ **Current Implementation Status**

The Khoded project is **already configured** to use Google Cloud Service Account authentication with Gmail API:

- ✅ Service Account JWT authentication (not OAuth)
- ✅ Proper PKCS#8 private key handling
- ✅ Comprehensive error handling with specific solutions
- ✅ Exponential backoff retry logic for rate limiting
- ✅ Docker secrets integration for production security
- ✅ Health check endpoint with credential validation

## 🚀 **Quick Setup (5 minutes)**

### Step 1: Create Google Cloud Service Account

1. **Go to Google Cloud Console**
   - Visit: https://console.cloud.google.com/
   - Select or create a project

2. **Enable Gmail API**
   ```bash
   # Navigate to APIs & Services > Library
   # Search for "Gmail API" and enable it
   ```

3. **Create Service Account**
   ```bash
   # Go to: IAM & Admin > Service Accounts
   # Click "Create Service Account"
   
   Name: khoded-gmail-service
   Description: Service account for Khoded contact form emails
   ```

4. **Generate Private Key**
   ```bash
   # Click on the created service account
   # Go to "Keys" tab
   # Click "Add Key" > "Create New Key"
   # Choose "JSON" format
   # Download the key file
   ```

### Step 2: Configure Production Secrets

1. **Place the downloaded JSON file**:
   ```bash
   # Save the downloaded service account JSON as:
   ./secrets/gmail_service_account_key.json
   ```

2. **Verify the JSON structure**:
   ```json
   {
     "type": "service_account",
     "project_id": "your-project-id",
     "private_key_id": "key-id",
     "private_key": "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n",
     "client_email": "khoded-gmail-service@your-project.iam.gserviceaccount.com",
     "client_id": "123456789012345678901",
     "auth_uri": "https://accounts.google.com/o/oauth2/auth",
     "token_uri": "https://oauth2.googleapis.com/token",
     "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
     "client_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs/...",
     "universe_domain": "googleapis.com"
   }
   ```

3. **Set the client email** (for reference):
   ```bash
   # Create this file with just the email address:
   echo "khoded-gmail-service@your-project.iam.gserviceaccount.com" > ./secrets/gmail_client_email.txt
   ```

### Step 3: Test the Configuration

1. **Deploy and test**:
   ```bash
   ./deploy-production.sh
   ```

2. **Check health endpoint**:
   ```bash
   curl -s http://localhost:8080/health | jq '.checks.gmail_service_account'
   ```

   Expected response:
   ```json
   {
     "status": "UP",
     "message": "Gmail service account configured correctly",
     "details": {
       "project_id": "your-project-id",
       "client_email": "khoded-gmail-service@your-project.iam.gserviceaccount.com",
       "type": "service_account",
       "universe_domain": "googleapis.com"
     }
   }
   ```

## 📋 **Configuration Methods**

The application supports three configuration methods (in priority order):

### 1. Docker Secrets (Production) ⭐ **RECOMMENDED**
```bash
# File: ./secrets/gmail_service_account_key.json
{
  "type": "service_account",
  "project_id": "your-project",
  // ... full service account JSON
}
```

### 2. Environment Variable (Development)
```bash
export GOOGLE_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
```

### 3. Build Configuration (Legacy)
Update `build.gradle.kts` buildkonfig section (not recommended for production).

## 🔒 **Security Features**

### Production Security
- ✅ **Docker Secrets**: Credentials stored securely, not in environment variables
- ✅ **No Logging**: Private keys never logged or exposed
- ✅ **Token Caching**: Access tokens cached securely with proper expiration
- ✅ **Validation**: Comprehensive credential validation at startup

### Network Security
- ✅ **HTTPS Only**: All Gmail API calls over TLS
- ✅ **JWT Authentication**: Industry-standard service account authentication
- ✅ **Rate Limiting**: Automatic handling of API rate limits
- ✅ **Retry Logic**: Exponential backoff for transient failures

## 📊 **Monitoring & Health Checks**

### Health Check Endpoint
```bash
GET /health
```

Returns detailed status of:
- Gmail service account configuration
- Credential validation
- API connectivity
- Token generation capability

### Monitoring Alerts
The production monitoring stack includes alerts for:
- Gmail API authentication failures
- High error rates from Gmail API
- Service account credential expiration warnings

## 🐛 **Troubleshooting**

### Common Issues and Solutions

#### ❌ "Service account key file not found"
```bash
# Solution: Ensure the key file exists
ls -la ./secrets/gmail_service_account_key.json

# If missing, download from Google Cloud Console
```

#### ❌ "JWT creation error: Invalid service account credentials"
```bash
# Solution: Verify JSON format and private key
cat ./secrets/gmail_service_account_key.json | jq '.private_key' | head -1
# Should show: "-----BEGIN PRIVATE KEY-----..."

# Check for common issues:
# - Missing quotes around private key
# - Corrupted newlines in private key
# - Wrong key format (should be PKCS#8)
```

#### ❌ "Gmail API Forbidden (403)"
```bash
# Solution: Enable Gmail API in Google Cloud Console
# 1. Go to APIs & Services > Library
# 2. Search "Gmail API" and enable it
# 3. Verify service account has necessary permissions
```

#### ❌ "Gmail API Bad Request (400)"
```bash
# Solution: Check email format and content
# - Verify recipient email format
# - Check message size (must be < 25MB)
# - Ensure proper MIME formatting
```

### Health Check Debugging
```bash
# Get detailed configuration info
curl -s http://localhost:8080/health | jq '.checks.gmail_service_account.details'

# Check application logs
docker-compose -f docker-compose.production.yaml logs khoded-app | grep -i gmail
```

## 📈 **Performance & Limits**

### Gmail API Quotas
- **Daily Limit**: 1 billion requests/day (shared across all apps)
- **Per-User Limit**: 250 quota units per user per second
- **Mail Send Limit**: 1,000,000,000 emails/day

### Optimizations Implemented
- ✅ **Token Caching**: Access tokens cached for 55 minutes (5-minute buffer)
- ✅ **Connection Pooling**: HTTP client reused across requests
- ✅ **Retry Logic**: Automatic handling of rate limits and server errors
- ✅ **Efficient Encoding**: Minimal MIME message format

## 🔄 **Development vs Production**

### Development Setup
```bash
# Use environment variable for easy development
export GOOGLE_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'

# Or place JSON file and point to it
export GOOGLE_SERVICE_ACCOUNT_KEY_PATH="/path/to/service-account.json"
```

### Production Setup
```bash
# Use Docker secrets (automatic in docker-compose.production.yaml)
./secrets/gmail_service_account_key.json  # Full service account JSON
./secrets/gmail_client_email.txt          # Just the email for reference
```

## ✅ **Verification Checklist**

- [ ] Gmail API enabled in Google Cloud Console
- [ ] Service account created with appropriate name
- [ ] JSON key file downloaded and placed in `./secrets/gmail_service_account_key.json`
- [ ] Health check endpoint returns `"status": "UP"` for Gmail service account
- [ ] Test email can be sent successfully
- [ ] Monitoring alerts configured for Gmail service failures

---

## 🎯 **Summary**

The Khoded Gmail integration is **production-ready** with:
- ✅ Secure service account authentication
- ✅ Comprehensive error handling and retry logic
- ✅ Production-grade monitoring and health checks
- ✅ Docker secrets integration for security
- ✅ Detailed troubleshooting and validation

**Time to configure**: ~5 minutes (just need to create service account and place JSON file)

The implementation follows Google Cloud best practices and includes enterprise-grade error handling, security, and monitoring.