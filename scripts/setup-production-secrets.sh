#!/bin/bash

# Khoded Production Secrets Setup Script
# This script helps configure production secrets securely

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SECRETS_DIR="$PROJECT_ROOT/secrets"

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

# Check if running as root (for Docker deployment)
check_permissions() {
    if [[ $EUID -eq 0 ]]; then
        log_warning "Running as root. Ensure proper file permissions are set."
    fi
}

# Create secrets directory with proper permissions
setup_secrets_directory() {
    log_info "Setting up secrets directory..."
    
    if [[ ! -d "$SECRETS_DIR" ]]; then
        mkdir -p "$SECRETS_DIR"
        log_success "Created secrets directory: $SECRETS_DIR"
    else
        log_info "Secrets directory already exists"
    fi
    
    # Set restrictive permissions
    chmod 700 "$SECRETS_DIR"
    log_success "Set directory permissions to 700"
}

# Generate secure database password
generate_db_password() {
    local password_file="$SECRETS_DIR/db_password.txt"
    
    if [[ -f "$password_file" ]]; then
        log_warning "Database password already exists. Skipping generation."
        return
    fi
    
    log_info "Generating secure database password..."
    
    # Generate 32-character password with alphanumeric + special chars
    local password=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
    echo "$password" > "$password_file"
    chmod 600 "$password_file"
    
    log_success "Generated database password"
}

# Generate secure PostgreSQL password
generate_postgres_password() {
    local password_file="$SECRETS_DIR/postgres_password.txt"
    
    if [[ -f "$password_file" ]]; then
        log_warning "PostgreSQL password already exists. Skipping generation."
        return
    fi
    
    log_info "Generating secure PostgreSQL password..."
    
    local password=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-32)
    echo "$password" > "$password_file"
    chmod 600 "$password_file"
    
    log_success "Generated PostgreSQL password"
}

# Generate Grafana admin password
generate_grafana_password() {
    local password_file="$SECRETS_DIR/grafana_password.txt"
    
    if [[ -f "$password_file" ]]; then
        log_warning "Grafana password already exists. Skipping generation."
        return
    fi
    
    log_info "Generating secure Grafana admin password..."
    
    local password=$(openssl rand -base64 24 | tr -d "=+/" | cut -c1-24)
    echo "$password" > "$password_file"
    chmod 600 "$password_file"
    
    log_success "Generated Grafana admin password"
}

# Validate Gmail service account
validate_gmail_service_account() {
    local gmail_file="$SECRETS_DIR/gmail_service_account_key.json"
    
    log_info "Validating Gmail service account..."
    
    if [[ ! -f "$gmail_file" ]]; then
        log_error "Gmail service account file not found: $gmail_file"
        log_info "Please download your Google Cloud service account JSON file and save it as:"
        log_info "  $gmail_file"
        return 1
    fi
    
    # Validate JSON structure
    if ! jq -e '.type == "service_account"' "$gmail_file" > /dev/null 2>&1; then
        log_error "Invalid Gmail service account file format"
        return 1
    fi
    
    # Check required fields
    local required_fields=("project_id" "private_key" "client_email")
    for field in "${required_fields[@]}"; do
        if ! jq -e ".$field" "$gmail_file" > /dev/null 2>&1; then
            log_error "Missing required field in service account: $field"
            return 1
        fi
    done
    
    chmod 600 "$gmail_file"
    log_success "Gmail service account validation passed"
}

# Create production environment file
create_production_env() {
    local env_file="$PROJECT_ROOT/.env"
    local template_file="$PROJECT_ROOT/.env.production"
    
    if [[ -f "$env_file" ]]; then
        log_warning "Production .env file already exists. Creating backup..."
        cp "$env_file" "$env_file.backup.$(date +%Y%m%d_%H%M%S)"
    fi
    
    log_info "Creating production environment file..."
    cp "$template_file" "$env_file"
    
    # Replace placeholders with actual values
    sed -i 's|ERROR_TRACKING_DSN=https://your-sentry-dsn@sentry.io/project-id|ERROR_TRACKING_DSN=|g' "$env_file"
    sed -i 's|GA_TRACKING_ID=G-XXXXXXXXXX|GA_TRACKING_ID=|g' "$env_file"
    
    chmod 600 "$env_file"
    log_success "Created production environment file"
}

# Validate all secrets exist
validate_secrets() {
    log_info "Validating all required secrets..."
    
    local required_secrets=(
        "db_password.txt"
        "postgres_password.txt"
        "grafana_password.txt"
        "gmail_service_account_key.json"
    )
    
    local missing_secrets=()
    
    for secret in "${required_secrets[@]}"; do
        if [[ ! -f "$SECRETS_DIR/$secret" ]]; then
            missing_secrets+=("$secret")
        fi
    done
    
    if [[ ${#missing_secrets[@]} -eq 0 ]]; then
        log_success "All required secrets are present"
        return 0
    else
        log_error "Missing secrets:"
        for secret in "${missing_secrets[@]}"; do
            log_error "  - $secret"
        done
        return 1
    fi
}

# Display setup summary
display_summary() {
    log_info "Production secrets setup summary:"
    echo
    echo "Secrets directory: $SECRETS_DIR"
    echo "Files created:"
    
    if [[ -f "$SECRETS_DIR/db_password.txt" ]]; then
        echo "  ✓ Database password"
    fi
    
    if [[ -f "$SECRETS_DIR/postgres_password.txt" ]]; then
        echo "  ✓ PostgreSQL password"
    fi
    
    if [[ -f "$SECRETS_DIR/grafana_password.txt" ]]; then
        echo "  ✓ Grafana admin password"
    fi
    
    if [[ -f "$SECRETS_DIR/gmail_service_account_key.json" ]]; then
        echo "  ✓ Gmail service account"
    fi
    
    echo
    log_info "Next steps:"
    echo "1. Review and update .env file with your specific configuration"
    echo "2. Configure SSL certificates"
    echo "3. Deploy using: docker-compose -f docker-compose.prod.yml up -d"
    echo
    log_warning "Keep all files in the secrets/ directory secure and never commit them to version control!"
}

# Main setup function
main() {
    log_info "Khoded Production Secrets Setup"
    log_info "==============================="
    
    check_permissions
    setup_secrets_directory
    generate_db_password
    generate_postgres_password
    generate_grafana_password
    
    # Gmail service account validation (optional - may already exist)
    if ! validate_gmail_service_account; then
        log_warning "Gmail service account validation failed. Please set it up manually."
    fi
    
    create_production_env
    
    if validate_secrets; then
        log_success "Production secrets setup completed successfully!"
        display_summary
    else
        log_error "Production secrets setup incomplete. Please review the errors above."
        exit 1
    fi
}

# Check for required tools
command -v openssl >/dev/null 2>&1 || { log_error "openssl is required but not installed. Aborting."; exit 1; }
command -v jq >/dev/null 2>&1 || { log_warning "jq is recommended for JSON validation but not installed."; }

# Run main function
main "$@"