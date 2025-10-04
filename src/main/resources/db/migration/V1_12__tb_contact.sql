CREATE TABLE tb_contact (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    role VARCHAR(100),
    organization_id UUID NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    FOREIGN KEY (organization_id) REFERENCES tb_organization(id) ON DELETE SET NULL
);


