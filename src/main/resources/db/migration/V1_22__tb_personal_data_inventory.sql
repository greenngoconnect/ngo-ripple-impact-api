-- Criação da tb_personal_data_inventory
CREATE TABLE tb_personal_data_inventory (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    data_category VARCHAR(50) NOT NULL,   -- enum DataCategory como string
    purpose VARCHAR(255) NOT NULL,
    legal_basis VARCHAR(50) NOT NULL,     -- enum LegalBasis como string
    retention_period INTEGER NOT NULL,    -- meses
    access_scope VARCHAR(255) NOT NULL,
    data_subject_id UUID NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_data_subject FOREIGN KEY (data_subject_id) REFERENCES tb_data_subject(id) ON DELETE CASCADE ON UPDATE CASCADE
);
