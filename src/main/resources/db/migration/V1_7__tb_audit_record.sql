-- Criação da tb_audit_record
CREATE TABLE tb_audit_record (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    clause VARCHAR(255) NOT NULL,
    evidence TEXT,
    date DATE,
    auditor VARCHAR(255),
    audit_status VARCHAR(50) NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE
);
