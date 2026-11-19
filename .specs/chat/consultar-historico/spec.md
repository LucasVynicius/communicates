# Consultar histórico de mensagens

## Contexto

O endpoint inicial é `GET /v1/channels/{id}/messages`, com paginação por cursor e autorização por canal.

## Tarefa

Retornar o histórico de mensagens de um canal ao qual o solicitante tenha acesso.

## Regras

- Revalidar a associação e as permissões do membro no canal.
- Usar paginação por cursor; formato do cursor, ordenação e tamanho máximo da página precisam ser definidos no contrato.
- Não expor mensagens de outros canais ou servidores.

## Definição de pronto

- O contrato documenta cursores, ordenação, limites e erros.
- Páginas consecutivas não perdem nem repetem mensagens devido à paginação.
- Acesso não autorizado é recusado.
- Testes cobrem paginação e isolamento de autorização.
