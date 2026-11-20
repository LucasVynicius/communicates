# Módulo Chat — primeira entrega

## Contexto

O primeiro recorte do módulo Chat cobre mensagens em canais de servidores já criados pelo módulo Servidores. A rota do backend segue o prefixo usado pela API existente: `/api/v1`.

## Tarefa

Implementar publicação de mensagem e consulta do histórico de um canal:

- `POST /api/v1/channels/{channelId}/messages`
- `GET /api/v1/channels/{channelId}/messages`

## Regras

- Mensagens têm identificador UUID, canal, utilizador autor, texto e instante de criação definido pelo servidor.
- O pedido de publicação contém texto não vazio, limitado inicialmente a 4.000 caracteres, e o cabeçalho `Idempotency-Key` (1–128 caracteres).
- Repetir a mesma chave para o mesmo utilizador e canal com o mesmo texto devolve a mensagem original sem criar outra. Reutilizar a chave com texto diferente retorna `409 Conflict`.
- Antes de publicar ou consultar, revalidar que o utilizador pertence ao workspace que contém o canal. A associação ao workspace é a regra de autorização deste primeiro recorte; permissões explícitas por canal ficam para a implementação da matriz de permissões.
- Gravar a mensagem e o evento `message.created` na outbox na mesma transação. A entrega WebSocket não faz parte desta entrega; clientes podem recuperar mensagens pela consulta de histórico após reconexão.
- O histórico usa cursor opaco e ordenação estável por instante de criação e UUID. A primeira página retorna as mensagens mais recentes, em ordem cronológica ascendente; páginas anteriores usam o cursor devolvido. O limite padrão é 50 e o máximo é 100.
- Não incluir anexos, threads, reações, DMs nem pesquisa nesta entrega. Não adicionar dependências.

## Definição de pronto

- Os dois contratos documentam autenticação, autorização, pedidos, respostas e erros.
- Publicação persiste mensagem e outbox atomicamente, respeita idempotência e impede acesso de utilizadores que não sejam membros do workspace.
- Consulta respeita autorização e cursor, sem duplicar ou omitir mensagens entre páginas.
- Testes cobrem regras de domínio, casos de uso, HTTP, isolamento entre workspaces, idempotência, paginação e persistência com PostgreSQL.
- `./mvnw test` passa com PostgreSQL disponível pelo Compose.
