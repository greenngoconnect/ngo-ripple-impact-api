-- Criação da tb_incident
CREATE TABLE tb_incident (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    incident_severity VARCHAR(50),
    incident_status VARCHAR(50),
    assigned_to VARCHAR(255),
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    detection_source VARCHAR(255),
    containment_measures TEXT,
    next_actions TEXT,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE
);

CREATE TABLE tb_incident_mitre_technique (
    incident_id UUID NOT NULL,
    mitre_technique_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (incident_id, mitre_technique_id),
    FOREIGN KEY (incident_id) REFERENCES tb_incident(id) ON DELETE CASCADE,
    FOREIGN KEY (mitre_technique_id) REFERENCES tb_mitre_technique(id) ON DELETE CASCADE
);

CREATE TABLE tb_incident_indicator (
    incident_id UUID NOT NULL,
    indicator_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (incident_id, indicator_id),
    FOREIGN KEY (incident_id) REFERENCES tb_incident(id) ON DELETE CASCADE,
    FOREIGN KEY (indicator_id) REFERENCES tb_indicator(id) ON DELETE CASCADE
);

CREATE TABLE tb_incident_contact (
    incident_id UUID NOT NULL,
    contact_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (incident_id, contact_id),
    FOREIGN KEY (incident_id) REFERENCES tb_incident(id) ON DELETE CASCADE,
    FOREIGN KEY (contact_id) REFERENCES tb_contact(id) ON DELETE CASCADE
);

CREATE TABLE tb_incident_document (
    incident_id UUID NOT NULL,
    document_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (incident_id, document_id),
    FOREIGN KEY (incident_id) REFERENCES tb_incident(id) ON DELETE CASCADE,
    FOREIGN KEY (document_id) REFERENCES tb_document(id) ON DELETE CASCADE
);

CREATE TABLE tb_incident_asset (
    incident_id UUID NOT NULL,
    asset_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (incident_id, asset_id),
    FOREIGN KEY (incident_id) REFERENCES tb_incident(id) ON DELETE CASCADE,
    FOREIGN KEY (asset_id) REFERENCES tb_asset(id) ON DELETE CASCADE
);

