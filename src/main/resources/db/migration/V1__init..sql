CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

create table tb_user (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    create_by varchar(255) not null,
    created_date timestamp(6) not null,
    last_modified_by varchar(255),
    last_modified_date timestamp(6),
    status varchar(255) not null,
    email varchar(255),
    name varchar(255),
    password varchar(255),
    role varchar(255)
);

create table tb_token (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    expired boolean not null,
    revoked boolean not null,
    token varchar(255),
    token_type varchar(255),
    user_id UUID not null,
    FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE
);