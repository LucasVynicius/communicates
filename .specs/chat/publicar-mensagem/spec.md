# Publicar mensagem

## Contexto

O endpoint inicial é `POST /v1/channels/{id}/messages`. A publicação deve verificar permissões e permitir recuperação de eventos após reconexão.

## Tarefa

Publicar uma mensagem num canal autorizado e emitir a atualização correspondente para os clientes inscritos.

## Regras

- Exigir chave de idempotência.
- Revalidar a autorização do membro no canal no momento da operação.
- Gravar mensagem e evento outbox na mesma transação.
- Definir limites, conteúdo aceito e formato dos erros no contrato OpenAPI.

## Definição de pronto

- Repetir a mesma chave não cria mensagens duplicadas.
- Um membro sem acesso não consegue publicar no canal.
- A mensagem persistida pode ser recuperada após reconexão.
- Testes cobrem sucesso, falta de autorização e repetição idempotente.
