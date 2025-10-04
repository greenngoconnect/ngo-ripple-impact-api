-- Criação da tb_data_subject_request
CREATE TABLE tb_data_subject_request (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    data_subject_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,       -- Enum RequestType armazenado como string
    request_status VARCHAR(50) NOT NULL,     -- Enum RequestStatus armazenado como string
    request_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_at TIMESTAMP WITH TIME ZONE NOT NULL,
    closed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    notes TEXT,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_data_subject FOREIGN KEY (data_subject_id) REFERENCES tb_data_subject(id) ON DELETE CASCADE ON UPDATE CASCADE
);
