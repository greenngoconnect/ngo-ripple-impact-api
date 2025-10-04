# Conventional Commits — Mini Tutorial

Padronize suas mensagens de commit para facilitar revisão, changelog e versionamento semântico.

> **Formato básico**
>
> ```
> <type>(<scope>): <subject>
>
> <body opcional>
>
> <footer opcional>
> ```
>
> `type` = categoria da mudança (feat, fix, etc.)  
> `scope` = área afetada (api, db, auth, build, docs...)  
> `subject` = resumo curto e no imperativo (até ~50 chars)

---

## 1) Tipos mais comuns

| type        | Quando usar                                                                 |
|-------------|------------------------------------------------------------------------------|
| `feat`      | Nova funcionalidade (impacta MINOR do SemVer)                                |
| `fix`       | Correção de bug (impacta PATCH do SemVer)                                    |
| `docs`      | Apenas documentação                                                          |
| `style`     | Formatação, espaçamento, sem mudar comportamento (ex.: lints)                |
| `refactor`  | Refatoração sem correção nem feature                                         |
| `perf`      | Melhoria de desempenho                                                       |
| `test`      | Adição/ajuste de testes                                                      |
| `build`     | Mudanças em build, dependências, empacotamento                               |
| `ci`        | Pipelines e automações (GitHub Actions, etc.)                               |
| `chore`     | Tarefas diversas sem afetar código de produção                               |
| `revert`    | Reversão de commit anterior                                                  |

**Scopes** (exemplos): `api`, `domain`, `db`, `controller`, `service`, `config`, `infra`, `docs`, `deps`, `ci`.

---

## 2) Exemplos rápidos

- `feat(api): add /projects/{id}/impacts endpoint`
- `fix(db): handle null in migration V4`
- `docs(readme): update local setup instructions`
- `refactor(service): extract mapper and reduce duplication`
- `test(web): add controller integration tests`
- `ci: cache maven repo and upload surefire reports`

---

## 3) Commits com *BREAKING CHANGE*

Há duas formas:

1. **Exclamação no tipo** (curto):
   ```
   feat(api)!: rename /impacts to /ripple-impacts
   ```
2. **Rodapé explícito** (detalhado):
   ```
   feat(api): rename /impacts to /ripple-impacts

   BREAKING CHANGE: clients must update the endpoint path
   ```

Qualquer `BREAKING CHANGE` implica **major** no SemVer.

---

## 4) Anatomia (detalhes úteis)

- **Subject**: voz imperativa, objetivo, ~50 caracteres.
  - ✅ `fix: prevent NPE on null project`
  - ❌ `fixed…`, `fixing…`, `bugfix…`
- **Body** (opcional): explique **o porquê** e **como** (linhas de ~72 chars).
- **Footer** (opcional): metadados, referências, **BREAKING CHANGE**, links e fechamento de issues:
  ```
  Closes #123
  Co-authored-by: Nome <email@exemplo.com>
  ```

---

## 5) SemVer — relação com os tipos

- `fix:` → **PATCH** (x.y.Z)
- `feat:` → **MINOR** (x.Y.z)
- `BREAKING CHANGE` (ou `type!:`) → **MAJOR** (X.y.z)

> Obs.: Outros tipos (docs, chore, test…) normalmente não mudam versão semântica por si só.

---

## 6) Dicas e anti-padrões

**Dicas**
- Commits pequenos e atômicos → revisão fácil.
- Um assunto por commit → *reverts* e *bisects* mais previsíveis.
- Prefira `scope` quando houver múltiplos módulos/camadas.

**Evite**
- Assuntos genéricos (`update`, `fix bug`, `adjustments`).
- Commits gigantes com mudanças mistas (docs+feat+fix).
- Mensagens no gerúndio/passado (`fixed`, `adding`).

---

## 7) Template sugerido (use no editor)

```
<type>(<scope>): <subject curto no imperativo>

Contexto:
- Por que essa mudança é necessária?

O que foi feito:
- Itens principais

Como testar:
- Passo a passo
- Dados/inputs de exemplo

Refs:
- Closes #<issue>
- BREAKING CHANGE: <descrição se houver>
```

---

## 8) Ferramentas (opcional, mas ajudam muito)

- **commitlint**: valida o padrão.
- **Commitizen**: guia interativo para escrever mensagens.
- **Husky**: ganchos de git para rodar validações no `commit-msg`.

Exemplo mínimo de `commitlint.config.js`:
```js
export default {
  extends: ['@commitlint/config-conventional'],
};
```

---

## 9) FAQ rápido

- **Preciso sempre de `scope`?**  
  Não. Use quando agrega clareza (ex.: `feat(api): ...`).

- **`docs:` vale para README e Swagger/OpenAPI?**  
  Sim, qualquer documentação.

- **Posso usar múltiplos tipos num commit?**  
  Não. Quebre em commits menores ou escolha o tipo predominante.

---

## 10) Checagem-relâmpago (checklist)

- [ ] Tipo correto (`feat`, `fix`, `docs`, …)
- [ ] `scope` (se útil)
- [ ] Assunto curto e claro, no imperativo
- [ ] Body explica **por quê** e **como** (se necessário)
- [ ] Rodapé com `Closes #n` e/ou `BREAKING CHANGE` (se houver)

---
