# Publicar mensagem

## Contexto

Esta ação pertence à primeira entrega do módulo Chat e usa `POST /api/v1/channels/{channelId}/messages`.

## Tarefa

Publicar uma mensagem de texto num canal acessível ao utilizador autenticado.

## Regras

- O pedido contém texto não vazio de até 4.000 caracteres e o cabeçalho `Idempotency-Key` com 1–128 caracteres.
- A identidade do autor vem do principal autenticado, nunca do corpo do pedido.
- O utilizador precisa pertencer ao workspace que contém o canal. Esta entrega aplica autorização por associação ao workspace; a matriz de permissões explícitas por canal será implementada depois.
- Repetir a mesma chave para o mesmo utilizador e canal com o mesmo texto devolve a mensagem original sem duplicar a gravação. A chave repetida com texto diferente retorna `409 Conflict`.
- Mensagem e evento `message.created` são gravados atomicamente na outbox. O evento contém identificador, tipo, escopo e instante; a entrega WebSocket fica fora desta entrega.
- Uma criação retorna `201 Created`; uma repetição idempotente retorna `200 OK`; utilizador sem associação recebe `403 Forbidden`.

## Definição de pronto

- O contrato documenta cabeçalho, corpo, respostas e erros.
- Mensagem e evento outbox são persistidos na mesma transação.
- Não há duplicação por repetição idempotente nem publicação por utilizador sem acesso.
- Testes cobrem publicação, autorização, repetição e conflito de chave.
