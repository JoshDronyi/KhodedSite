#!/bin/bash

# Khoded Development Environment Deployment Script
# Deploys the application to local development environment

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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
    log_info "Checking prerequisites..."
    
    # Check if Docker is installed and running
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        log_error "Docker is not running. Please start Docker."
        exit 1
    fi
    
    # Check if Docker Compose is available
    if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
        log_error "Docker Compose is not available. Please install Docker Compose."
        exit 1
    fi
    
    log_success "Prerequisites check passed"
}

# Setup development environment
setup_development_env() {
    log_info "Setting up development environment..."
    
    cd "$PROJECT_ROOT"
    
    # Copy development environment file
    if [[ ! -f ".env" ]]; then
        cp ".env.development" ".env"
        log_success "Created .env file from .env.development"
    else
        log_warning ".env file already exists. Skipping copy."
    fi
    
    # Create secrets directory if it doesn't exist
    if [[ ! -d "secrets" ]]; then
        mkdir -p secrets
        log_success "Created secrets directory"
    fi
    
    # Check for Gmail service account
    if [[ ! -f "secrets/gmail_service_account_key.json" ]]; then
        log_warning "Gmail service account file not found at secrets/gmail_service_account_key.json"
        log_info "Development will continue without email functionality"
        
        # Create a dummy service account file for development
        cat > secrets/gmail_service_account_key.json << 'EOF'
{
  "type": "service_account",
  "project_id": "dev-project",
  "private_key_id": "dev-key-id",
  "private_key": "-----BEGIN PRIVATE KEY-----\nDEV_PLACEHOLDER_KEY\n-----END PRIVATE KEY-----\n",
  "client_email": "dev@example.com",
  "client_id": "12345",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/dev%40example.com",
  "universe_domain": "googleapis.com"
}
EOF
        log_info "Created dummy Gmail service account for development"
    fi
}

# Build development images
build_dev_images() {
    log_info "Building development Docker images..."
    
    cd "$PROJECT_ROOT"
    
    # Build the application image
    docker build -f Dockerfile.minimal -t khoded:dev . || {
        log_error "Failed to build development image"
        exit 1
    }
    
    log_success "Development images built successfully"
}

# Start development services
start_dev_services() {
    log_info "Starting development services..."
    
    cd "$PROJECT_ROOT"
    
    # Stop any existing services
    docker-compose -f docker-compose.dev.yml down -v --remove-orphans
    
    # Start services
    docker-compose -f docker-compose.dev.yml up -d || {
        log_error "Failed to start development services"
        exit 1
    }
    
    log_success "Development services started"
}

# Wait for services to be healthy
wait_for_services() {
    log_info "Waiting for services to be ready..."
    
    local max_wait=120
    local wait_time=0
    
    while [[ $wait_time -lt $max_wait ]]; do
        if curl -s http://localhost:8080/health > /dev/null 2>&1; then
            log_success "Application is ready!"
            break
        fi
        
        if [[ $wait_time -eq 0 ]]; then
            log_info "Waiting for application to start..."
        fi
        
        sleep 5
        wait_time=$((wait_time + 5))
        
        if [[ $((wait_time % 30)) -eq 0 ]]; then
            log_info "Still waiting... (${wait_time}s elapsed)"
        fi
    done
    
    if [[ $wait_time -ge $max_wait ]]; then
        log_error "Application failed to start within ${max_wait} seconds"
        log_info "Check logs with: docker-compose -f docker-compose.dev.yml logs app"
        exit 1
    fi
}

# Show deployment status
show_status() {
    log_info "Development deployment status:"
    echo
    echo "🌐 Application: http://localhost:8080"
    echo "📊 Grafana: http://localhost:3000 (admin/admin)"
    echo "📈 Prometheus: http://localhost:9090"
    echo "📧 MailCatcher: http://localhost:1080"
    echo "🗄️ Database: localhost:5432"
    echo "🔴 Redis: localhost:6379"
    echo
    echo "📁 Project root: $PROJECT_ROOT"
    echo "🐳 Docker Compose file: docker-compose.dev.yml"
    echo
    
    # Show running containers
    log_info "Running containers:"
    docker-compose -f docker-compose.dev.yml ps
    
    echo
    log_info "Useful commands:"
    echo "  View logs: docker-compose -f docker-compose.dev.yml logs -f"
    echo "  Stop services: docker-compose -f docker-compose.dev.yml down"
    echo "  Restart app: docker-compose -f docker-compose.dev.yml restart app"
    echo "  Shell access: docker-compose -f docker-compose.dev.yml exec app sh"
}

# Main deployment function
main() {
    log_info "Starting Khoded development deployment..."
    echo
    
    check_prerequisites
    setup_development_env
    build_dev_images
    start_dev_services
    wait_for_services
    show_status
    
    echo
    log_success "🚀 Development environment deployed successfully!"
    log_info "The application is now running at http://localhost:8080"
}

# Handle script interruption
trap 'log_error "Deployment interrupted"; exit 1' INT

# Run main function
main "$@"