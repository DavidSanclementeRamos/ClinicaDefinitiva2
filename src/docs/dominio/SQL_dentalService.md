-- tabla principal de servicios provistos
CREATE TABLE provided_service (
id VARCHAR(36) PRIMARY KEY,
name VARCHAR(200) NOT NULL,
service_type VARCHAR(50) NOT NULL, -- discriminador
catalog_id VARCHAR(36) NOT NULL,
catalog_name VARCHAR(200) NOT NULL,
catalog_category VARCHAR(100) NOT NULL,
code VARCHAR(50) NOT NULL UNIQUE,
base_rate_amount NUMERIC(12,2),
base_rate_currency VARCHAR(3),
duration_minutes INT,
requires_authorization BOOLEAN,
description TEXT,
status VARCHAR(50),
created_at TIMESTAMP,
updated_at TIMESTAMP
);

-- tabla específica para ortodoncia (one-to-one por provided_service.id)
CREATE TABLE provided_service_orthodontic (
provided_service_id VARCHAR(36) PRIMARY KEY,
appliance_type VARCHAR(100),
treatment_duration_months INT,
requires_followup BOOLEAN,
FOREIGN KEY (provided_service_id) REFERENCES provided_service(id) ON DELETE CASCADE
);

CREATE INDEX idx_orthodontic_treatment_duration ON provided_service_orthodontic (treatment_duration_months);
CREATE INDEX idx_provided_service_code ON provided_service (code);
CREATE INDEX idx_provided_service_type ON provided_service (service_type);