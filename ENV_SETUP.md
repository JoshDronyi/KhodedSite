# 🔐 Environment Variables Setup Guide

## ⚠️ **Security Notice**
**NEVER commit .env files to git!** They contain sensitive information like:
- Gmail API service account keys
- Database passwords
- API tokens and secrets

## 🚀 **Quick Setup**

### 1. **Local Development**
```bash
# Copy the template
cp .env.template .env.development

# Fill in your actual values (NEVER commit this file!)
# Edit .env.development with your local database and Gmail settings
```

### 2. **Production (Render.com)**
Set environment variables in Render Dashboard:
- DATABASE_URL (auto-provided by Render)
- GMAIL_SERVICE_ACCOUNT_JSON (from Google Cloud)
- APP_ENVIRONMENT=production

### 3. **CI/CD (GitHub Actions)**
Environment variables are automatically generated from `.env.template` for testing.

## 📋 **Required Environment Variables**

### **Core Application**
- `APP_ENVIRONMENT`: development, staging, production
- `PRIMARY_DOMAIN`: Your application domain
- `SERVER_PORT`: Application port (default: 8080)

### **Database (Local Development)**
- `DATABASE_DEV_URI`: PostgreSQL connection string
- `DATABASE_DEV_USERNAME`: Database username
- `DATABASE_DEV_PASSWORD`: Database password

### **Gmail API (Production)**
- `GOOGLE_PROJECT_ID`: KhodedProd (production) or Khoded-staging (staging)
- `GOOGLE_CLIENT_EMAIL`: Service account email
- `GOOGLE_PRIVATE_KEY`: Service account private key
- `GOOGLE_PRIVATE_KEY_ID`: Service account key ID
- `GOOGLE_CLIENT_ID`: Service account client ID

### **Security & Features**
- `SSL_ENABLED`: true/false
- `CORS_ALLOWED_ORIGINS`: Allowed origins for CORS
- `LOG_LEVEL`: INFO, DEBUG, WARN, ERROR

## 🔄 **How CI/CD Works Securely**

1. **No .env files in git**: All .env files are in .gitignore
2. **Template-based**: CI uses `.env.template` with safe defaults
3. **GitHub Secrets**: Production secrets stored in GitHub repository secrets
4. **Environment-specific**: Each environment gets appropriate configuration

## 🛠️ **Setup Instructions**

### **For Developers**
```bash
# 1. Clone repository
git clone <repository-url>

# 2. Copy template and configure
cp .env.template .env.development

# 3. Fill in your local values
# Edit .env.development with your local database credentials

# 4. Never commit .env files!
git status  # Should not show any .env files
```

### **For Production Deployment**
1. Set up Gmail service account in Google Cloud (KhodedProd project)
2. Configure environment variables in Render Dashboard
3. Deploy using render.yaml Blueprint

### **For GitHub Secrets (CI/CD)**
Add these secrets in GitHub repository settings:
- `RENDER_API_KEY`: For automatic deployment
- `RENDER_SERVICE_ID`: For deployment targeting

## 🚨 **Security Best Practices**

✅ **DO:**
- Use `.env.template` for documentation
- Set sensitive values in production environment variables
- Use GitHub secrets for CI/CD credentials
- Regularly rotate service account keys

❌ **DON'T:**
- Commit .env files to git
- Share .env files in chat/email
- Hardcode secrets in code
- Use production credentials in development

## 📁 **File Structure**
```
├── .env.template          # Template with safe defaults (committed)
├── .env.development       # Local dev config (gitignored)
├── .env.staging          # Local staging config (gitignored)
├── .env.production       # Local prod config (gitignored)
├── .env                  # Current active config (gitignored)
└── secrets/              # Service account keys (gitignored)
```

## 🔍 **Troubleshooting**

**CI/CD failing with missing .env files?**
- ✅ Check `.env.template` exists and is committed
- ✅ Verify .gitignore excludes .env files
- ✅ Update CI/CD workflow to use template

**Local development not working?**
- ✅ Copy `.env.template` to `.env.development`
- ✅ Fill in your local database credentials
- ✅ Check Gmail service account configuration

**Production deployment failing?**
- ✅ Verify environment variables in Render Dashboard
- ✅ Check Gmail service account permissions
- ✅ Ensure DATABASE_URL is automatically provided

## 📚 **Related Documentation**
- [Render Deployment Guide](./RENDER_DEPLOYMENT_GUIDE.md)
- [Environment Setup](./ENVIRONMENTS.md)
- [Next Steps](./RENDER_NEXT_STEPS.md)