-- Criação da tb_asset
CREATE TABLE tb_asset (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(255) NOT NULL,
   description TEXT,
   asset_type_id UUID REFERENCES tb_asset_type(id),
   owner_department_id UUID REFERENCES tb_department(id),
   acquisition_date DATE,
   acquisition_value FLOAT,
   classification VARCHAR(50) NOT NULL,
   status varchar(255) NOT NULL,
   create_by varchar(255) NOT NULL DEFAULT 'system_user',
   created_date timestamp DEFAULT CURRENT_DATE,
   last_modified_by varchar(255),
   last_modified_date timestamp DEFAULT CURRENT_DATE,
   FOREIGN KEY (asset_type_id) REFERENCES tb_asset_type(id) ON DELETE CASCADE
);

CREATE TABLE tb_asset_tags (
    asset_id UUID,
    asset_tag_id UUID,
    PRIMARY KEY (asset_id, asset_tag_id),
    FOREIGN KEY (asset_id) REFERENCES tb_asset(id) ON DELETE CASCADE,
    FOREIGN KEY (asset_tag_id) REFERENCES tb_asset_tag(id) ON DELETE CASCADE
);

CREATE TABLE tb_asset_service_resource (
    asset_id UUID NOT NULL,
    service_resource_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (asset_id, service_resource_id),
    FOREIGN KEY (asset_id) REFERENCES tb_asset(id) ON DELETE CASCADE,
    FOREIGN KEY (service_resource_id) REFERENCES tb_service_resource(id) ON DELETE CASCADE
);
