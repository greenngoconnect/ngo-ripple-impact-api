-- Criação da tb_incident
CREATE TABLE tb_incident_response_action (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    incident_id UUID NOT NULL,
    action_description TEXT NOT NULL,
    action_taken_by VARCHAR(255),
    action_date timestamptz NOT NULL DEFAULT now(),
    status_at_action varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    create_by varchar(255) NOT NULL DEFAULT 'system_user',
    created_date timestamp DEFAULT CURRENT_DATE,
    last_modified_by varchar(255),
    last_modified_date timestamp DEFAULT CURRENT_DATE,
    FOREIGN KEY (incident_id) REFERENCES tb_incident(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_actions_incident ON tb_incident_response_action(incident_id);
CREATE INDEX IF NOT EXISTS idx_actions_date ON tb_incident_response_action(action_date);