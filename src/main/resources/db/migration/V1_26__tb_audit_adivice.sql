-- Criação da tb_audit_advice
CREATE TABLE tb_audit_advice (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    business_severity VARCHAR(50) NOT NULL,
    explanation_pt TEXT,
    should_open_ticket BOOLEAN NOT NULL,
    finding_id UUID NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_finding FOREIGN KEY (finding_id) REFERENCES tb_audit_finding(id) ON DELETE CASCADE ON UPDATE CASCADE
);
