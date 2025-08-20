# 📋 Copy-Paste Environment Setup

## 🚀 **Quick Setup (Just Copy & Paste)**

Run these commands in your project root directory:

### **For Development:**
```bash
cp env-files/.env.development .env.development
```

### **For Staging:**
```bash
cp env-files/.env.staging .env.staging
```

### **For Production:**
```bash
cp env-files/.env.production .env.production
```

### **Set Active Environment:**
```bash
# For development (default)
cp .env.development .env

# OR for staging
cp .env.staging .env

# OR for production  
cp .env.production .env
```

## 🔑 **What You Need to Replace**

### **Staging Environment (.env.staging):**
After copying, replace these values in `.env.staging`:
- `REPLACE_WITH_STAGING_DB_PASSWORD` → Your staging database password
- `REPLACE_WITH_STAGING_PRIVATE_KEY_ID` → From Khoded-staging Google service account
- `REPLACE_WITH_STAGING_PRIVATE_KEY` → From Khoded-staging Google service account  
- `REPLACE_WITH_STAGING_CLIENT_ID` → From Khoded-staging Google service account

### **Production Environment (.env.production):**
After copying, replace these values in `.env.production`:
- `REPLACE_WITH_PRODUCTION_DB_PASSWORD` → Your production database password
- `REPLACE_WITH_PROD_PRIVATE_KEY_ID` → From KhodedProd Google service account
- `REPLACE_WITH_PROD_PRIVATE_KEY` → From KhodedProd Google service account
- `REPLACE_WITH_PROD_CLIENT_ID` → From KhodedProd Google service account

### **Development Environment (.env.development):**
✅ **Ready to use!** No changes needed - uses local PostgreSQL with default credentials.

## 📁 **File Locations After Setup**
```
Khoded Site/
├── .env                    # Active environment (copy of one of the below)
├── .env.development       # Development config (ready to use)
├── .env.staging          # Staging config (needs Gmail keys)
├── .env.production       # Production config (needs Gmail keys)
└── env-files/            # Source files (don't edit these)
    ├── .env.development
    ├── .env.staging
    └── .env.production
```

## 🎯 **Gmail Service Account Keys**

### **Get Keys from Google Cloud Console:**

**For KhodedProd (Production):**
1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Select "KhodedProd" project
3. Go to IAM & Admin → Service Accounts
4. Find `khoded-service@khodedprod.iam.gserviceaccount.com`
5. Click "Keys" → "Add Key" → "Create new key" → JSON
6. Extract values for .env.production

**For Khoded-staging (Staging):**
1. Select "Khoded-staging" project
2. Go to IAM & Admin → Service Accounts  
3. Find `khoded-staging-service@khoded-staging.iam.gserviceaccount.com`
4. Click "Keys" → "Add Key" → "Create new key" → JSON
5. Extract values for .env.staging

## ✅ **Verification**
After setup, verify your configuration:

```bash
# Check files exist and are not tracked by git
ls -la .env*
git status  # Should NOT show .env files

# Test development environment
cp .env.development .env
# Your app should start successfully
```

## 🔐 **Security Reminder**
- ✅ These files are gitignored and won't be committed
- ✅ Never share .env files in chat or email
- ✅ Use environment variables in production (Render Dashboard)
- ✅ Rotate service account keys regularly