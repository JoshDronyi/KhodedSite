# Production Deployment Checklist (30 Minutes)
*Optimized for 5-employee small business with growth aspirations*

## Pre-Deployment Setup (15 minutes)

### 1. Environment Configuration (5 minutes)
- [ ] Copy `.env.production.template` to `.env.production`
- [ ] Update production values:
  ```bash
  DATABASE_PROD_URI=your_render_postgresql_url
  DATABASE_PROD_USERNAME=your_db_username  
  DATABASE_PROD_PASSWORD=your_db_password
  ```
- [ ] Set Gmail API credentials (if using contact form):
  ```bash
  GOOGLE_PROJECT_ID=your_project_id
  GOOGLE_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----\n"
  GOOGLE_CLIENT_EMAIL=your_service_account@project.iam.gserviceaccount.com
  ```

### 2. Security Verification (5 minutes)
- [ ] Confirm no secrets in git: `git log --oneline -10` (check for sensitive commits)
- [ ] Verify `.env.production` is in `.gitignore`
- [ ] Check `secrets/` directory contains only template files

### 3. Final Build Test (5 minutes)
- [ ] Run: `JAVA_HOME="C:\Users\joshu\.jdks\jbr-21.0.7" ./gradlew.bat build --no-daemon --console=plain`
- [ ] Verify build completes in under 30 seconds
- [ ] Confirm all 4 API endpoints appear in build logs:
  - `/health-api`
  - `/sendemail` 
  - `/saveIntakeForm`
  - `/metrics-api`

## Deployment (10 minutes)

### 4. GitHub Push and CI/CD (5 minutes)
- [ ] Commit final changes: `git add . && git commit -m "Production-ready deployment"`
- [ ] Push to main: `git push origin enhancement/MetaTagsAndBasicStructure:main`
- [ ] Monitor GitHub Actions: All checks pass (build completes in ~2 minutes)

### 5. Render.com Deployment (5 minutes)
- [ ] Verify Render service starts successfully
- [ ] Test application health: `curl https://your-app.onrender.com/health-api`
- [ ] Verify contact form works: Submit test message
- [ ] Check application loads completely within 10 seconds

## Post-Deployment Monitoring (5 minutes)

### 6. Optional: Simple Monitoring Setup
*Skip this initially - deploy core application first*
- [ ] **Later**: Run `docker-compose -f docker-compose.monitoring.yml up -d`
- [ ] **Later**: Access Grafana at `http://localhost:3000` (admin/admin123)
- [ ] **Later**: Set up basic alerts for downtime

### 7. Verification & Documentation
- [ ] Document production URL in project README
- [ ] Test all major features work in production
- [ ] Set up basic uptime monitoring (UptimeRobot free tier)

## Success Criteria ✅
- Application builds in under 30 seconds
- All 4 API endpoints functional
- Contact form sends emails successfully  
- No security vulnerabilities or exposed credentials
- Production site loads in under 10 seconds
- All GitHub Actions pass without errors

## Emergency Rollback Plan
If deployment fails:
1. Revert to last known good commit: `git reset --hard HEAD~1`
2. Push rollback: `git push --force-with-lease origin main`
3. Render will automatically redeploy previous version

---
**Total Time Investment: 30 minutes**  
**Result: Fully production-ready application with enterprise-grade security and monitoring foundation**