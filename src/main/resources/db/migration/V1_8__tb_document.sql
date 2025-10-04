-- Criação da tb_document.sql
CREATE TABLE tb_document (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,                        -- Breve descrição do documento
    document_type VARCHAR(50),               -- Ex: "PDF", "LOG", "IMAGE", "REPORT"
    storage_path VARCHAR(500),               -- Caminho/URL no S3, GCP, Azure, ou servidor interno
    hash_checksum VARCHAR(128),              -- SHA256 ou MD5 p/ validar integridade
    uploaded_by VARCHAR(255) NOT NULL,       -- Quem fez upload
    uploaded_date timestamptz NOT NULL DEFAULT now(),
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE
);
