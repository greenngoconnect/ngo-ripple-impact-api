# Contribuindo para ngo-ripple-impact-api

Obrigado por querer contribuir! Este guia descreve como preparar seu ambiente, o fluxo de trabalho de *fork → branch → PR*, o padrão de commits e as regras de qualidade.

---

## Visão Geral do Fluxo

1. **Fork** do repositório e **clone** do seu fork.
2. Configure o **upstream** para sincronizar com o repo original.
3. Crie uma **branch** temática a partir de `main`.  
   - Padrões: `feat/...`, `fix/...`, `chore/...`, `docs/...`, `test/...`, `refactor/...`
4. Faça mudanças, **commits no padrão Conventional Commits** e rode **testes/linters**.
5. Sincronize com `upstream/main`, **abra o PR** e siga o template.
6. Enderece o review; mantenedores farão *squash & merge* quando aprovado.

---

## Pré-requisitos

- **Java 21** (Temurin recomendado)
- **Maven Wrapper** (`./mvnw`) **ou** **Gradle Wrapper** (`./gradlew`)
- **Docker** (opcional, para banco local)  
- IDE à sua escolha (IntelliJ/VS Code/Eclipse)

> **Dica:** Se o projeto usar **Testcontainers**, não precisa subir DB local; os testes iniciam o container automaticamente.

---

## Ambiente Local

### Com Maven
```bash
./mvnw -v
./mvnw -q -DskipTests=false test
