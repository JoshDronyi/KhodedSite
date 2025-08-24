-- Khoded Production Database Initialization
-- Creates tables for consultation requests and application monitoring

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Consultation requests table
CREATE TABLE IF NOT EXISTS consultation_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    -- Personal Information
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    company VARCHAR(255),
    title VARCHAR(255),
    
    -- Project Information
    project_type VARCHAR(50) NOT NULL,
    budget VARCHAR(50) NOT NULL,
    timeline VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    goals TEXT[],
    platforms TEXT[],
    
    -- Business Information
    industry VARCHAR(100),
    company_size VARCHAR(50),
    current_website VARCHAR(500),
    target_audience TEXT,
    competitors TEXT[],
    
    -- Additional Information
    has_existing_brand BOOLEAN DEFAULT FALSE,
    needs_hosting BOOLEAN DEFAULT FALSE,
    needs_maintenance BOOLEAN DEFAULT FALSE,
    additional_services TEXT[],
    comments TEXT,
    
    -- Status and tracking
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'in_review', 'contacted', 'closed')),
    assigned_to VARCHAR(100),
    notes TEXT,
    
    -- Metadata
    ip_address INET,
    user_agent TEXT,
    referrer TEXT,
    utm_source VARCHAR(100),
    utm_medium VARCHAR(100),
    utm_campaign VARCHAR(100)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_consultation_requests_created_at ON consultation_requests(created_at);
CREATE INDEX IF NOT EXISTS idx_consultation_requests_status ON consultation_requests(status);
CREATE INDEX IF NOT EXISTS idx_consultation_requests_email ON consultation_requests(email);
CREATE INDEX IF NOT EXISTS idx_consultation_requests_project_type ON consultation_requests(project_type);

-- Email templates table
CREATE TABLE IF NOT EXISTS email_templates (
    id SERIAL PRIMARY KEY,
    template_name VARCHAR(100) UNIQUE NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body_html TEXT NOT NULL,
    body_text TEXT NOT NULL,
    variables JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Insert default email templates
INSERT INTO email_templates (template_name, subject, body_html, body_text, variables) 
VALUES 
(
    'consultation_confirmation',
    'Thank you for your consultation request - Khoded',
    '<h1>Thank you for reaching out!</h1><p>Dear {{first_name}},</p><p>We have received your consultation request and will get back to you within 24 hours.</p><p>Project Type: {{project_type}}</p><p>Timeline: {{timeline}}</p><p>Best regards,<br>The Khoded Team</p>',
    'Dear {{first_name}},\n\nWe have received your consultation request and will get back to you within 24 hours.\n\nProject Type: {{project_type}}\nTimeline: {{timeline}}\n\nBest regards,\nThe Khoded Team',
    '["first_name", "project_type", "timeline"]'
),
(
    'consultation_followup',
    'Following up on your consultation request - Khoded',
    '<h1>Following up on your project</h1><p>Dear {{first_name}},</p><p>Thank you for your interest in working with Khoded. I wanted to follow up on your {{project_type}} project.</p><p>When would be a good time for a brief call to discuss your requirements in more detail?</p><p>Best regards,<br>{{assigned_to}}</p>',
    'Dear {{first_name}},\n\nThank you for your interest in working with Khoded. I wanted to follow up on your {{project_type}} project.\n\nWhen would be a good time for a brief call to discuss your requirements in more detail?\n\nBest regards,\n{{assigned_to}}',
    '["first_name", "project_type", "assigned_to"]'
) ON CONFLICT (template_name) DO NOTHING;

-- Application logs table for structured logging
CREATE TABLE IF NOT EXISTS application_logs (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    level VARCHAR(10) NOT NULL CHECK (level IN ('DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL')),
    logger_name VARCHAR(255),
    message TEXT NOT NULL,
    exception TEXT,
    properties JSONB,
    trace_id VARCHAR(64),
    span_id VARCHAR(16),
    user_id VARCHAR(100),
    session_id VARCHAR(100),
    ip_address INET,
    user_agent TEXT
);

-- Create indexes for log queries
CREATE INDEX IF NOT EXISTS idx_application_logs_timestamp ON application_logs(timestamp);
CREATE INDEX IF NOT EXISTS idx_application_logs_level ON application_logs(level);
CREATE INDEX IF NOT EXISTS idx_application_logs_logger_name ON application_logs(logger_name);
CREATE INDEX IF NOT EXISTS idx_application_logs_trace_id ON application_logs(trace_id);

-- User sessions table
CREATE TABLE IF NOT EXISTS user_sessions (
    session_id VARCHAR(128) PRIMARY KEY,
    user_id VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_accessed TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ip_address INET,
    user_agent TEXT,
    data JSONB
);

-- Create index for session cleanup
CREATE INDEX IF NOT EXISTS idx_user_sessions_expires_at ON user_sessions(expires_at);

-- Performance metrics table
CREATE TABLE IF NOT EXISTS performance_metrics (
    id BIGSERIAL PRIMARY KEY,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    endpoint VARCHAR(255) NOT NULL,
    method VARCHAR(10) NOT NULL,
    status_code INTEGER NOT NULL,
    response_time_ms INTEGER NOT NULL,
    memory_usage_mb REAL,
    cpu_usage_percent REAL,
    database_query_count INTEGER,
    database_query_time_ms INTEGER,
    cache_hits INTEGER DEFAULT 0,
    cache_misses INTEGER DEFAULT 0,
    user_id VARCHAR(100),
    session_id VARCHAR(100),
    ip_address INET
);

-- Create indexes for performance analysis
CREATE INDEX IF NOT EXISTS idx_performance_metrics_timestamp ON performance_metrics(timestamp);
CREATE INDEX IF NOT EXISTS idx_performance_metrics_endpoint ON performance_metrics(endpoint);
CREATE INDEX IF NOT EXISTS idx_performance_metrics_status_code ON performance_metrics(status_code);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
DROP TRIGGER IF EXISTS update_consultation_requests_updated_at ON consultation_requests;
CREATE TRIGGER update_consultation_requests_updated_at 
    BEFORE UPDATE ON consultation_requests 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_email_templates_updated_at ON email_templates;
CREATE TRIGGER update_email_templates_updated_at 
    BEFORE UPDATE ON email_templates 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Clean up old logs (keep last 30 days)
CREATE OR REPLACE FUNCTION cleanup_old_logs()
RETURNS void AS $$
BEGIN
    DELETE FROM application_logs WHERE timestamp < NOW() - INTERVAL '30 days';
    DELETE FROM user_sessions WHERE expires_at < NOW();
    DELETE FROM performance_metrics WHERE timestamp < NOW() - INTERVAL '30 days';
END;
$$ LANGUAGE plpgsql;

-- Create admin user with environment-based password
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'khoded_admin') THEN
        -- In production, this will use the password from Docker secrets
        -- In development, use the environment variable
        CREATE ROLE khoded_admin WITH LOGIN PASSWORD current_setting('app.admin_password');
        GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO khoded_admin;
        GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO khoded_admin;
        GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO khoded_admin;
    END IF;
END
$$;