#!/bin/bash

echo "🔐 Criando usuário:"
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria",
    "email": "maria@empresa.com",
    "role": "ANALYST"
}'

echo -e "\n\n📋 Criando política:"
curl -X POST http://localhost:8080/policies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Política de Backup",
    "description": "Define regras de backup diário",
    "documentUrl": "https://empresa.com/politicas/backup.pdf",
    "validUntil": "2026-12-31"
}'

echo -e "\n\n💾 Criando ativo:"
curl -X POST http://localhost:8080/api/assets \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Servidor de Banco de Dados",
    "description": "Servidor utilizado para hospedar o banco de dados da aplicação",
    "assetType": {
      "id": "UUID_DO_ASSET_TYPE"
    },
    "owner": "João da Silva",
    "acquisitionDate": "2023-10-01",
    "acquisitionValue": 15000.00,
    "classification": "CRITICO"
  }'


echo -e "\n\n⚠️ Criando risco (substituir UUID_DO_ATIVO):"
curl -X POST http://localhost:8080/risks \
  -H "Content-Type: application/json" \
  -d '{
    "assetId": "UUID_DO_ATIVO",
    "description": "Risco de falha elétrica",
    "category": "Operacional",
    "probability": 4,
    "impact": 5,
    "treatmentPlan": "Instalar nobreak",
    "status": "IDENTIFIED"
}'

echo -e "\n\n✅ Criando evidência de auditoria:"
curl -X POST http://localhost:8080/audits \
  -H "Content-Type: application/json" \
  -d '{
    "clause": "A.12.1.1 - Controle de Acesso Físico",
    "evidence": "Fotos da sala de servidores com controle biométrico",
    "date": "2025-08-01",
    "auditor": "Maria Andrade",
    "status": "CONFORME"
}'

echo -e "\n\n🚨 Reportando incidente:"
curl -X POST http://localhost:8080/incidents \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Acesso não autorizado detectado",
    "description": "Usuário externo acessou servidor",
    "incidentSeverity": "HIGH",
    "incidentStatus": "PENDING",
    "assignedTo": "Equipe de Segurança"
}'

echo -e "\n\n📥 Aceite de política (substituir UUIDs):"
curl -X POST "http://localhost:8080/acknowledgements?userId=UUID_USUARIO&policyId=UUID_POLITICA"

echo -e "\n\n📊 Listando dados:"
curl http://localhost:8080/users
curl http://localhost:8080/policies
curl http://localhost:8080/assets
curl http://localhost:8080/risks
curl http://localhost:8080/audits
curl http://localhost:8080/incidents
