#!/bin/bash
# SSL Certificate Setup and Automation Script
# Supports both Let's Encrypt and custom certificates

set -euo pipefail

DOMAIN="khoded.com"
ALT_DOMAIN="www.khoded.com"
EMAIL="admin@khoded.com"
CERT_DIR="./nginx/ssl"
LOG_FILE="./logs/ssl-setup.log"

# Create directories
mkdir -p "$CERT_DIR" "$(dirname "$LOG_FILE")"

log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

# Function to setup Let's Encrypt certificates
setup_letsencrypt() {
    log "Setting up Let's Encrypt certificates for $DOMAIN"
    
    # Install certbot if not present
    if ! command -v certbot &> /dev/null; then
        log "Installing certbot..."
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            sudo apt-get update && sudo apt-get install -y certbot python3-certbot-nginx
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            brew install certbot
        else
            log "Please install certbot manually for your system"
            exit 1
        fi
    fi
    
    # Generate certificates
    log "Generating certificates..."
    sudo certbot certonly \
        --standalone \
        --email "$EMAIL" \
        --agree-tos \
        --no-eff-email \
        --domains "$DOMAIN,$ALT_DOMAIN" \
        --cert-path "$CERT_DIR/khoded.crt" \
        --key-path "$CERT_DIR/khoded.key"
    
    # Copy certificates to nginx directory
    sudo cp "/etc/letsencrypt/live/$DOMAIN/fullchain.pem" "$CERT_DIR/khoded.crt"
    sudo cp "/etc/letsencrypt/live/$DOMAIN/privkey.pem" "$CERT_DIR/khoded.key"
    sudo chown $(whoami):$(whoami) "$CERT_DIR"/*
    sudo chmod 644 "$CERT_DIR/khoded.crt"
    sudo chmod 600 "$CERT_DIR/khoded.key"
    
    log "Let's Encrypt certificates installed successfully"
}

# Function to setup custom certificates
setup_custom_certificates() {
    log "Setting up custom certificates"
    
    if [[ ! -f "./custom-certs/certificate.crt" ]] || [[ ! -f "./custom-certs/private.key" ]]; then
        log "Custom certificate files not found in ./custom-certs/"
        log "Please place your certificate.crt and private.key files in ./custom-certs/"
        exit 1
    fi
    
    cp "./custom-certs/certificate.crt" "$CERT_DIR/khoded.crt"
    cp "./custom-certs/private.key" "$CERT_DIR/khoded.key"
    chmod 644 "$CERT_DIR/khoded.crt"
    chmod 600 "$CERT_DIR/khoded.key"
    
    log "Custom certificates installed successfully"
}

# Function to generate self-signed certificates for development
setup_selfsigned() {
    log "Generating self-signed certificates for development"
    
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
        -keyout "$CERT_DIR/khoded.key" \
        -out "$CERT_DIR/khoded.crt" \
        -subj "/C=US/ST=State/L=City/O=Khoded/CN=$DOMAIN"
    
    chmod 644 "$CERT_DIR/khoded.crt"
    chmod 600 "$CERT_DIR/khoded.key"
    
    log "Self-signed certificates generated successfully"
}

# Function to validate certificates
validate_certificates() {
    log "Validating SSL certificates..."
    
    if [[ ! -f "$CERT_DIR/khoded.crt" ]] || [[ ! -f "$CERT_DIR/khoded.key" ]]; then
        log "ERROR: Certificate files not found"
        exit 1
    fi
    
    # Check certificate validity
    if openssl x509 -in "$CERT_DIR/khoded.crt" -text -noout > /dev/null 2>&1; then
        EXPIRY=$(openssl x509 -in "$CERT_DIR/khoded.crt" -noout -enddate | cut -d= -f2)
        log "Certificate is valid. Expires: $EXPIRY"
    else
        log "ERROR: Certificate validation failed"
        exit 1
    fi
    
    # Check private key
    if openssl rsa -in "$CERT_DIR/khoded.key" -check > /dev/null 2>&1; then
        log "Private key is valid"
    else
        log "ERROR: Private key validation failed"
        exit 1
    fi
    
    # Check if certificate and key match
    CERT_HASH=$(openssl x509 -noout -modulus -in "$CERT_DIR/khoded.crt" | openssl md5)
    KEY_HASH=$(openssl rsa -noout -modulus -in "$CERT_DIR/khoded.key" | openssl md5)
    
    if [[ "$CERT_HASH" == "$KEY_HASH" ]]; then
        log "Certificate and private key match"
    else
        log "ERROR: Certificate and private key do not match"
        exit 1
    fi
}

# Function to setup certificate renewal
setup_renewal() {
    log "Setting up certificate renewal..."
    
    # Create renewal script
    cat > "./scripts/renew-ssl.sh" << 'EOF'
#!/bin/bash
# SSL Certificate Renewal Script

set -euo pipefail

LOG_FILE="./logs/ssl-renewal.log"
CERT_DIR="./nginx/ssl"

log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

log "Starting SSL certificate renewal check..."

# Renew certificates
if sudo certbot renew --quiet; then
    # Copy renewed certificates
    sudo cp "/etc/letsencrypt/live/khoded.com/fullchain.pem" "$CERT_DIR/khoded.crt"
    sudo cp "/etc/letsencrypt/live/khoded.com/privkey.pem" "$CERT_DIR/khoded.key"
    sudo chown $(whoami):$(whoami) "$CERT_DIR"/*
    sudo chmod 644 "$CERT_DIR/khoded.crt"
    sudo chmod 600 "$CERT_DIR/khoded.key"
    
    # Reload nginx
    docker-compose exec nginx nginx -s reload
    
    log "Certificates renewed and nginx reloaded successfully"
else
    log "No certificates needed renewal"
fi
EOF
    
    chmod +x "./scripts/renew-ssl.sh"
    
    # Add to cron (weekly renewal check)
    (crontab -l 2>/dev/null; echo "0 3 * * 0 $(pwd)/scripts/renew-ssl.sh") | crontab -
    
    log "Certificate renewal automation setup complete"
}

# Main execution
case "${1:-letsencrypt}" in
    "letsencrypt")
        setup_letsencrypt
        setup_renewal
        ;;
    "custom")
        setup_custom_certificates
        ;;
    "selfsigned")
        setup_selfsigned
        ;;
    *)
        echo "Usage: $0 [letsencrypt|custom|selfsigned]"
        echo "  letsencrypt - Setup Let's Encrypt certificates (default)"
        echo "  custom      - Use custom certificates from ./custom-certs/"
        echo "  selfsigned  - Generate self-signed certificates for development"
        exit 1
        ;;
esac

validate_certificates
log "SSL setup completed successfully"