-- Criação da tb_policy_content
CREATE TABLE tb_audit_finding (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    service VARCHAR(255) NOT NULL,
    rule_id VARCHAR(255) NOT NULL,
    resource_name VARCHAR(255),
    environment VARCHAR(50),
    involves_personal_data BOOLEAN NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE
);
