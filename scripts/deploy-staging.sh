#!/bin/bash

# Khoded Staging Environment Deployment Script
# Deploys the application to staging environment with production-like setup

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
STAGING_BRANCH="staging"
COMPOSE_FILE="docker-compose.staging.yml"
ENVIRONMENT="staging"

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking staging prerequisites..."
    
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
    if [[ "$current_branch" != "$STAGING_BRANCH" ]]; then
        log_error "Must be on '$STAGING_BRANCH' branch for staging deployment. Currently on '$current_branch'"
        log_info "Run: git checkout $STAGING_BRANCH"
        exit 1
    fi
    
    # Check if working directory is clean
    if ! git diff-index --quiet HEAD --; then
        log_error "Working directory is not clean. Please commit or stash changes."
        exit 1
    fi
    
    log_success "Prerequisites check passed"
}

# Setup staging secrets
setup_staging_secrets() {
    log_info "Setting up staging secrets..."
    
    cd "$PROJECT_ROOT"
    
    # Create secrets directory
    mkdir -p secrets
    
    # Required staging secrets
    local required_secrets=(
        "staging_db_password.txt"
        "staging_postgres_password.txt" 
        "staging_google_service_account.json"
        "grafana_password.txt"
    )
    
    local missing_secrets=()
    
    # Check for required secrets
    for secret in "${required_secrets[@]}"; do
        if [[ ! -f "secrets/$secret" ]]; then
            missing_secrets+=("$secret")
        fi
    done
    
    # Generate missing secrets
    if [[ ${#missing_secrets[@]} -gt 0 ]]; then
        log_warning "Missing staging secrets. Generating..."
        
        for secret in "${missing_secrets[@]}"; do
            case "$secret" in
                *_password.txt)
                    # Generate secure password
                    openssl rand -base64 32 | tr -d "=+/" | cut -c1-32 > "secrets/$secret"
                    chmod 600 "secrets/$secret"
                    log_success "Generated $secret"
                    ;;
                staging_google_service_account.json)
                    log_error "Missing Google service account file: secrets/$secret"
                    log_info "Please download the staging service account JSON from Google Cloud Console"
                    log_info "and save it as secrets/$secret"
                    exit 1
                    ;;
            esac
        done
    fi
    
    # Set proper permissions
    chmod -R 600 secrets/
    chmod 700 secrets/
    
    log_success "Staging secrets configured"
}

# Setup staging environment
setup_staging_env() {
    log_info "Setting up staging environment..."
    
    cd "$PROJECT_ROOT"
    
    # Copy staging environment file
    if [[ ! -f ".env" ]] || [[ ".env.staging" -nt ".env" ]]; then
        cp ".env.staging" ".env"
        log_success "Updated .env file from .env.staging"
    fi
    
    # Validate required environment variables
    local required_vars=(
        "APP_ENVIRONMENT"
        "PRIMARY_DOMAIN"
        "DATABASE_URL"
        "GOOGLE_SERVICE_ACCOUNT_FILE"
    )
    
    source .env
    
    for var in "${required_vars[@]}"; do
        if [[ -z "${!var:-}" ]]; then
            log_error "Required environment variable $var is not set"
            exit 1
        fi
    done
    
    log_success "Staging environment validated"
}

# Build staging images
build_staging_images() {
    log_info "Building staging Docker images..."
    
    cd "$PROJECT_ROOT"
    
    # Pull latest changes
    git pull origin "$STAGING_BRANCH"
    
    # Build the application image with production Dockerfile
    docker build -f Dockerfile.production -t khoded:staging . || {
        log_error "Failed to build staging image"
        exit 1
    }
    
    log_success "Staging images built successfully"
}

# Deploy to staging
deploy_staging() {
    log_info "Deploying to staging environment..."
    
    cd "$PROJECT_ROOT"
    
    # Create backup of current deployment if it exists
    if docker-compose -f "$COMPOSE_FILE" ps | grep -q "Up"; then
        log_info "Creating backup of current staging deployment..."
        docker-compose -f "$COMPOSE_FILE" exec database pg_dump -U khoded_staging_user khoded_staging | gzip > "backups/staging-pre-deploy-$(date +%Y%m%d_%H%M%S).sql.gz"
    fi
    
    # Stop existing services
    docker-compose -f "$COMPOSE_FILE" down --remove-orphans
    
    # Pull latest images for infrastructure services
    docker-compose -f "$COMPOSE_FILE" pull nginx prometheus grafana loki promtail
    
    # Start services with rolling deployment
    log_info "Starting staging services..."
    docker-compose -f "$COMPOSE_FILE" up -d --force-recreate || {
        log_error "Failed to start staging services"
        log_info "Rolling back..."
        docker-compose -f "$COMPOSE_FILE" down
        exit 1
    }
    
    log_success "Staging deployment completed"
}

# Wait for services to be healthy
wait_for_services() {
    log_info "Waiting for staging services to be ready..."
    
    local max_wait=300  # 5 minutes for staging
    local wait_time=0
    local health_endpoint="http://localhost:8080/health"
    
    while [[ $wait_time -lt $max_wait ]]; do
        if curl -s "$health_endpoint" > /dev/null 2>&1; then
            log_success "Staging application is ready!"
            break
        fi
        
        if [[ $wait_time -eq 0 ]]; then
            log_info "Waiting for staging application to start..."
        fi
        
        sleep 10
        wait_time=$((wait_time + 10))
        
        if [[ $((wait_time % 60)) -eq 0 ]]; then
            log_info "Still waiting... (${wait_time}s elapsed)"
            # Show container status
            docker-compose -f "$COMPOSE_FILE" ps
        fi
    done
    
    if [[ $wait_time -ge $max_wait ]]; then
        log_error "Staging application failed to start within ${max_wait} seconds"
        log_info "Check logs with: docker-compose -f $COMPOSE_FILE logs app"
        show_logs
        exit 1
    fi
}

# Run staging tests
run_staging_tests() {
    log_info "Running staging health checks..."
    
    local base_url="http://localhost:8080"
    local failed_tests=0
    
    # Test health endpoint
    if ! curl -sf "$base_url/health" > /dev/null; then
        log_error "Health check failed"
        ((failed_tests++))
    else
        log_success "Health check passed"
    fi
    
    # Test metrics endpoint
    if ! curl -sf "$base_url/metrics" > /dev/null; then
        log_error "Metrics endpoint failed"
        ((failed_tests++))
    else
        log_success "Metrics endpoint passed"
    fi
    
    # Test database connectivity (via health endpoint)
    if ! curl -s "$base_url/health" | grep -q '"database".*"UP"'; then
        log_error "Database connectivity test failed"
        ((failed_tests++))
    else
        log_success "Database connectivity test passed"
    fi
    
    if [[ $failed_tests -gt 0 ]]; then
        log_error "$failed_tests staging tests failed"
        return 1
    fi
    
    log_success "All staging tests passed"
}

# Show deployment status
show_status() {
    log_info "Staging deployment status:"
    echo
    echo "🌐 Application: http://localhost:8080"
    echo "📊 Grafana: http://localhost:3000"
    echo "📈 Prometheus: http://localhost:9090" 
    echo "📝 Loki: http://localhost:3100"
    echo "🔍 Health Check: http://localhost:8080/health"
    echo "📊 Metrics: http://localhost:8080/metrics"
    echo
    
    # Show running containers
    log_info "Running containers:"
    docker-compose -f "$COMPOSE_FILE" ps
    
    # Show resource usage
    echo
    log_info "Resource usage:"
    docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}" $(docker-compose -f "$COMPOSE_FILE" ps -q)
}

# Show logs if deployment fails
show_logs() {
    log_info "Recent application logs:"
    docker-compose -f "$COMPOSE_FILE" logs --tail=50 app
    
    echo
    log_info "Recent database logs:"
    docker-compose -f "$COMPOSE_FILE" logs --tail=20 database
}

# Cleanup on failure
cleanup() {
    log_error "Staging deployment failed. Cleaning up..."
    docker-compose -f "$COMPOSE_FILE" down --remove-orphans
}

# Main deployment function
main() {
    log_info "Starting Khoded staging deployment..."
    echo
    
    # Set trap for cleanup on failure
    trap cleanup ERR
    
    check_prerequisites
    setup_staging_secrets
    setup_staging_env
    build_staging_images
    deploy_staging
    wait_for_services
    
    if run_staging_tests; then
        show_status
        echo
        log_success "🚀 Staging environment deployed successfully!"
        log_info "Branch: $STAGING_BRANCH"
        log_info "Commit: $(git rev-parse --short HEAD)"
        log_info "Deployed at: $(date)"
    else
        log_error "Staging tests failed. Deployment may be unstable."
        show_logs
        exit 1
    fi
}

# Handle script interruption
trap 'log_error "Staging deployment interrupted"; cleanup; exit 1' INT

# Run main function
main "$@"