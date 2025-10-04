-- Criação da tb_remediation_steps
CREATE TABLE tb_remediation_steps (
    advice_id UUID NOT NULL,
    step TEXT NOT NULL,
    CONSTRAINT fk_advice FOREIGN KEY (advice_id) REFERENCES tb_audit_advice(id)
);
