-- afterMigrate__seed.sql (PostgreSQL + Flyway)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Tabela de controle
CREATE TABLE IF NOT EXISTS public.app_control (
  key text PRIMARY KEY,
  created_at timestamptz DEFAULT now()
);

DO $do$
BEGIN
  -- roda uma única vez
  IF EXISTS (SELECT 1 FROM public.app_control WHERE key='seed_v1_done') THEN
    RAISE NOTICE 'Seed já aplicado. Saindo.';
    RETURN;
  END IF;

  ---------------------------------------------------------------------------
  -- ROLES -------------------------------------------------------------------
  ---------------------------------------------------------------------------
  IF to_regclass('public.roles') IS NOT NULL THEN
    IF NOT EXISTS (SELECT 1 FROM public.roles WHERE id = 1) THEN
      INSERT INTO public.roles(id, name) VALUES (1,'ADMIN');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM public.roles WHERE id = 2) THEN
      INSERT INTO public.roles(id, name) VALUES (2,'USER');
    END IF;
  ELSE
    RAISE NOTICE 'Tabela public.roles não existe. Pulando roles.';
  END IF;

  ---------------------------------------------------------------------------
  -- TB_ASSET_TYPE -----------------------------------------------------------
  ---------------------------------------------------------------------------
  IF to_regclass('public.tb_asset_type') IS NOT NULL THEN
    -- TECNOLOGIA
    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Servidor', 'TECNOLOGIA','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Servidor' AND assert_group='TECNOLOGIA');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Notebook', 'TECNOLOGIA','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Notebook' AND assert_group='TECNOLOGIA');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Dispositivo Móvel','TECNOLOGIA','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Dispositivo Móvel' AND assert_group='TECNOLOGIA');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Firewall','TECNOLOGIA','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Firewall' AND assert_group='TECNOLOGIA');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Switch','TECNOLOGIA','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Switch' AND assert_group='TECNOLOGIA');

    -- SOFTWARE
    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Sistema ERP','SOFTWARE','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Sistema ERP' AND assert_group='SOFTWARE');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Sistema de E-mail','SOFTWARE','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Sistema de E-mail' AND assert_group='SOFTWARE');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Aplicação Web','SOFTWARE','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Aplicação Web' AND assert_group='SOFTWARE');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'API Interna','SOFTWARE','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='API Interna' AND assert_group='SOFTWARE');

    -- DOCUMENTAL
    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Contrato de Prestação de Serviço','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Contrato de Prestação de Serviço' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Contrato de SLA','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Contrato de SLA' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Política de Segurança da Informação','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Política de Segurança da Informação' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Plano de Continuidade de Negócios','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Plano de Continuidade de Negócios' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Plano de Recuperação de Desastres','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Plano de Recuperação de Desastres' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Manual de Boas Práticas','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Manual de Boas Práticas' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Relatório de Auditoria','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Relatório de Auditoria' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Política de Backup','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Política de Backup' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Acordo de Confidencialidade','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Acordo de Confidencialidade' AND assert_group='DOCUMENTAL');

    INSERT INTO public.tb_asset_type (id, name, assert_group, create_by, created_date, last_modified_by, last_modified_date, status)
    SELECT gen_random_uuid(), 'Política de Controle de Acesso','DOCUMENTAL','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM public.tb_asset_type WHERE name='Política de Controle de Acesso' AND assert_group='DOCUMENTAL');
  ELSE
    RAISE NOTICE 'Tabela public.tb_asset_type não existe. Pulando asset types.';
  END IF;

  ---------------------------------------------------------------------------
  -- TB_AUDIT_RECORD ---------------------------------------------------------
  ---------------------------------------------------------------------------
  IF to_regclass('public.tb_audit_record') IS NOT NULL THEN
    IF NOT EXISTS (SELECT 1 FROM public.tb_audit_record WHERE clause='A.5.1.1' AND evidence='Políticas de segurança documentadas') THEN
      INSERT INTO public.tb_audit_record
        (id, clause, evidence, "date", auditor, audit_status, create_by, created_date, last_modified_by, last_modified_date, status)
      VALUES
        (gen_random_uuid(), 'A.5.1.1', 'Políticas de segurança documentadas', '2023-05-14','Auditoria Interna','PENDING',
         'root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE');
    END IF;
    -- (repita as demais linhas seguindo o mesmo padrão)
  ELSE
    RAISE NOTICE 'Tabela public.tb_audit_record não existe. Pulando audit records.';
  END IF;

  ---------------------------------------------------------------------------
  -- TB_POLICY / TB_POLICY_TEMPLATE -----------------------------------------
  ---------------------------------------------------------------------------
  IF to_regclass('public.tb_policy') IS NOT NULL THEN
    IF NOT EXISTS (SELECT 1 FROM public.tb_policy WHERE id = '98033816-8bb1-4e19-8df1-17c34c4dbe6f') THEN
      INSERT INTO public.tb_policy (
        id, name, description, document_url, valid_until, policy_status,
        create_by, created_date, last_modified_by, last_modified_date, status
      ) VALUES (
        '98033816-8bb1-4e19-8df1-17c34c4dbe6f',
        'Política de Segurança da Informação',
        'Esta política estabelece as diretrizes e responsabilidades para garantir a segurança da informação na organização.',
        NULL, '2025-12-31', 'ACTIVE',
        'root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
      );
    END IF;
  ELSE
    RAISE NOTICE 'Tabela public.tb_policy não existe. Pulando tb_policy.';
  END IF;

  IF to_regclass('public.tb_policy_template') IS NOT NULL THEN
    IF NOT EXISTS (
      SELECT 1 FROM public.tb_policy_template
      WHERE policy_id='98033816-8bb1-4e19-8df1-17c34c4dbe6f' AND code='POL-ACCESS-CONTROL' AND language='pt-BR'
    ) THEN
      INSERT INTO public.tb_policy_template (
        id, policy_id, code, language, title, version, body_template, controls_refs_json, template_status,
        create_by, created_date, last_modified_by, last_modified_date, status
      ) VALUES (
        gen_random_uuid(),
        '98033816-8bb1-4e19-8df1-17c34c4dbe6f',
        'POL-ACCESS-CONTROL','pt-BR','Política de Controle de Acesso','1.0.0',
        $ftl$<#-- POL-ACCESS-CONTROL pt-BR -->
# Política de Controle de Acesso – ${org_name!""}
$ftl$,
        '[{"framework":"ISO27001","clause":"A.9","section":"Access Control"}]',
        'ACTIVE','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
      );
    END IF;

    IF NOT EXISTS (
      SELECT 1 FROM public.tb_policy_template
      WHERE policy_id='98033816-8bb1-4e19-8df1-17c34c4dbe6f' AND code='POL-BACKUP' AND language='pt-BR'
    ) THEN
      INSERT INTO public.tb_policy_template (
        id, policy_id, code, language, title, version, body_template, controls_refs_json, template_status,
        create_by, created_date, last_modified_by, last_modified_date, status
      ) VALUES (
        gen_random_uuid(),
        '98033816-8bb1-4e19-8df1-17c34c4dbe6f',
        'POL-BACKUP','pt-BR','Política de Backup e Restauração','1.0.0',
        $ftl$<#-- POL-BACKUP pt-BR -->
# Política de Backup e Restauração – ${org_name!""}
$ftl$,
        '[{"framework":"ISO27001","clause":"A.12.3","section":"Backup"}]',
        'ACTIVE','root@localhost.com','2023-05-14 18:38:20','root@localhost.com','2023-05-14 18:38:20','ACTIVE'
      );
    END IF;
  ELSE
    RAISE NOTICE 'Tabela public.tb_policy_template não existe. Pulando tb_policy_template.';
  END IF;

  -- marca seed aplicado
  INSERT INTO public.app_control(key) VALUES ('seed_v1_done');

END
$do$;
