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


https://ripple-impact-net.lovable.app/dashboard

==============================
java -jar wiremock-standalone.jar \
--port 9090 \
--verbose \
--extensions org.wiremock.extension.responsetemplating.ResponseTemplateTransformer


==============================
mvn exec:java -Dexec.mainClass=com.github.tomakehurst.wiremock.standalone.WireMockServerRunner \
-Dexec.args="--port 9090 --verbose --root-dir src/test/resources/wiremock \
--extensions org.wiremock.extension.responsetemplating.ResponseTemplateTransformer"

Ou, se tiver configurado o <execution> no plugin, apenas:
mvn pre-integration-test

Testar localmente
curl -X GET http://localhost:9090/v1/ngos | jq .


====
💡 Dica bônus: subir WireMock junto aos testes de integração

Você pode criar um perfil Maven chamado wiremock e iniciar o servidor antes dos testes:

<profiles>
  <profile>
    <id>wiremock</id>
    <activation><activeByDefault>true</activeByDefault></activation>
    <build>
      <plugins>
        <!-- Aqui o exec-maven-plugin mostrado acima -->
      </plugins>
    </build>
  </profile>
</profiles>


E rodar:

mvn test -Pwiremock


docker run -it --rm -p 8080:8080 \
-v $(pwd)/wiremock:/home/wiremock \
wiremock/wiremock:3.6.0

curl -X GET http://localhost:8080/v1/ngo-categories
--
curl -X GET http://localhost:8080/v1/ngos
curl -X GET http://localhost:8080/v1/ngos/11111111-1111-1111-1111-111111111111
curl -X POST http://localhost:8080/v1/ngos -H "Content-Type: application/json" -d '{"name":"Nova ONG","email":"nova@ong.org"}'
---
curl -X GET http://localhost:8080/v1/departments
curl -X GET http://localhost:8080/v1/departments/11111111-1111-1111-1111-111111111111
curl -X POST http://localhost:8080/v1/departments -H "Content-Type: application/json" \
-d '{"name":"Operações","ngo":{"id":"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"}}'
--
curl -s http://localhost:8080/v1/ngo-details | jq
curl -s http://localhost:8080/v1/ngo-details/11111111-1111-1111-1111-111111111111 | jq

curl -s -X POST http://localhost:8080/v1/ngo-details \
-H "Content-Type: application/json" \
-d '{
"ngo": {"id":"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"},
"responsibleName":"Maria Silva",
"responsibleEmail":"maria@verdefuturo.org",
"responsiblePhone":"+55 34 99999-9999",
"address":"Rua das Flores, 123 - Uberlândia/MG",
"mission":"Promover educação ambiental",
"vision":"Referência em educação ambiental",
"values":"Sustentabilidade, Ética, Comunidade"
}' | jq
----
curl -s http://localhost:8080/v1/users | jq
curl -s http://localhost:8080/v1/users/11111111-1111-1111-1111-111111111111 | jq
curl -s -X POST http://localhost:8080/v1/users \
-H "Content-Type: application/json" \
-d '{"name":"Alice","email":"alice@greenngo.org","password":"123456","role":"ADMIN","consentAccepted":true}' | jq
---
# Login
curl -s -X POST http://localhost:8080/v1/auth/authenticate \
-H "Content-Type: application/json" \
-d '{"email":"user@greenngo.org","password":"secret"}' | jq

# Refresh OK
curl -s -X POST http://localhost:8080/v1/auth/refresh-token \
-H "Content-Type: application/json" \
-d '{"refreshToken":"REFRESH_TOKEN_SAMPLE"}' | jq

# Refresh inválido → 401
curl -i -s -X POST http://localhost:8080/v1/auth/refresh-token \
-H "Content-Type: application/json" \
-d '{"refreshToken":"INVALID"}'

# Consentimento
curl -s http://localhost:8080/v1/auth/user/11111111-1111-1111-1111-111111111111/consent/accept | jq
curl -s -X PUT http://localhost:8080/v1/auth/user/11111111-1111-1111-1111-111111111111/consent/accept
