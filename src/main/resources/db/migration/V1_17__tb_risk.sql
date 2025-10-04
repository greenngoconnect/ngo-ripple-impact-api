-- Criação da tb_risk
CREATE TABLE tb_risk (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    asset_id UUID NOT NULL,
    description TEXT,
    category VARCHAR(50),
    probability VARCHAR(50),
    impact VARCHAR(50),
    risk_score INTEGER,
    treatment_plan TEXT,
    risk_status VARCHAR(50),
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    FOREIGN KEY (asset_id) REFERENCES tb_asset(id) ON DELETE CASCADE
);
