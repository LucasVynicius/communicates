# Consultar histórico de mensagens

## Contexto

Esta ação pertence à primeira entrega do módulo Chat e usa `GET /api/v1/channels/{channelId}/messages`.

## Tarefa

Consultar mensagens de um canal com paginação por cursor, permitindo recuperar o histórico após uma reconexão.

## Regras

- O utilizador autenticado precisa pertencer ao workspace que contém o canal. A autorização é revalidada em cada pedido.
- A primeira página retorna as mensagens mais recentes em ordem cronológica ascendente.
- `limit` tem padrão 50 e máximo 100.
- O cursor é opaco e codifica instante e UUID para ordenação estável; a página seguinte retorna mensagens anteriores à mais antiga da página atual.
- Mensagens de canais ou workspaces sem associação não são expostas; estes pedidos retornam `403 Forbidden`.
- Um cursor malformado ou um limite fora de 1–100 retorna `400 Bad Request`.

## Definição de pronto

- O contrato documenta cursor, limite, ordenação, resposta e erros.
- Páginas sucessivas não repetem nem omitem mensagens.
- A autorização é verificada antes de devolver cada página.
- Testes cobrem histórico vazio, paginação e isolamento de acesso.
