#!/bin/bash
# Simple cross-platform shell script for environment management
# Works on Linux/macOS (container environments)

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Get script directory (works on both Linux and macOS)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
ENV_FILES_DIR="$PROJECT_ROOT/env-files"

log_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
log_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
log_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

usage() {
    echo "Usage: $0 [command] [environment]"
    echo ""
    echo "Commands:"
    echo "  setup [env]     - Setup environment"
    echo "  validate [env]  - Validate environment"
    echo "  status          - Show status"
    echo "  clean           - Clean temp files"
    echo ""
    echo "Environments: development, staging, production"
}

setup_environment() {
    local env=${1:-development}
    log_info "Setting up $env environment..."
    
    # Check template exists
    if [ ! -f "$ENV_FILES_DIR/.env.$env" ]; then
        log_error "Template not found: $ENV_FILES_DIR/.env.$env"
        return 1
    fi
    
    # Copy template
    cp "$ENV_FILES_DIR/.env.$env" "$PROJECT_ROOT/.env"
    log_success "Copied $env configuration to .env"
    
    # Create secrets directory
    mkdir -p "$PROJECT_ROOT/secrets"
    
    # Create dummy secrets for development
    if [ "$env" = "development" ]; then
        cat > "$PROJECT_ROOT/secrets/gmail_service_account_key.json" << EOF
{
  "type": "service_account",
  "project_id": "development-project",
  "client_email": "dev@development.iam.gserviceaccount.com",
  "private_key": "-----BEGIN PRIVATE KEY-----\nDUMMY_KEY_FOR_DEVELOPMENT\n-----END PRIVATE KEY-----"
}
EOF
        log_success "Created development secrets"
    else
        log_warning "For $env environment, configure real secrets manually"
    fi
    
    log_success "Environment setup completed"
    return 0
}

validate_environment() {
    local env=${1:-$(get_current_environment)}
    log_info "Validating $env environment..."
    
    local error_count=0
    
    # Check .env file
    if [ ! -f "$PROJECT_ROOT/.env" ]; then
        log_error ".env file not found"
        ((error_count++))
    else
        log_success ".env file exists"
    fi
    
    # Check secrets directory
    if [ ! -d "$PROJECT_ROOT/secrets" ]; then
        log_error "secrets/ directory not found"
        ((error_count++))
    else
        log_success "secrets/ directory exists"
        
        # Check Gmail service account file
        if [ -f "$PROJECT_ROOT/secrets/gmail_service_account_key.json" ]; then
            log_success "Gmail service account file exists"
        else
            if [ "$env" = "development" ]; then
                log_warning "Gmail service account file missing (acceptable for development)"
            else
                log_error "Gmail service account file missing"
                ((error_count++))
            fi
        fi
    fi
    
    # Check Gradle wrapper
    if [ -f "$PROJECT_ROOT/gradlew" ]; then
        log_success "Gradle wrapper exists"
    else
        log_error "Gradle wrapper not found"
        ((error_count++))
    fi
    
    if [ $error_count -eq 0 ]; then
        log_success "Validation passed ✅"
        return 0
    else
        log_error "Validation failed with $error_count errors ❌"
        return 1
    fi
}

get_current_environment() {
    if [ -f "$PROJECT_ROOT/.env" ]; then
        grep "^APP_ENVIRONMENT=" "$PROJECT_ROOT/.env" 2>/dev/null | cut -d'=' -f2 | tr -d '"' || echo "unknown"
    else
        echo "none"
    fi
}

show_status() {
    local current_env=$(get_current_environment)
    
    echo -e "${BLUE}📊 Environment Status${NC}"
    echo "===================="
    echo "Current Environment: $current_env"
    echo "Project Root: $PROJECT_ROOT"
    echo ""
    
    # Show .env file status
    if [ -f "$PROJECT_ROOT/.env" ]; then
        echo -e "${GREEN}✅${NC} .env file exists"
    else
        echo -e "${RED}❌${NC} .env file missing"
    fi
    
    # Show secrets status
    if [ -d "$PROJECT_ROOT/secrets" ]; then
        echo -e "${GREEN}✅${NC} secrets/ directory exists"
    else
        echo -e "${RED}❌${NC} secrets/ directory missing"
    fi
    
    # Show available environments
    echo ""
    echo "Available Environments:"
    for env in development staging production; do
        if [ -f "$ENV_FILES_DIR/.env.$env" ]; then
            if [ "$env" = "$current_env" ]; then
                echo -e "  ${GREEN}● $env${NC} (current)"
            else
                echo "  ○ $env"
            fi
        else
            echo -e "  ${RED}✗ $env${NC} (template missing)"
        fi
    done
}

clean_temp_files() {
    log_info "Cleaning temporary files..."
    
    local cleaned=0
    
    # Clean build artifacts
    if [ -d "$PROJECT_ROOT/site/build" ]; then
        rm -rf "$PROJECT_ROOT/site/build"
        log_success "Removed build artifacts"
        ((cleaned++))
    fi
    
    # Clean Gradle cache
    if [ -d "$PROJECT_ROOT/.gradle" ]; then
        rm -rf "$PROJECT_ROOT/.gradle"
        log_success "Removed local Gradle cache"
        ((cleaned++))
    fi
    
    # Clean Kobweb cache
    if [ -d "$PROJECT_ROOT/site/.kobweb" ]; then
        rm -rf "$PROJECT_ROOT/site/.kobweb"
        log_success "Removed Kobweb cache"
        ((cleaned++))
    fi
    
    # Clean Kotlin JS store
    if [ -d "$PROJECT_ROOT/site/kotlin-js-store" ]; then
        rm -rf "$PROJECT_ROOT/site/kotlin-js-store"
        log_success "Removed Kotlin JS store"
        ((cleaned++))
    fi
    
    if [ $cleaned -eq 0 ]; then
        log_info "No temporary files to clean"
    else
        log_success "Cleaned $cleaned directories"
    fi
}

# Main execution
case "${1:-}" in
    setup)
        setup_environment "$2"
        ;;
    validate)
        validate_environment "$2"
        ;;
    status)
        show_status
        ;;
    clean)
        clean_temp_files
        ;;
    *)
        usage
        exit 1
        ;;
esac