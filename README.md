✅ Como usar:
Cole esse conteúdo num arquivo chamado test-api-sgsi.sh

Torne o arquivo executável:

bash
Copy
Edit
chmod +x test-api-sgsi.sh
Execute:

bash
Copy
Edit
./test-api-sgsi.sh
Se quiser, posso subir esse conteúdo em um repositório GitHub para facilitar o acesso. Deseja isso?










Ask ChatGPT
# oakshield-api

=================================
# Criar titular
curl -X POST http://localhost:8080/api/data-subjects \
-H 'Content-Type: application/json' \
-d '{"name":"Ana Souza","email":"ana@exemplo.com","phone":"+55 34 9...","nationalId":"123.456.789-00"}'

# Conceder consentimento
curl -X POST http://localhost:8080/api/consents \
-H 'Content-Type: application/json' \
-d '{"dataSubject":{"id":"<UUID_DO_TITULAR>"},"purpose":"newsletter"}'

# Revogar consentimento
curl -X POST http://localhost:8080/api/consents/<CONSENT_ID>/revoke

# Cadastrar item de inventário para o titular
curl -X POST http://localhost:8080/api/personal-data \
-H 'Content-Type: application/json' \
-d '{"dataCategory":"Identificação","purpose":"Suporte","legalBasis":"CONTRACT",
"retentionPeriod":"5 anos","accessScope":"Suporte N1",
"dataSubject":{"id":"<UUID_DO_TITULAR>"}}'

# Solicitação de acesso
curl -X POST http://localhost:8080/api/subject-requests \
-H 'Content-Type: application/json' \
-d '{"dataSubject":{"id":"<UUID_DO_TITULAR>"},"type":"ACCESS","notes":"Quero cópia de todos os dados."}'
# ngo-ripple-impact-api
