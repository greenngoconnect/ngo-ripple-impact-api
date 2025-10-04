-- Criação da tb_employee_role
CREATE TABLE tb_policy (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    document_url TEXT,
    valid_until DATE,
    policy_status VARCHAR(255) NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE
);
