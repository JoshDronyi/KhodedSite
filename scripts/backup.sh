#!/bin/bash
# PostgreSQL Backup and Recovery Script
# Automated database backup with compression and retention

set -euo pipefail

# Configuration
DB_NAME="khodedBackendData"
DB_USER="khodedAdmin"
DB_HOST="database"
DB_PORT="5432"
BACKUP_DIR="/backups"
RETENTION_DAYS=30
COMPRESSION_LEVEL=9
LOG_FILE="$BACKUP_DIR/backup.log"

# Ensure backup directory exists
mkdir -p "$BACKUP_DIR"

log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

# Function to create backup
create_backup() {
    local timestamp=$(date +"%Y%m%d_%H%M%S")
    local backup_file="$BACKUP_DIR/${DB_NAME}_${timestamp}.sql"
    local compressed_file="${backup_file}.gz"
    
    log "Starting database backup for $DB_NAME"
    
    # Create backup with verbose output
    PGPASSWORD="$(cat /run/secrets/postgres_password)" pg_dump \
        -h "$DB_HOST" \
        -p "$DB_PORT" \
        -U "$DB_USER" \
        -d "$DB_NAME" \
        --verbose \
        --no-password \
        --format=custom \
        --compress="$COMPRESSION_LEVEL" \
        --file="$backup_file" \
        2>&1 | tee -a "$LOG_FILE"
    
    if [[ $? -eq 0 ]]; then
        # Compress backup
        gzip -"$COMPRESSION_LEVEL" "$backup_file"
        
        # Get file size
        local size=$(du -h "$compressed_file" | cut -f1)
        log "Backup completed successfully: $compressed_file (Size: $size)"
        
        # Create checksum
        sha256sum "$compressed_file" > "${compressed_file}.sha256"
        log "Checksum created: ${compressed_file}.sha256"
        
        # Update latest symlink
        ln -sf "$(basename "$compressed_file")" "$BACKUP_DIR/latest.sql.gz"
        
        return 0
    else
        log "ERROR: Backup failed for $DB_NAME"
        return 1
    fi
}

# Function to clean old backups
cleanup_old_backups() {
    log "Cleaning up backups older than $RETENTION_DAYS days"
    
    find "$BACKUP_DIR" -name "*.sql.gz" -type f -mtime +$RETENTION_DAYS -exec rm -f {} \;
    find "$BACKUP_DIR" -name "*.sha256" -type f -mtime +$RETENTION_DAYS -exec rm -f {} \;
    
    local remaining=$(find "$BACKUP_DIR" -name "*.sql.gz" -type f | wc -l)
    log "Cleanup completed. $remaining backup files remaining."
}

# Function to verify backup integrity
verify_backup() {
    local backup_file="$1"
    
    if [[ ! -f "$backup_file" ]]; then
        log "ERROR: Backup file not found: $backup_file"
        return 1
    fi
    
    log "Verifying backup integrity: $backup_file"
    
    # Check if file is a valid gzip
    if gzip -t "$backup_file" 2>/dev/null; then
        log "Backup file compression is valid"
    else
        log "ERROR: Backup file compression is corrupted"
        return 1
    fi
    
    # Verify checksum if exists
    local checksum_file="${backup_file}.sha256"
    if [[ -f "$checksum_file" ]]; then
        if sha256sum -c "$checksum_file" 2>/dev/null; then
            log "Backup checksum verification passed"
        else
            log "ERROR: Backup checksum verification failed"
            return 1
        fi
    fi
    
    return 0
}

# Function to restore database from backup
restore_database() {
    local backup_file="$1"
    local target_db="${2:-${DB_NAME}_restore_$(date +%Y%m%d_%H%M%S)}"
    
    if [[ ! -f "$backup_file" ]]; then
        log "ERROR: Backup file not found: $backup_file"
        return 1
    fi
    
    log "Starting database restoration from $backup_file to $target_db"
    
    # Verify backup first
    if ! verify_backup "$backup_file"; then
        log "ERROR: Backup verification failed. Aborting restoration."
        return 1
    fi
    
    # Create target database
    PGPASSWORD="$(cat /run/secrets/postgres_password)" createdb \
        -h "$DB_HOST" \
        -p "$DB_PORT" \
        -U "$DB_USER" \
        "$target_db" 2>&1 | tee -a "$LOG_FILE"
    
    # Restore from backup
    zcat "$backup_file" | PGPASSWORD="$(cat /run/secrets/postgres_password)" pg_restore \
        -h "$DB_HOST" \
        -p "$DB_PORT" \
        -U "$DB_USER" \
        -d "$target_db" \
        --verbose \
        --no-password \
        2>&1 | tee -a "$LOG_FILE"
    
    if [[ $? -eq 0 ]]; then
        log "Database restoration completed successfully to $target_db"
        return 0
    else
        log "ERROR: Database restoration failed"
        return 1
    fi
}

# Function to list available backups
list_backups() {
    log "Available backups in $BACKUP_DIR:"
    
    find "$BACKUP_DIR" -name "*.sql.gz" -type f -printf "%T@ %Tc %s %p\n" | \
    sort -nr | \
    awk '{
        size = $3
        if (size > 1024*1024*1024) {
            size_str = sprintf("%.2f GB", size/(1024*1024*1024))
        } else if (size > 1024*1024) {
            size_str = sprintf("%.2f MB", size/(1024*1024))
        } else if (size > 1024) {
            size_str = sprintf("%.2f KB", size/1024)
        } else {
            size_str = sprintf("%d B", size)
        }
        printf "%-20s %-10s %s\n", $4, size_str, substr($0, index($0, $4))
    }' | \
    head -20
}

# Function to get backup statistics
backup_stats() {
    local total_size=$(find "$BACKUP_DIR" -name "*.sql.gz" -type f -exec du -b {} \; | awk '{total += $1} END {print total}')
    local backup_count=$(find "$BACKUP_DIR" -name "*.sql.gz" -type f | wc -l)
    local oldest_backup=$(find "$BACKUP_DIR" -name "*.sql.gz" -type f -printf "%T@\n" | sort -n | head -1)
    local newest_backup=$(find "$BACKUP_DIR" -name "*.sql.gz" -type f -printf "%T@\n" | sort -nr | head -1)
    
    if [[ -n "$total_size" && "$total_size" -gt 0 ]]; then
        if [[ "$total_size" -gt $((1024*1024*1024)) ]]; then
            total_size_str=$(echo "scale=2; $total_size/(1024*1024*1024)" | bc -l)" GB"
        elif [[ "$total_size" -gt $((1024*1024)) ]]; then
            total_size_str=$(echo "scale=2; $total_size/(1024*1024)" | bc -l)" MB"
        else
            total_size_str=$(echo "scale=2; $total_size/1024" | bc -l)" KB"
        fi
    else
        total_size_str="0 B"
    fi
    
    log "Backup Statistics:"
    log "  Total backups: $backup_count"
    log "  Total size: $total_size_str"
    
    if [[ -n "$oldest_backup" ]]; then
        log "  Oldest backup: $(date -d "@$oldest_backup" '+%Y-%m-%d %H:%M:%S')"
    fi
    
    if [[ -n "$newest_backup" ]]; then
        log "  Newest backup: $(date -d "@$newest_backup" '+%Y-%m-%d %H:%M:%S')"
    fi
}

# Function to run health check
health_check() {
    log "Running database health check"
    
    # Check database connectivity
    if PGPASSWORD="$(cat /run/secrets/postgres_password)" pg_isready \
        -h "$DB_HOST" \
        -p "$DB_PORT" \
        -U "$DB_USER" \
        -d "$DB_NAME" > /dev/null 2>&1; then
        log "Database connectivity: OK"
    else
        log "ERROR: Database connectivity failed"
        return 1
    fi
    
    # Check backup directory permissions
    if [[ -w "$BACKUP_DIR" ]]; then
        log "Backup directory permissions: OK"
    else
        log "ERROR: Backup directory not writable"
        return 1
    fi
    
    # Check available disk space (warn if less than 1GB)
    local available_space=$(df "$BACKUP_DIR" | awk 'NR==2 {print $4}')
    local available_gb=$((available_space / 1024 / 1024))
    
    if [[ "$available_gb" -gt 1 ]]; then
        log "Available disk space: ${available_gb} GB (OK)"
    else
        log "WARNING: Low disk space: ${available_gb} GB"
    fi
    
    log "Health check completed"
}

# Main execution
case "${1:-backup}" in
    "backup"|"create")
        health_check
        create_backup
        cleanup_old_backups
        backup_stats
        ;;
    "restore")
        if [[ $# -lt 2 ]]; then
            echo "Usage: $0 restore <backup_file> [target_database]"
            exit 1
        fi
        restore_database "$2" "${3:-}"
        ;;
    "verify")
        if [[ $# -lt 2 ]]; then
            echo "Usage: $0 verify <backup_file>"
            exit 1
        fi
        verify_backup "$2"
        ;;
    "list")
        list_backups
        ;;
    "stats")
        backup_stats
        ;;
    "cleanup")
        cleanup_old_backups
        ;;
    "health")
        health_check
        ;;
    *)
        echo "Usage: $0 [backup|restore|verify|list|stats|cleanup|health]"
        echo ""
        echo "Commands:"
        echo "  backup           - Create new backup (default)"
        echo "  restore <file>   - Restore from backup file"
        echo "  verify <file>    - Verify backup integrity"
        echo "  list             - List available backups"
        echo "  stats            - Show backup statistics"
        echo "  cleanup          - Remove old backups"
        echo "  health           - Run health check"
        exit 1
        ;;
esac