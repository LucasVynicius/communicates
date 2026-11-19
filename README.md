# Communicates API

Backend da plataforma de comunicação. Requer Java 21 e Docker com o plugin Compose.

## Arranque local

Inicie o PostgreSQL:

```sh
docker compose up -d db
```

Inicie a API com o perfil local. A configuração padrão usa a mesma base de dados e credenciais de desenvolvimento do Compose:

```sh
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

O Flyway cria as tabelas ao iniciar. Para usar outras credenciais, configure `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` e `DB_PORT` antes de iniciar o Compose; configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` para a API. As credenciais HTTP locais podem ser substituídas por `LOCAL_USERNAME` e `LOCAL_PASSWORD`.

## Testar criação de servidor

Com a API em execução, envie um pedido autenticado:

```sh
curl -i -u local-user:local-password \
  -H 'Content-Type: application/json' \
  -d '{"name":"Meu servidor","template":"DEV"}' \
  http://localhost:8080/api/v1/workspaces
```

Uma criação válida responde `201 Created` com o identificador, o modelo, os cargos e os canais iniciais. Os modelos aceites são `DEV`, `CREATOR`, `COMMUNITY` e `COMPANY`.

Para executar os testes:

```sh
./mvnw test
```

Para parar o banco:

```sh
docker compose down
```

Para apagar também os dados locais do banco:

```sh
docker compose down -v
```
