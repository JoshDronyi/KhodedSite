#!/bin/bash
# Basic Health Check Script for Khoded Production
# Run this every 5 minutes via cron: */5 * * * * /path/to/basic-health-check.sh

SITE_URL="${SITE_URL:-https://khoded.onrender.com}"
LOG_FILE="${LOG_FILE:-/tmp/khoded-health.log}"
SLACK_WEBHOOK="${SLACK_WEBHOOK_URL:-}"  # Optional Slack notifications

# Function to log with timestamp
log_message() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" >> "$LOG_FILE"
}

# Function to send Slack notification (if webhook configured)
send_slack_alert() {
    if [ -n "$SLACK_WEBHOOK" ]; then
        curl -X POST -H 'Content-type: application/json' \
            --data "{\"text\":\"🚨 Khoded Site Alert: $1\"}" \
            "$SLACK_WEBHOOK" 2>/dev/null
    fi
}

# Check main site health
check_health() {
    local response=$(curl -s -o /dev/null -w "%{http_code}" "$SITE_URL/health-api" --max-time 10)
    
    if [ "$response" = "200" ]; then
        log_message "✅ Health check passed (HTTP $response)"
        return 0
    else
        log_message "❌ Health check failed (HTTP $response)"
        send_slack_alert "Health check failed with HTTP $response"
        return 1
    fi
}

# Check site loading time
check_performance() {
    local load_time=$(curl -s -o /dev/null -w "%{time_total}" "$SITE_URL" --max-time 15)
    local threshold=5.0
    
    if (( $(echo "$load_time > $threshold" | bc -l) )); then
        log_message "⚠️ Site loading slowly: ${load_time}s (threshold: ${threshold}s)"
        send_slack_alert "Site loading slowly: ${load_time}s"
    else
        log_message "🚀 Site performance good: ${load_time}s"
    fi
}

# Run checks
log_message "Starting health checks..."
if check_health; then
    check_performance
    log_message "Health checks completed successfully"
else
    log_message "Health checks failed"
    exit 1
fi

# Keep only last 1000 lines in log
tail -n 1000 "$LOG_FILE" > "${LOG_FILE}.tmp" && mv "${LOG_FILE}.tmp" "$LOG_FILE"