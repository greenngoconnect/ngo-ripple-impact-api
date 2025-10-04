-- Criação da tb_data_subject
CREATE TABLE tb_consent (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    data_subject_id UUID NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    granted BOOLEAN NOT NULL DEFAULT TRUE,
    source VARCHAR(50),
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_data_subject FOREIGN KEY (data_subject_id) REFERENCES tb_data_subject(id) ON DELETE CASCADE ON UPDATE CASCADE
);
