-- Criação da tb_policy_content
CREATE TABLE tb_policy_content (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    policy_id UUID NOT NULL,
    format VARCHAR(8) NOT NULL,
    content_md_html OID,
    content_pdf OID,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_policy FOREIGN KEY (policy_id) REFERENCES tb_policy(id) ON DELETE CASCADE ON UPDATE CASCADE
);
