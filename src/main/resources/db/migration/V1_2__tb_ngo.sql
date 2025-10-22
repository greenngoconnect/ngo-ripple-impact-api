-- Criação da tb_ngo
CREATE TABLE tb_ngo (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   ngo_category_id UUID NOT NULL,
   name VARCHAR(255) NOT NULL,
   fantasy_name VARCHAR(255) NOT NULL UNIQUE,
   description TEXT NOT NULL,
   company_id VARCHAR(20) NOT NULL UNIQUE,
   email VARCHAR(255) NOT NULL UNIQUE,
   phone VARCHAR(20) NOT NULL UNIQUE,
   ngo_status VARCHAR(50) NOT NULL,
   status varchar(255) NOT NULL,
   create_by varchar(255) NOT NULL DEFAULT 'system_user',
   created_date timestamp DEFAULT CURRENT_DATE,
   last_modified_by varchar(255),
   last_modified_date timestamp DEFAULT CURRENT_DATE,
   FOREIGN KEY (ngo_category_id) REFERENCES tb_ngo_category(id) ON DELETE CASCADE
);