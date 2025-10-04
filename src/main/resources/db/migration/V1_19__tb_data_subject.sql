-- Criação da tb_data_subject
CREATE TABLE tb_data_subject (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    national_id_type VARCHAR(50) NOT NULL,
    national_id VARCHAR(60) NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE
);

CREATE TABLE tb_incident_data_subject (
    incident_id UUID NOT NULL,
    data_subject_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (incident_id, data_subject_id),
    FOREIGN KEY (incident_id) REFERENCES tb_incident(id) ON DELETE CASCADE,
    FOREIGN KEY (data_subject_id) REFERENCES tb_data_subject(id) ON DELETE CASCADE
);