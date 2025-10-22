-- Criação da tb_service
CREATE TABLE tb_department (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   ngo_id UUID NOT NULL,
   name VARCHAR(255) NOT NULL,
   status varchar(255) NOT NULL,
   create_by varchar(255) NOT NULL DEFAULT 'system_user',
   created_date timestamp DEFAULT CURRENT_DATE,
   last_modified_by varchar(255),
   last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_department FOREIGN KEY (ngo_id) REFERENCES tb_ngo(id) ON DELETE CASCADE
);