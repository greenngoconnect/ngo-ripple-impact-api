-- Criação da tb_service
CREATE TABLE tb_service_resource (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(255) NOT NULL,
   service_resource_type_id UUID REFERENCES tb_service_resource_type(id),
   status varchar(255) NOT NULL,
   create_by varchar(255) NOT NULL DEFAULT 'system_user',
   created_date timestamp DEFAULT CURRENT_DATE,
   last_modified_by varchar(255),
   last_modified_date timestamp DEFAULT CURRENT_DATE,
   FOREIGN KEY (service_resource_type_id) REFERENCES tb_service_resource_type(id) ON DELETE CASCADE
);