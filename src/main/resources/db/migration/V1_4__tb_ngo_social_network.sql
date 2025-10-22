-- Criação da tb_ngo_social_network
CREATE TABLE tb_ngo_social_network (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   ngo_id UUID NOT NULL,
   linkedin VARCHAR(255) NOT NULL,
   twitter_x VARCHAR(255),
   instagram VARCHAR(255),
   website VARCHAR(255),
   status varchar(255) NOT NULL,
   create_by varchar(255) NOT NULL DEFAULT 'system_user',
   created_date timestamp DEFAULT CURRENT_DATE,
   last_modified_by varchar(255),
   last_modified_date timestamp DEFAULT CURRENT_DATE,
    CONSTRAINT fk_ngo_social_network FOREIGN KEY (ngo_id) REFERENCES tb_ngo(id) ON DELETE CASCADE
);