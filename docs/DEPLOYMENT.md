# Khoded Deployment Guide

## Quick Start Production Deployment

### Prerequisites
- Docker and Docker Compose installed
- Domain name configured (khoded.com)
- SSL certificates ready
- Gmail service account credentials

### Environment Setup
1. Copy `.env.example` to `.env`
2. Configure all required environment variables
3. Set up Gmail service account JSON
4. Configure database credentials

### Deployment Commands
```bash
# Build and deploy
docker-compose up -d

# Check health
curl https://khoded.com/health

# View logs
docker-compose logs -f app
```

## Security Checklist
- [ ] SSL certificates installed
- [ ] Rate limiting configured
- [ ] Security headers enabled
- [ ] Service account credentials secured
- [ ] Database access restricted

## Monitoring Setup
- [ ] Prometheus metrics enabled
- [ ] Alert rules configured
- [ ] Health checks responding
- [ ] Log aggregation working

For detailed instructions, see PRODUCTION-READY.md