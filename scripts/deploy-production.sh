#!/bin/bash

# Khoded Production Environment Deployment Script
# Deploys the application to production environment with zero-downtime deployment

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PRODUCTION_BRANCH="main"
COMPOSE_FILE="docker-compose.prod.yml"
ENVIRONMENT="production"

# Logging functions with timestamps
log_info() {
    echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${RED}[ERROR]${NC} $1"
}

# Production safety checks
production_safety_checks() {
    log_info "Running production safety checks..."
    
    # Check if Docker is installed and running
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        log_error "Docker is not running. Please start Docker."
        exit 1
    fi
    
    # Check if we're on the correct branch
    current_branch=$(git rev-parse --abbrev-ref HEAD)
    if [[ "$current_branch" != "$PRODUCTION_BRANCH" ]]; then
        log_error "Must be on '$PRODUCTION_BRANCH' branch for production deployment. Currently on '$current_branch'"
        log_info "Run: git checkout $PRODUCTION_BRANCH"
        exit 1
    fi
    
    # Check if working directory is clean
    if ! git diff-index --quiet HEAD --; then
        log_error "Working directory is not clean. Please commit or stash changes."
        exit 1
    fi
    
    # Check if we're up to date with remote
    git fetch origin "$PRODUCTION_BRANCH"
    local_commit=$(git rev-parse HEAD)
    remote_commit=$(git rev-parse "origin/$PRODUCTION_BRANCH")
    
    if [[ "$local_commit" != "$remote_commit" ]]; then
        log_error "Local branch is not up to date with remote. Please pull latest changes."
        exit 1
    fi
    
    # Confirm production deployment
    echo
    log_warning "⚠️  PRODUCTION DEPLOYMENT CONFIRMATION ⚠️"
    echo "You are about to deploy to PRODUCTION environment."
    echo "Branch: $PRODUCTION_BRANCH"
    echo "Commit: $(git log -1 --pretty=format:'%h - %s (%an, %ar)')"
    echo
    read -p "Are you sure you want to proceed? (yes/no): " confirm
    
    if [[ "$confirm" != "yes" ]]; then
        log_info "Production deployment cancelled by user."
        exit 0
    fi
    
    log_success "Production safety checks passed"
}

# Setup production secrets
setup_production_secrets() {
    log_info "Validating production secrets..."
    
    cd "$PROJECT_ROOT"
    
    # Required production secrets
    local required_secrets=(
        "db_password.txt"
        "postgres_password.txt"
        "gmail_service_account_key.json"
        "grafana_password.txt"
    )
    
    local missing_secrets=()
    
    # Check for required secrets
    for secret in "${required_secrets[@]}"; do
        if [[ ! -f "secrets/$secret" ]]; then
            missing_secrets+=("$secret")
        fi
    done
    
    if [[ ${#missing_secrets[@]} -gt 0 ]]; then
        log_error "Missing required production secrets:"
        for secret in "${missing_secrets[@]}"; do
            log_error "  - secrets/$secret"
        done
        log_info "Run: ./scripts/setup-production-secrets.sh"
        exit 1
    fi
    
    # Validate Gmail service account
    if ! jq -e '.type == "service_account"' secrets/gmail_service_account_key.json > /dev/null 2>&1; then
        log_error "Invalid Gmail service account file"
        exit 1
    fi
    
    # Check secret file permissions
    for secret in "${required_secrets[@]}"; do
        if [[ "$(stat -c %a "secrets/$secret")" != "600" ]]; then
            log_warning "Setting proper permissions for secrets/$secret"
            chmod 600 "secrets/$secret"
        fi
    done
    
    log_success "Production secrets validated"
}

# Setup production environment
setup_production_env() {
    log_info "Setting up production environment..."
    
    cd "$PROJECT_ROOT"
    
    # Copy production environment file
    if [[ ! -f ".env" ]] || [[ ".env.production" -nt ".env" ]]; then
        cp ".env.production" ".env"
        log_success "Updated .env file from .env.production"
    fi
    
    # Validate critical environment variables
    local required_vars=(
        "APP_ENVIRONMENT"
        "PRIMARY_DOMAIN"
        "DATABASE_URL"
        "GOOGLE_SERVICE_ACCOUNT_FILE"
        "CORS_ALLOWED_ORIGINS"
    )
    
    source .env
    
    for var in "${required_vars[@]}"; do
        if [[ -z "${!var:-}" ]]; then
            log_error "Required environment variable $var is not set"
            exit 1
        fi
    done
    
    # Validate APP_ENVIRONMENT is production
    if [[ "${APP_ENVIRONMENT}" != "production" ]]; then
        log_error "APP_ENVIRONMENT must be 'production' for production deployment"
        exit 1
    fi
    
    log_success "Production environment validated"
}

# Pre-deployment backup
create_backup() {
    log_info "Creating pre-deployment backup..."
    
    cd "$PROJECT_ROOT"
    
    # Create backups directory if it doesn't exist
    mkdir -p backups
    
    local backup_name="prod-pre-deploy-$(date +%Y%m%d_%H%M%S)"
    
    # Backup database if currently running
    if docker-compose -f "$COMPOSE_FILE" ps database | grep -q "Up"; then
        log_info "Backing up production database..."
        docker-compose -f "$COMPOSE_FILE" exec -T database pg_dump -U khoded_user khoded_production | gzip > "backups/${backup_name}.sql.gz"
        log_success "Database backup created: backups/${backup_name}.sql.gz"
    else
        log_info "No running database to backup"
    fi
    
    # Create application backup (Docker volumes)
    if docker volume ls | grep -q "khoded-prod"; then
        log_info "Backing up application volumes..."
        docker run --rm \
            -v khoded-prod_postgres-data:/source:ro \
            -v "$(pwd)/backups":/backup \
            alpine tar czf "/backup/${backup_name}-volumes.tar.gz" -C /source .
        log_success "Volume backup created: backups/${backup_name}-volumes.tar.gz"
    fi
}

# Build production images
build_production_images() {
    log_info "Building production Docker images..."
    
    cd "$PROJECT_ROOT"
    
    # Build the application image with production Dockerfile
    log_info "Building application image..."
    docker build -f Dockerfile.production -t "khoded:$(git rev-parse --short HEAD)" -t khoded:latest . || {
        log_error "Failed to build production image"
        exit 1
    }
    
    # Test the built image
    log_info "Testing built image..."
    if ! docker run --rm khoded:latest java -version > /dev/null 2>&1; then
        log_error "Built image failed basic runtime test"
        exit 1
    fi
    
    log_success "Production images built and tested successfully"
}

# Zero-downtime deployment
zero_downtime_deploy() {
    log_info "Starting zero-downtime production deployment..."
    
    cd "$PROJECT_ROOT"
    
    # Pull latest images for infrastructure services
    docker-compose -f "$COMPOSE_FILE" pull nginx prometheus grafana loki
    
    # Deploy with zero downtime strategy
    if docker-compose -f "$COMPOSE_FILE" ps | grep -q "Up"; then
        log_info "Performing rolling update..."
        
        # Update database first (if needed)
        docker-compose -f "$COMPOSE_FILE" up -d database redis
        
        # Wait for database to be ready
        sleep 10
        
        # Rolling update of application
        docker-compose -f "$COMPOSE_FILE" up -d --no-deps --force-recreate app
        
        # Update supporting services
        docker-compose -f "$COMPOSE_FILE" up -d nginx prometheus grafana loki promtail
        
    else
        log_info "Cold start deployment..."
        docker-compose -f "$COMPOSE_FILE" up -d || {
            log_error "Failed to start production services"
            exit 1
        }
    fi
    
    log_success "Production deployment completed"
}

# Comprehensive health checks
run_health_checks() {
    log_info "Running comprehensive production health checks..."
    
    local max_wait=300  # 5 minutes
    local wait_time=0
    local base_url="https://khoded.com"  # Use production domain
    local failed_tests=0
    
    # Wait for application to be ready
    while [[ $wait_time -lt $max_wait ]]; do
        if curl -sf "$base_url/health" > /dev/null 2>&1; then
            break
        fi
        
        if [[ $wait_time -eq 0 ]]; then
            log_info "Waiting for production application to start..."
        fi
        
        sleep 10
        wait_time=$((wait_time + 10))
        
        if [[ $((wait_time % 60)) -eq 0 ]]; then
            log_info "Still waiting... (${wait_time}s elapsed)"
        fi
    done
    
    if [[ $wait_time -ge $max_wait ]]; then
        log_error "Production application failed to start within ${max_wait} seconds"
        return 1
    fi
    
    log_success "Application is responding"
    
    # Detailed health checks
    log_info "Running detailed health checks..."
    
    # Test health endpoint with detailed response
    local health_response=$(curl -sf "$base_url/health" || echo "FAILED")
    if [[ "$health_response" == "FAILED" ]]; then
        log_error "Health endpoint failed"
        ((failed_tests++))
    elif echo "$health_response" | jq -e '.status == "UP"' > /dev/null 2>&1; then
        log_success "Health check passed"
    else
        log_error "Health check returned unexpected response"
        ((failed_tests++))
    fi
    
    # Test SSL certificate
    if ! curl -sf --max-time 10 "$base_url" > /dev/null; then
        log_error "SSL/HTTPS test failed"
        ((failed_tests++))
    else
        log_success "SSL/HTTPS test passed"
    fi
    
    # Test database connectivity (via health endpoint)
    if ! echo "$health_response" | jq -e '.checks.database.status == "UP"' > /dev/null 2>&1; then
        log_error "Database connectivity test failed"
        ((failed_tests++))
    else
        log_success "Database connectivity test passed"
    fi
    
    # Test Gmail service
    if ! echo "$health_response" | jq -e '.checks.gmail_service_account.status == "UP"' > /dev/null 2>&1; then
        log_error "Gmail service test failed"
        ((failed_tests++))
    else
        log_success "Gmail service test passed"
    fi
    
    # Test metrics endpoint
    if ! curl -sf "$base_url/metrics" > /dev/null; then
        log_error "Metrics endpoint failed"
        ((failed_tests++))
    else
        log_success "Metrics endpoint passed"
    fi
    
    # Test performance (response time)
    local response_time=$(curl -sf -w "%{time_total}" -o /dev/null "$base_url" 2>/dev/null || echo "999")
    if (( $(echo "$response_time > 2.0" | bc -l) )); then
        log_warning "Slow response time: ${response_time}s"
    else
        log_success "Response time acceptable: ${response_time}s"
    fi
    
    if [[ $failed_tests -gt 0 ]]; then
        log_error "$failed_tests production health checks failed"
        return 1
    fi
    
    log_success "All production health checks passed ✅"
}

# Post-deployment tasks
post_deployment_tasks() {
    log_info "Running post-deployment tasks..."
    
    cd "$PROJECT_ROOT"
    
    # Clean up old Docker images (keep last 3 versions)
    log_info "Cleaning up old Docker images..."
    docker images khoded --format "table {{.Repository}}:{{.Tag}}\t{{.CreatedAt}}" | tail -n +2 | sort -k2 -r | tail -n +4 | awk '{print $1}' | xargs -r docker rmi || true
    
    # Prune unused Docker resources
    docker system prune -f || true
    
    # Log deployment details
    local deployment_log="deployments/production-$(date +%Y%m%d_%H%M%S).log"
    mkdir -p deployments
    {
        echo "Production Deployment Log"
        echo "========================="
        echo "Date: $(date)"
        echo "Branch: $PRODUCTION_BRANCH"
        echo "Commit: $(git log -1 --pretty=format:'%h - %s (%an, %ar)')"
        echo "Environment: $ENVIRONMENT"
        echo ""
        echo "Container Status:"
        docker-compose -f "$COMPOSE_FILE" ps
        echo ""
        echo "Resource Usage:"
        docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}" $(docker-compose -f "$COMPOSE_FILE" ps -q)
    } > "$deployment_log"
    
    log_success "Deployment logged to $deployment_log"
    
    log_success "Post-deployment tasks completed"
}

# Show production status
show_production_status() {
    log_info "Production deployment status:"
    echo
    echo "🌐 Application: https://khoded.com"
    echo "🌐 Alt Domain: https://www.khoded.com"
    echo "📊 Grafana: https://khoded.com:3000"
    echo "📈 Prometheus: https://khoded.com:9090"
    echo "🔍 Health Check: https://khoded.com/health"
    echo "📊 Metrics: https://khoded.com/metrics"
    echo
    echo "📅 Deployed: $(date)"
    echo "🌿 Branch: $PRODUCTION_BRANCH"
    echo "📝 Commit: $(git log -1 --pretty=format:'%h - %s')"
    echo
    
    # Show running containers
    log_info "Running production containers:"
    docker-compose -f "$COMPOSE_FILE" ps
    
    echo
    log_info "Production monitoring:"
    echo "  Container logs: docker-compose -f $COMPOSE_FILE logs -f"
    echo "  Service restart: docker-compose -f $COMPOSE_FILE restart [service]"
    echo "  Scale app: docker-compose -f $COMPOSE_FILE up -d --scale app=N"
}

# Rollback function (in case of deployment failure)
rollback() {
    log_error "Production deployment failed. Initiating rollback..."
    
    # Stop current deployment
    docker-compose -f "$COMPOSE_FILE" down
    
    # Restore from backup if available
    local latest_backup=$(ls -t backups/prod-pre-deploy-*.sql.gz 2>/dev/null | head -1)
    if [[ -n "$latest_backup" ]]; then
        log_info "Restoring database from backup: $latest_backup"
        # Start only database for restore
        docker-compose -f "$COMPOSE_FILE" up -d database
        sleep 10
        gunzip -c "$latest_backup" | docker-compose -f "$COMPOSE_FILE" exec -T database psql -U khoded_user khoded_production
    fi
    
    log_error "Rollback completed. Please investigate the deployment failure."
}

# Main deployment function
main() {
    log_info "🚀 Starting Khoded PRODUCTION deployment..."
    echo
    
    # Set trap for rollback on failure
    trap rollback ERR
    
    production_safety_checks
    setup_production_secrets  
    setup_production_env
    create_backup
    build_production_images
    zero_downtime_deploy
    
    # Wait and run health checks
    if run_health_checks; then
        post_deployment_tasks
        show_production_status
        echo
        log_success "🎉 PRODUCTION DEPLOYMENT SUCCESSFUL! 🎉"
        log_info "The application is now live at https://khoded.com"
    else
        log_error "Production health checks failed. Deployment may be unstable."
        rollback
        exit 1
    fi
}

# Handle script interruption
trap 'log_error "Production deployment interrupted"; rollback; exit 1' INT

# Run main function
main "$@"