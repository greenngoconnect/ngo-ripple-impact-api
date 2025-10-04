
---

# `.github/pull_request_template.md`

```md
## O que foi feito
<!-- Descreva as mudanças de alto nível. Ex.: "Cria endpoint /impacts para buscar impactos por projeto." -->

## Por que
<!-- Contexto e motivação. Ex.: "Necessário para compor o relatório XYZ da ONG." -->

## Como testar
<!-- Passo a passo para validar localmente. Inclua env vars, comandos e dados de exemplo. -->
1.
2.
3.

## Impactos/risco
<!-- Migrações de DB? Mudanças em contrato de API? Backwards compatibility? -->

## Padrão de Commits

Adotamos **Conventional Commits** para padronizar mensagens e facilitar changelog/versionamento.  
👉 Consulte o guia rápido: [CONVENTIONAL_COMMITS.md](CONVENTIONAL_COMMITS.md)

**Exemplos**
- `feat(api): add /projects/{id}/impacts endpoint`
- `fix(db): handle null in migration V4`
- `docs(readme): update local setup instructions`

## Checklist
- [ ] Testes **passando** localmente (`./mvnw test` ou `./gradlew test`)
- [ ] Commits no padrão **Conventional Commits**
- [ ] Se aplicável, **docs** atualizados (README/Swagger)
- [ ] **Sem** queda relevante de cobertura (quando aplicável)
- [ ] Sincronizado com `upstream/main` (rebase/merge recente)

### Screenshots/Logs (opcional)
<!-- Cole aqui imagens/prints de respostas da API, logs de testes, etc. -->

