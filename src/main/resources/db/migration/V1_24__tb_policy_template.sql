-- Criação da tb_policy_content
CREATE TABLE tb_policy_template (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    policy_id UUID NOT NULL,
    code VARCHAR(80) NOT NULL,
    language VARCHAR(10) NOT NULL,
    title VARCHAR(255) NOT NULL,
    version VARCHAR(20) NOT NULL,
    body_template TEXT NOT NULL,
    controls_refs_json TEXT,
    template_status VARCHAR(50) NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_policy FOREIGN KEY (policy_id) REFERENCES tb_policy(id) ON DELETE CASCADE ON UPDATE CASCADE
);
