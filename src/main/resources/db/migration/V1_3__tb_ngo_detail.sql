-- Criação da tb_ngo_detail
CREATE TABLE tb_ngo_detail (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   ngo_id UUID NOT NULL,
   responsible_name VARCHAR(255) NOT NULL UNIQUE,
   responsible_email VARCHAR(255) NOT NULL UNIQUE,
   responsible_phone VARCHAR(20) NOT NULL UNIQUE,
   address VARCHAR(255) NOT NULL,
   mission TEXT,
   vision TEXT,
   values TEXT,
   status varchar(255) NOT NULL,
   create_by varchar(255) NOT NULL DEFAULT 'system_user',
   created_date timestamp DEFAULT CURRENT_DATE,
   last_modified_by varchar(255),
   last_modified_date timestamp DEFAULT CURRENT_DATE,
   CONSTRAINT fk_ngo FOREIGN KEY (ngo_id) REFERENCES tb_ngo(id)
);