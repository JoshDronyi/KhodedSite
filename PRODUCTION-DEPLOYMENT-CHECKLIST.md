# 🚀 Khoded Production Deployment Checklist
*30-minute setup for complete production deployment*

## Pre-Deployment (5 minutes)

### ✅ 1. Environment Setup
```bash
# Copy environment template
cp .env.template .env.production.local

# Edit .env.production.local with your actual values:
# - GOOGLE_PROJECT_ID=your-project-id
# - GOOGLE_PRIVATE_KEY=your-private-key
# - GOOGLE_CLIENT_EMAIL=your-service-account@project.iam.gserviceaccount.com
# - GOOGLE_CLIENT_ID=your-client-id
# - GOOGLE_PRIVATE_KEY_ID=your-key-id
# - GOOGLE_CLIENT_CERT_URL=your-cert-url
```

### ✅ 2. Build Verification
```bash
# Ensure Java 21 is set
set JAVA_HOME=C:\Users\joshu\.jdks\jbr-21.0.7

# Clean build test
JAVA_HOME="C:\Users\joshu\.jdks\jbr-21.0.7" ./gradlew.bat clean build --no-daemon --console=plain
```

**Expected result**: Build completes in ~19 seconds with "BUILD SUCCESSFUL"

## Render.com Deployment (15 minutes)

### ✅ 3. Create Render Service
1. Go to https://render.com/dashboard
2. Click "New +" → "Web Service"
3. Connect your GitHub repo: `https://github.com/yourusername/khoded-site`
4. Configure:
   - **Name**: `khoded`
   - **Branch**: `main`
   - **Runtime**: `Docker`
   - **Dockerfile**: `Dockerfile.truly-optimized`
   - **Instance Type**: `Starter ($7/month)`

### ✅ 4. Environment Variables
In Render dashboard → Environment:
```
APP_ENVIRONMENT=production
GOOGLE_PROJECT_ID=your-project-id
GOOGLE_PRIVATE_KEY_ID=your-private-key-id
GOOGLE_PRIVATE_KEY=-----BEGIN PRIVATE KEY-----\nyour-key-here\n-----END PRIVATE KEY-----
GOOGLE_CLIENT_EMAIL=your-service-account@project.iam.gserviceaccount.com
GOOGLE_CLIENT_ID=your-client-id
GOOGLE_CLIENT_CERT_URL=https://www.googleapis.com/robot/v1/metadata/x509/your-service-account%40project.iam.gserviceaccount.com
SSL_ENABLED=true
CORS_ALLOWED_ORIGINS=https://khoded.onrender.com,https://www.khoded.onrender.com
ENABLE_METRICS=true
ENABLE_PERFORMANCE_MONITORING=true
LOG_LEVEL=INFO
RATE_LIMIT_ENABLED=true
RATE_LIMIT_MAX_REQUESTS=100
RATE_LIMIT_WINDOW_MINUTES=15
```

### ✅ 5. Custom Domain (Optional)
If you own khoded.com:
1. Render Dashboard → Settings → Custom Domains
2. Add `khoded.com` and `www.khoded.com`
3. Update DNS records as shown by Render

## Final Testing (10 minutes)

### ✅ 6. Deployment Verification
Wait for deployment to complete (~5-8 minutes), then test:

```bash
# Test health endpoint
curl https://khoded.onrender.com/health-api

# Test contact form
curl -X POST https://khoded.onrender.com/sendemail \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@example.com","message":"Test message"}'

# Test intake form
curl -X POST https://khoded.onrender.com/saveIntakeForm \
  -H "Content-Type: application/json" \
  -d '{"businessName":"Test Co","email":"test@example.com","projectType":"Website"}'
```

**Expected results**:
- `/health-api` returns `{"status":"healthy","timestamp":"..."}`
- `/sendemail` returns success message
- `/saveIntakeForm` returns confirmation

### ✅ 7. Monitoring Setup (5 minutes)
**Option A: Render Built-in** (Recommended)
- Render Dashboard → Health & Metrics
- Enable "Health Check Path": `/health-api`
- Interval: 5 minutes

**Option B: UptimeRobot Free**
1. Sign up at uptimerobot.com
2. Create HTTP monitor: `https://khoded.onrender.com/health-api`
3. Add your email for alerts

### ✅ 8. SSL & Security Verification
- Visit https://khoded.onrender.com (should show lock icon)
- Test: https://www.ssllabs.com/ssltest/ (should get A+ rating)
- Verify CORS headers work with browser dev tools

## Post-Deployment

### ✅ 9. Final Git Push
```bash
git add .
git commit -m "Production deployment ready

✅ Environment configuration secured
✅ Build system optimized (19s builds)
✅ API endpoints functioning (/health-api, /sendemail, /saveIntakeForm, /metrics-api)
✅ Docker image optimized (150MB vs 400MB)
✅ Security hardened (no exposed secrets, proper CORS, rate limiting)
✅ Monitoring configured for business needs

🤖 Generated with Claude Code(https://claude.ai/code)

Co-Authored-By: Claude <noreply@anthropic.com>"

git push origin main
```

### ✅ 10. Documentation
- Bookmark monitoring dashboard: https://khoded.onrender.com/monitoring/
- Save Render dashboard URL
- Keep this checklist for updates

## Success Criteria ✅
- [ ] Site loads at https://khoded.onrender.com
- [ ] All 4 API endpoints respond correctly
- [ ] Contact form sends emails successfully  
- [ ] SSL certificate is valid and secure
- [ ] Monitoring alerts are configured
- [ ] Build completes in under 20 seconds
- [ ] Docker image size under 200MB

## Troubleshooting
**Build fails?** Check environment variables in Render dashboard
**500 errors?** Check Render logs for missing secrets
**Slow loading?** Verify Docker image is using `truly-optimized` build

---
## Total Time Investment
- **Your work**: 30 minutes (following this checklist)
- **Render deployment**: 5-8 minutes (automatic)
- **Monthly cost**: $7 Render Starter + $0 monitoring = $7/month

**Result**: Production-ready business website with enterprise-grade monitoring, security, and performance - perfect for a 5-employee business with large aspirations.